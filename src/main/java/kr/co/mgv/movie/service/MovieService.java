package kr.co.mgv.movie.service;

import kr.co.mgv.movie.dao.MovieCommentDao;
import kr.co.mgv.movie.dao.MovieDao;
import kr.co.mgv.movie.dao.MovieLikeDao;
import kr.co.mgv.movie.util.DateUtils;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.movie.vo.MovieComment;
import kr.co.mgv.movie.vo.MovieCommentLike;
import kr.co.mgv.movie.vo.MovieLike;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

class Node {

    Map<Character, Node> children;
    HashSet<String> data;

    public Node(){
        children=new HashMap<>();
        data=new LinkedHashSet<>();
    }
}

@Service
public class MovieService {
    private final char[] choSet = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
    private final char[] jungSet = { 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
            'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ',
            'ㅣ' };
    private final char[] jongSet = {'\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
            'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    private Node trie;

    public MovieService(MovieDao movieDao, MovieLikeDao movieLikesDao,MovieCommentDao movieCommentDao) {
        this.movieDao = movieDao;
        this.movieLikeDao = movieLikesDao;
        this.movieCommentDao=movieCommentDao;
    }

    public String getJamo(String inputWord){
        StringBuilder jamo=new StringBuilder();
        int l=inputWord.length();
        for (int i=0; i<l; i++){
            int uniCode = inputWord.charAt(i)& 0xFFFF;

            if (uniCode==32){
                jamo.append(' ');
                continue;
            }
            if (uniCode < 0xAC00 || uniCode > 0xD7A3) {
                if(uniCode>=65 && uniCode<=90) {
                    uniCode+=32;
                }
                jamo.append((char)uniCode);
                continue;
            }
            uniCode-=0xAC00;


            int jong = uniCode % 28;
            int jung = ((uniCode - jong) / 28 ) % 21;
            int cho = (((uniCode - jong) / 28 ) - jung ) / 21 ;

            jamo.append(choSet[cho]);
            jamo.append( jungSet[jung] );
            if (jong != 0) {
                jamo.append( jongSet[jong] );
            }
        }
        return jamo.toString();
    }
    public void insertWordIntoTrie(String keyword,Node head,String title) {
        int l=keyword.length();
        for(int i=0; i<l; i++) {
            char key=keyword.charAt(i);

            if(!head.children.containsKey(key)) {
                head.children.put(key, new Node());
            }
            head=head.children.get(key);
            head.data.add(title);
        }
    }

    public Node initTrie() {
        if(trie!=null) {
            return trie;
        }
        List<Movie> movies = movieDao.getAllMovies();
        trie =new Node();
        for(Movie movie : movies) {
            String title=getJamo(movie.getTitle());
            String director=getJamo(movie.getDirector());
            insertWordIntoTrie(director, trie, movie.getTitle());
            insertWordIntoTrie(title, trie, movie.getTitle());
            for(String actor : movie.getCast().split(", ")){
                insertWordIntoTrie(getJamo(actor),trie,movie.getTitle());
            }
        }
        return trie;
    }
    public List<String> searchWord(String keyword){
       String keywordJamo=getJamo(keyword);
        Node head=initTrie();
        int l=keywordJamo.length();
        for(int i=0; i<l; i++) {
            char key=keywordJamo.charAt(i);

            if(!head.children.containsKey(key)) {
                return new ArrayList<String>();
            }
            head=head.children.get(key);
        }
        List<String> result = new ArrayList<String>();
        for (String x : head.data) {
            result.add(x);
            if(result.size()==10) {
                break;
            }
        }

        return result;
    }

    private boolean compChar(char x, char y){
        if(x==y){
            return true;
        }
        return (x - 32 == y || x == y - 32) && ((x >= 65 && x <= 90) || (x >= 97 && x <= 122)) &&
                ((y >= 65 && y <= 90) || (y >= 97 && y <= 122));
    }

    public String modifyString(String target, String s) {
        if (target.isBlank()) {
            return target;
        }

        StringBuilder sb = new StringBuilder();
        int l=target.length();
        int[] tb = new int[l];
        char[] a= target.toCharArray();
        char[] b= s.toCharArray();
        int i,j;
        for( i=1, j=0; i<l; i++){
            while(j>0 && compChar(b[i],b[j])){
                j=tb[j-1];
            }
            if(compChar(b[i],b[j])){
                tb[i]=++j;
            }
        }
        for ( i=0, j=0; i<l; i++){
            while(j>0 && compChar(a[i],b[j])){
                j=tb[j-1];
            }
            if(compChar(a[i],b[j])){
                if(j== l-1){
                    sb.append("<span style='background-color:yellow'>"+s.substring(i, i+l)+"</span>");
                    j=tb[j];
                }else j++;
            }

        }
        sb.append(s.substring(i));

        return sb.toString();
    }
    private final MovieDao movieDao;
    private final MovieLikeDao movieLikeDao;
    private final MovieCommentDao movieCommentDao;
    private static final String KOBIS_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    private static final String KOBIS_API_KEY = "45ac471b35ca42c983d971a438b31d25";
    private static final String KMDB_API_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y&ServiceKey=Y40OV2CFS1I2MTV081VG";
    private static final JSONParser JSON_PARSER = new JSONParser();

    public Movie getMovieByMovieNo(int movieNo){
        return movieDao.getMovieByMovieNo(movieNo);
    }
    public List<Movie> getMovies() {

        List<Movie> movies = new ArrayList<Movie>();

        try {

            DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(KOBIS_API_URL);
            factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
            WebClient webClient = WebClient.builder().uriBuilderFactory(factory).baseUrl(KOBIS_API_URL).build();
          String kobisResponse = webClient.get().uri(uriBuilder -> uriBuilder
                    .queryParam("key",KOBIS_API_KEY)
                    .queryParam("targetDt",LocalDate.now().minus(1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .build())
                    .retrieve()
                    .bodyToMono(String.class)
                   .block();

            JSONObject  kobisResponseJson = (JSONObject) JSON_PARSER.parse(kobisResponse);

            JSONObject resultObject = (JSONObject) kobisResponseJson.get("boxOfficeResult");
            JSONArray resultArray = (JSONArray)resultObject.get("dailyBoxOfficeList");

            for (Object o : resultArray) {
                JSONObject dailyBoxOfficeObj = (JSONObject) o;
                Movie movie = new Movie();
                movie.setChartRank(Integer.parseInt((String) dailyBoxOfficeObj.get("rank")));
                movie.setTitle((String) dailyBoxOfficeObj.get("movieNm"));
                String stringDate = dailyBoxOfficeObj.get("openDt").toString();
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
                        URLEncoder.encode(movieTitle, StandardCharsets.UTF_8),
                        URLEncoder.encode(DateUtils.toText(movie.getReleaseDate()).replace("-",""), StandardCharsets.UTF_8)
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
                    sb.append(actor.get("actorNm") +", ");
                }
                actor= (JSONObject)actorArray.get(actorIndex);
                sb.append((String)actor.get("actorNm"));
                String cast=sb.toString();
                String titleEng = (String) result.get("titleEng");
                String[] titleEngModified = titleEng.split("\\(");
                movie.setTitleEng(titleEngModified[0]);
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

        HashSet<Integer> movieNos= 	movieDao.getMovieNos();

        for (Movie movie: movies){
            if (movieNos.contains(movie.getNo())){
                movieDao.updateMovie(movie);
                movieNos.remove(movie.getNo());
            }else {
                movieDao.insertMovie(movie);
            }
        }

        for (int movieNo : movieNos){
            movieDao.initChart(movieNo);
        }
        return movies;
    }

    public void sync(){
        try {
            List<Movie> movies = movieDao.getAllMovies();
            for (Movie movie : movies) {
                String movieTitle=movie.getTitle();
                movieTitle=movieTitle.replace("무삭제","");
                String apiUrl = String.format(
                        "%s&title=%s&releaseDts=%s",
                        KMDB_API_URL,
                        URLEncoder.encode(movieTitle, StandardCharsets.UTF_8),
                        URLEncoder.encode(DateUtils.toText(movie.getReleaseDate()).replace("-",""), StandardCharsets.UTF_8)
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
                    sb.append(poster[i]).append(" ");
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
                String titleEng = (String) result.get("titleEng");
                String[] titleEngModified = titleEng.split("\\(");
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
                    sb.append(actor.get("actorNm") +", ");
                }
                actor= (JSONObject)actorArray.get(actorIndex);
                sb.append((String)actor.get("actorNm"));
                String cast=sb.toString();

                movie.setTitleEng(titleEngModified[0]);
                movie.setIsPlaying("N");
                movie.setPosterUrl(posters);
                movie.setGenre((String)result.get("genre"));
                movie.setContentRating(contentRating);
                movie.setDirector(directorName);
                movie.setPlot(plot);
                movie.setCast(cast);
                movie.setRuntime(runtime);
                movieDao.syncMovie(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Movie> getMovieChart(int rowNum){
        return movieDao.getMoviesByRowNum(rowNum);
    }
    public List<Movie> getMovieChart(){
        return movieDao.getMoviesByRowNum(10);
    }

    public List<Movie> getAllMovies(){return movieDao.getAllMovies();}
    public void insertMovieLike(MovieLike movieLike){
        movieLikeDao.insertMovieLike(movieLike);
    }
    public void deleteMovieLike(MovieLike movielike){movieLikeDao.deleteMovieLike(movielike);}

    public void incrementMovielikes(int movieNo){movieDao.incrementMovieLikes(movieNo);}
    public void decrementMovielikes(int movieNo){movieDao.decrementMovieLikes(movieNo);}

    public boolean isMovieLikedByUser(MovieLike movieLike){
        return movieLikeDao.isMovieLikedByUser(movieLike) != null;
    }
    public HashSet<Integer> getAllLikedMovieNos(String userId){
        return movieLikeDao.getLikedMovieNosByUserId(userId);
    }
    public void insertMovieComment(MovieComment movieComment){
        movieCommentDao.insertMovieComment(movieComment);
        Movie movie = movieDao.getMovieByMovieNo(movieComment.getMovieNo());
        movie.setScore(movie.getScore()+ movieComment.getCommentRating());
        movie.setScoreGiver(movie.getScoreGiver()+1);
        movieDao.updateMovie(movie);
    }

    public List<Movie> getFavoriteMoviesByUserId(String id){
        HashSet<Integer> fav=getAllLikedMovieNos(id);

        return getAllMovies().stream().filter(movie->fav.contains(movie.getNo())).collect(Collectors.toList());
    }
    public List<MovieComment> getMovieCommentsByMovieNo(int movieNo){return movieCommentDao.getMovieCommentsByMovieNo(movieNo);}
    public void deleteMovieCommentLike(MovieCommentLike movieCommentLike){
        movieCommentDao.decrementMovieCommentLike(movieCommentLike.getCommentNo());
        movieCommentDao.deleteMovieCommentLike(movieCommentLike );}
    public void insertMovieCommentLike(MovieCommentLike movieCommentLike){
        movieCommentDao.incrementMovieCommentLike(movieCommentLike.getCommentNo());
        movieCommentDao.insertMovieCommentLike(movieCommentLike );}

    public void deleteMovieComment(long no){
        MovieComment movieComment= movieCommentDao.getMovieCommentByCommentNo(no);
        Movie movie = movieDao.getMovieByMovieNo(movieComment.getMovieNo());
        movie.setScore(movie.getScore()-movieComment.getCommentRating());
        movie.setScoreGiver(movie.getScoreGiver()-1);
        movieDao.updateMovie(movie);
        movieCommentDao.deleteMovieCommentByNo(no);
        movieCommentDao.deleteMovieCommentLikeByCommentNo(no);
    }

}
