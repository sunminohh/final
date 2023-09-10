package kr.co.mgv.movie.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data @Alias("MovieCommentLike")
@NoArgsConstructor
public class MovieCommentLike {
    private long commentNo;
    private String userId;
}
