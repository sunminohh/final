package kr.co.mgv.movie.service;

import kr.co.mgv.movie.mapper.MovieMapper;
import kr.co.mgv.movie.util.DateUtils;
import kr.co.mgv.movie.vo.Movie;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class MovieService {


    @Autowired
    private final MovieMapper movieMapper;
    private static final String KOBIS_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=45ac471b35ca42c983d971a438b31d25&targetDt=";
    private static final String KMDB_API_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y&ServiceKey=Y40OV2CFS1I2MTV081VG";
    private static final JSONParser JSON_PARSER = new JSONParser();

    public List<Movie> getMovies() {

        List<Movie> movies = new ArrayList<Movie>();

        try {
            String kobisURLString = KOBIS_API_URL+LocalDate.now().minus(1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);
            UriComponents kobisUri = UriComponentsBuilder.fromHttpUrl(kobisURLString).build();
            ResponseEntity<Map> kobisResponseEntity = restTemplate.exchange(kobisUri.toString(), HttpMethod.GET,entity,Map.class);
            LinkedHashMap resultMap = (LinkedHashMap)kobisResponseEntity.getBody().get("boxOfficeResult");

            ArrayList<Map> dailyBoxOfficeList = (ArrayList<Map>)resultMap.get("dailyBoxOfficeList");

            for (Map dailyBoxOfficeObj : dailyBoxOfficeList) {
                Movie movie = new Movie();
                movie.setChartRank(Integer.parseInt((String) dailyBoxOfficeObj.get("rank")));
                movie.setTitle((String) dailyBoxOfficeObj.get("movieNm"));
                String stringDate= dailyBoxOfficeObj.get("openDt").toString();
                if (!stringDate.isBlank() && !stringDate.isEmpty()) {
                    movie.setReleaseDate(DateUtils.toDate(stringDate));
                }
                movie.setAudiCnt(Integer.parseInt((String) dailyBoxOfficeObj.get("audiAcc")));
                movie.setNo(Integer.parseInt((String) dailyBoxOfficeObj.get("movieCd")));
                movie.setRankInten(Integer.parseInt((String) dailyBoxOfficeObj.get("rankInten")));
                movie.setRankOldAndNew((String) dailyBoxOfficeObj.get("rankOldAndNew"));
                movies.add(movie);
            }

            for (Movie movie : movies) {
                String movieTitle=movie.getTitle();
                movieTitle=movieTitle.replace("무삭제","");
                String apiUrl = String.format(
                        "%s&title=%s&releaseDts=%s",
                        KMDB_API_URL,
                        URLEncoder.encode(movieTitle, "UTF-8"),
                        URLEncoder.encode(DateUtils.toText(movie.getReleaseDate()).replace("-",""), "UTF-8")
                );

                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String kmdbApi;
                while ((kmdbApi = rd.readLine()) != null) {
                    sb.append(kmdbApi);
                }
                rd.close();
                conn.disconnect();

                String movieDetail = sb.toString();
                Object obj = JSON_PARSER.parse(movieDetail);
                JSONObject result = (JSONObject) obj;
                JSONArray result1 = (JSONArray) result.get("Data");
                result = (JSONObject) result1.get(0);
                result1 = (JSONArray) result.get("Result");
                result = (JSONObject) result1.get(0);
                String posters = (String) result.get("posters");
                String[] poster = posters.split("\\|");
                sb= new StringBuilder();
                for (int i=0; i<(poster.length<4? poster.length : 3); i++) {
                    sb.append(poster[i]+" ");
                }
                posters=sb.toString();
                JSONObject directorResult = (JSONObject)result.get("directors");
                JSONArray directorResultArray = (JSONArray)directorResult.get("director");
                directorResult=(JSONObject)directorResultArray.get(0);
                String directorName=(String)directorResult.get("directorNm");


                JSONObject plotResult = (JSONObject)result.get("plots");
                JSONArray descriptionResultArray = (JSONArray)plotResult.get("plot");
                plotResult=(JSONObject)descriptionResultArray.get(0);
                String plot=(String)plotResult.get("plotText");

                int runtime = Integer.parseInt((String) result.get("runtime"));
                String contentRating = (String) result.get("rating");
                if("전체관람가".equals(contentRating)){
                    contentRating="all";
                }else if ("12세관람가".equals(contentRating)){
                    contentRating="12";
                }else if("15세관람가".equals(contentRating)){
                    contentRating="15";
                }else if("18세관람가".equals(contentRating)){
                    contentRating="18";
                }else contentRating="no";
                JSONObject actor = (JSONObject)result.get("actors");
                JSONArray actorArray = (JSONArray)actor.get("actor");
                sb=new StringBuilder();
                int actorIndex;
                for (actorIndex=0; actorIndex< (actorArray.size()<3 ? actorArray.size()-1 : 2); actorIndex++ ) {
                    actor= (JSONObject)actorArray.get(actorIndex);
                    sb.append((String)actor.get("actorNm")+", ");
                }
                actor= (JSONObject)actorArray.get(actorIndex);
                sb.append((String)actor.get("actorNm"));
                String cast=sb.toString();

                movie.setIsPlaying("N");
                movie.setPosterUrl(posters);
                movie.setGenre((String)result.get("genre"));
                movie.setContentRating(contentRating);
                movie.setDirector(directorName);
                movie.setPlot(plot);
                movie.setCast(cast);
                movie.setRuntime(runtime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashSet<Integer> movieNos= 	movieMapper.getMovieNos();

        for (Movie movie: movies){
            if (movieNos.contains(movie.getNo())){
                movieMapper.updateMovie(movie);
                movieNos.remove(movie.getNo());
            }else {
                movieMapper.insertMovie(movie);
            }
        }

        for (int movieNo : movieNos){
            movieMapper.initChart(movieNo);
        }
        return movies;
    }

    public void sync(){
        try {
            List<Movie> movies = movieMapper.getAllMovies();
            for (Movie movie : movies) {
                String movieTitle=movie.getTitle();
                movieTitle=movieTitle.replace("무삭제","");
                String apiUrl = String.format(
                        "%s&title=%s&releaseDts=%s",
                        KMDB_API_URL,
                        URLEncoder.encode(movieTitle, "UTF-8"),
                        URLEncoder.encode(DateUtils.toText(movie.getReleaseDate()).replace("-",""), "UTF-8")
                );

                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String kmdbApi;
                while ((kmdbApi = rd.readLine()) != null) {
                    sb.append(kmdbApi);
                }
                rd.close();
                conn.disconnect();

                String movieDetail = sb.toString();
                Object obj = JSON_PARSER.parse(movieDetail);
                JSONObject result = (JSONObject) obj;
                JSONArray result1 = (JSONArray) result.get("Data");
                result = (JSONObject) result1.get(0);
                result1 = (JSONArray) result.get("Result");
                result = (JSONObject) result1.get(0);
                String posters = (String) result.get("posters");
                String[] poster = posters.split("\\|");
                sb= new StringBuilder();
                for (int i=0; i<(poster.length<4? poster.length : 3); i++) {
                    sb.append(poster[i]+" ");
                }
                posters=sb.toString();
                JSONObject directorResult = (JSONObject)result.get("directors");
                JSONArray directorResultArray = (JSONArray)directorResult.get("director");
                directorResult=(JSONObject)directorResultArray.get(0);
                String directorName=(String)directorResult.get("directorNm");


                JSONObject plotResult = (JSONObject)result.get("plots");
                JSONArray descriptionResultArray = (JSONArray)plotResult.get("plot");
                plotResult=(JSONObject)descriptionResultArray.get(0);
                String plot=(String)plotResult.get("plotText");

                int runtime = Integer.parseInt((String) result.get("runtime"));
                String contentRating = (String) result.get("rating");
                if("전체관람가".equals(contentRating)){
                    contentRating="all";
                }else if ("12세관람가".equals(contentRating)){
                    contentRating="12";
                }else if("15세관람가".equals(contentRating)){
                    contentRating="15";
                }else if("18세관람가".equals(contentRating)){
                    contentRating="18";
                }else contentRating="no";

                JSONObject actor = (JSONObject)result.get("actors");
                JSONArray actorArray = (JSONArray)actor.get("actor");
                sb=new StringBuilder();
                int actorIndex;
                for (actorIndex=0; actorIndex< (actorArray.size()<3 ? actorArray.size()-1 : 2); actorIndex++ ) {
                    actor= (JSONObject)actorArray.get(actorIndex);
                    sb.append((String)actor.get("actorNm")+", ");
                }
                actor= (JSONObject)actorArray.get(actorIndex);
                sb.append((String)actor.get("actorNm"));
                String cast=sb.toString();

                movie.setIsPlaying("N");
                movie.setPosterUrl(posters);
                movie.setGenre((String)result.get("genre"));
                movie.setContentRating(contentRating);
                movie.setDirector(directorName);
                movie.setPlot(plot);
                movie.setCast(cast);
                movie.setRuntime(runtime);
                movieMapper.syncMovie(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Movie> getMovieChart(int rowNum){
        return movieMapper.getMoviesByRowNum(rowNum);
    }
    public List<Movie> getMovieChart(){
        return movieMapper.getMoviesByRowNum(10);
    }
}
