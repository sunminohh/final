package kr.co.mgv.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class MovieCommentDto {
    private int no;
    private String userId;
    private int movieNo;
    private int rating;
    private Date createDate;
    private int commentLikes;
    private String comment;
}
