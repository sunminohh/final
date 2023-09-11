package kr.co.mgv.movie.vo;

import kr.co.mgv.movie.util.DateUtils;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@Alias("Movie")
public class Movie {

    private int no;

    private double rating;
    private int chartRank;
    private int rankInten;
    private int audiCnt;
    private String title;
    private String titleEng;
    private String genre;
    private String contentRating;
    private String director;
    private String cast;
    private String plot;
    private String posterUrl;
    private String isPlaying;
    private int runtime;
    private String rankOldAndNew;
    private Date releaseDate;
    private Date updateDate;
    private int score;
    private int scoreGiver;
    private int likes;
    private Date openDate;
    private Date closeDate;
    private int seatsOpen;
    private int seatsBooked;
    private boolean isLiked;
    public String getStringFormattedDate() {
        return DateUtils.toText(releaseDate);
    }
    public String getMainPoster() {
        String[] poster = posterUrl.split(" ");
        if (poster.length==0) {
            return null;
        }
        return poster[0];
    }
    public String getSubPoster() {
        String[] poster = posterUrl.split(" ");
        if (poster.length==0) {
            return null;
        }
        if (poster.length>1) {
            return poster[1];
        }
        return poster[0];
    }


    public double getAverageScore(){
        if (score == 0){
            return 0;
        }
        return  Math.round((double) score/scoreGiver*10)/10.0;
    }
    public List<String> getActors(){
        return Arrays.stream(cast.split(", ")).collect(Collectors.toList());
    }
    @Builder
	public Movie(int no, double rating, int chartRank, int rankInten, int audiCnt, String title, String titleEng,
			String genre, String contentRating, String director, String cast, String plot, String posterUrl,
			String isPlaying, int runtime, String rankOldAndNew, Date releaseDate, Date updateDate, int score,
			int scoreGiver, int likes, Date openDate, Date closeDate, int seatsOpen, int seatsBooked, boolean isLiked) {
		super();
		this.no = no;
		this.rating = rating;
		this.chartRank = chartRank;
		this.rankInten = rankInten;
		this.audiCnt = audiCnt;
		this.title = title;
		this.titleEng = titleEng;
		this.genre = genre;
		this.contentRating = contentRating;
		this.director = director;
		this.cast = cast;
		this.plot = plot;
		this.posterUrl = posterUrl;
		this.isPlaying = isPlaying;
		this.runtime = runtime;
		this.rankOldAndNew = rankOldAndNew;
		this.releaseDate = releaseDate;
		this.updateDate = updateDate;
		this.score = score;
		this.scoreGiver = scoreGiver;
		this.likes = likes;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.seatsOpen = seatsOpen;
		this.seatsBooked = seatsBooked;
        this.isLiked = isLiked;
	}

    public double getBookingRate(){
        if(seatsOpen==0){
            return 0.0;
        }
        return Math.round(((double) seatsBooked / seatsOpen)*10000.0)/100.0;
    }
    public String getContentRatingKr(){
        if ("12".equals(contentRating)){
            return "12세이용가";
        }else if("15".equals(contentRating)){
            return "15세이용가";
        }else if("18".equals(contentRating)){
            return "청소년관람불가";
        }else if("all".equals(contentRating)){
            return "전체이용가";
        }else return "상영등급미정";
    }
    
}