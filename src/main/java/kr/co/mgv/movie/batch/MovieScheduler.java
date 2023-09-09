package kr.co.mgv.movie.batch;

import kr.co.mgv.movie.dao.MovieDao;
import kr.co.mgv.movie.util.DateUtils;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.movie.vo.MovieCollect;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class MovieScheduler {

    private final MovieDao movieDao;
    private final WebClient webClient;
    private final JSONParser jsonParser;
    private final DateTimeFormatter formatter;

    @Value("${movie.kobis.key}")
    private String KOBIS_API_KEY;
    @Value("${movie.kobis.url}")
    private String KOBIS_API_URL;

    @Value("${movie.kmdb.key}")
    private String KMDB_API_KEY;
    @Value("${movie.kmdb.url}")
    private String KMDB_API_URL;

    // Constructor
    public MovieScheduler(MovieDao movieDao, WebClient.Builder webClientBuilder) {

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(1024 * 1024))
            .build();

        this.movieDao = movieDao;
        this.webClient = webClientBuilder.exchangeStrategies(exchangeStrategies).build();
        this.jsonParser = new JSONParser();
        this.formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    // 어제 데이터 수집, 매일 0시 0분 0초 실행
    @Scheduled(cron = "0 0 0 * * ?")
    private void collectYesterdayBoxOffice() throws Exception {
        String targetDt = LocalDate.now().minusDays(1).format(formatter);
        List<MovieCollect> movieCollects = this.collectBoxOffice(targetDt);
        movieCollects.forEach(this::syncMovies);
    }

    // 지난 한달간 데이터 수집, 테스트용
//    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24, initialDelay = 1000 * 3)
    private void collectOneMonthBoxOffice() {
        LocalDate start = LocalDate.now().minusMonths(12);
        LocalDate end = LocalDate.now();

        Stream.iterate(start, date -> date.isBefore(end), date -> date.plusDays(1))
            .map(date -> date.format(formatter))
            .map(this::collectBoxOffice)
            .forEach(movieCollects -> movieCollects.forEach(this::syncMovies));
    }

    private List<MovieCollect> collectBoxOffice(String targetDt) {

        log.info("[SYNC MOVIE] START, TARGET DATE => {}", targetDt);

        String response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(KOBIS_API_URL)
                .queryParam("key", KOBIS_API_KEY)
                .queryParam("targetDt", targetDt)
                .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.info("[SYNC MOVIE] API REQUEST SUCCESSFUL!");

        try {
            JSONObject responseJson = (JSONObject) jsonParser.parse(response);
            JSONObject boxOfficeResult = (JSONObject) responseJson.get("boxOfficeResult");
            JSONArray dailyBoxOfficeList = (JSONArray) boxOfficeResult.get("dailyBoxOfficeList");
            log.info("[SYNC MOVIE] BOX OFFICE SIZE => {}", dailyBoxOfficeList.size());

            Stream<MovieCollect> stream = dailyBoxOfficeList.stream().map(dailyBoxOfficeObj -> {
                JSONObject dailyBoxOffice = (JSONObject) dailyBoxOfficeObj;
                String title = (String) dailyBoxOffice.get("movieNm");
                Date openDt = DateUtils.toDate((String) dailyBoxOffice.get("openDt"));
                int rank = Integer.parseInt((String) dailyBoxOffice.get("rank"));
                int movieCd = Integer.parseInt((String) dailyBoxOffice.get("movieCd"));
                long rankInten = Long.parseLong((String) dailyBoxOffice.get("rankInten"));
                String rankOldAndNew = (String) dailyBoxOffice.get("rankOldAndNew");
                long scrnCnt = Long.parseLong((String) dailyBoxOffice.get("scrnCnt"));
                long showCnt = Long.parseLong((String) dailyBoxOffice.get("showCnt"));
                double salesInten = Double.parseDouble((String) dailyBoxOffice.get("salesInten"));
                double salesChange = Double.parseDouble((String) dailyBoxOffice.get("salesChange"));
                long salesAcc = Long.parseLong((String) dailyBoxOffice.get("salesAcc"));
                long salesAmt = Long.parseLong((String) dailyBoxOffice.get("salesAmt"));
                long audiCnt = Long.parseLong((String) dailyBoxOffice.get("audiCnt"));
                double audiInten = Double.parseDouble((String) dailyBoxOffice.get("audiInten"));
                double audiChange = Double.parseDouble((String) dailyBoxOffice.get("audiChange"));
                long audiAcc = Long.parseLong((String) dailyBoxOffice.get("audiAcc"));

                return MovieCollect.builder()
                    .movieNo(movieCd)
                    .targetDt(targetDt)
                    .title(title)
                    .openDt(openDt)
                    .rank(rank)
                    .rankInten(rankInten)
                    .rankOldAndNew(rankOldAndNew)
                    .scrnCnt(scrnCnt)
                    .showCnt(showCnt)
                    .salesInten(salesInten)
                    .salesChange(salesChange)
                    .salesAcc(salesAcc)
                    .salesAmt(salesAmt)
                    .audiCnt(audiCnt)
                    .audiInten(audiInten)
                    .audiChange(audiChange)
                    .audiAcc(audiAcc)
                    .collectDt(LocalDateTime.now())
                    .build();
            });

            List<MovieCollect> movies = stream.collect(Collectors.toList());
            movieDao.insertMovieCollect(movies);
            return movies;
        } catch (Exception e) {
            log.error("[SYNC MOVIE] BOX OFFICE Collect Failed", e);
            return Collections.emptyList();
        }
    }

    private void syncMovies(MovieCollect movieCollect) {

        Movie.MovieBuilder movieBuilder = Movie.builder()
            .no(movieCollect.getMovieNo())
            .title(movieCollect.getTitle())
            .chartRank(movieCollect.getRank())
            .releaseDate(movieCollect.getOpenDt())
            .audiCnt((int) movieCollect.getAudiCnt())
            .rankInten((int) movieCollect.getRankInten())
            .rankOldAndNew(movieCollect.getRankOldAndNew());

        try {
            String detailResponse = webClient.get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                        .path(KMDB_API_URL)
                        .queryParam("title", movieCollect.getTitle().replace("무삭제", ""))
                        .queryParam("releaseDts", DateUtils.toText(movieCollect.getOpenDt()).replace("-", ""))
                        .queryParam("ServiceKey", KMDB_API_KEY)
                        .queryParam("detail", "Y")
                        .queryParam("collection", "kmdb_new2")
                        .build();
                    log.info("\t[SYNC MOVIE] REQUEST DETAIL => {}", UriEncoder.decode(uri.toString()));
                    return uri;
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();

            JSONObject movieDetailWrap = (JSONObject) jsonParser.parse(detailResponse);
            JSONObject data = (JSONObject) ((JSONArray) movieDetailWrap.get("Data")).get(0);
            JSONObject movieDetail = (JSONObject)((JSONArray) data.get("Result")).get(0);

            // 포스터 정보
            String posters = Arrays.stream(movieDetail.get("posters").toString().split("\\|")).limit(3).collect(Collectors.joining(" "));

            // 감독 정보
            JSONObject directorWrap = (JSONObject) movieDetail.get("directors");
            JSONObject director = (JSONObject)((JSONArray) directorWrap.get("director")).get(0);
            String directorNm = director.get("directorNm").toString();

            // 줄거리 정보
            JSONObject plotWrap = (JSONObject) movieDetail.get("plots");
            JSONObject plotObj = (JSONObject) ((JSONArray) plotWrap.get("plot")).get(0);
            String plotText = plotObj.get("plotText").toString();

            // 상영시간과 등급
            int runtime = Integer.parseInt(movieDetail.get("runtime").toString());
            String contentRating = movieDetail.get("rating").toString();
            contentRating = ContentRating.convert(contentRating);

            // 배우 정보
            JSONArray actorArray = (JSONArray) ((JSONObject) movieDetail.get("actors")).get("actor");
            String actors = (String) actorArray.stream().limit(3).map(o -> ((JSONObject) o).get("actorNm").toString()).collect(Collectors.joining(", "));

            // 영어 제목
            String titleEng = movieDetail.get("titleEng").toString().split("\\(")[0];
            String genre = movieDetail.get("genre").toString();

            movieBuilder = movieBuilder.posterUrl(posters)
                .director(directorNm)
                .plot(plotText)
                .runtime(runtime)
                .contentRating(contentRating)
                .cast(actors)
                .genre(genre);

            movieDao.saveMovie(movieBuilder.build());
        } catch (Exception e) {
            log.error("[SYNC MOVIE] {}: DETAIL Sync Failed", movieCollect.getTitle());
        }

    }

    private enum ContentRating {
        ALL("전체관람가", "all"),
        AGE_12("12세관람가", "12"),
        AGE_15("15세관람가", "15"),
        AGE_18("18세관람가", "18"),
        UNKNOWN("UNKNOWN", "no");

        private final String koreanName;
        @Getter
        private final String code;

        ContentRating(String koreanName, String code) {
            this.koreanName = koreanName;
            this.code = code;
        }

        public static String convert(String koreanName) {
            for (ContentRating rating : values()) {
                if (rating.koreanName.equals(koreanName)) {
                    return rating.getCode();
                }
            }
            return UNKNOWN.getCode();
        }
    }
}
