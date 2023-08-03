package kr.co.mgv.movie.vo;

import kr.co.mgv.movie.util.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.util.Date;


@Setter
@Getter
@ToString
@NoArgsConstructor
@Alias("Movie")
public class Movie {

    private int no;

    private double rating;
    private int chartRank;
    private int rankInten;
    private int audiCnt;
    private String title;
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

}