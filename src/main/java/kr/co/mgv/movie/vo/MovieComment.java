package kr.co.mgv.movie.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Alias("MovieComment")
public class MovieComment {
    private int no;
    private String userId;
    private int movieNo;
    private int rating;
    private Date createDate;
    private int commentLikes;
    private String comment;

}
