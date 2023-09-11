package kr.co.mgv.movie.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Alias("MovieComment")
public class MovieComment {
    private long no;
    private String userId;
    private String profileImage;
    private int movieNo;
    private int commentRating;
    private Date createDate;
    private int commentLikes;
    private String commentContent;
    private boolean isLiked;

}
