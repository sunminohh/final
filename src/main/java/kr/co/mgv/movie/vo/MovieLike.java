package kr.co.mgv.movie.vo;

import lombok.*;
import org.apache.ibatis.type.Alias;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Alias("MovieLike")
public class MovieLike {
    private String userId;
    private int movieNo;
}
