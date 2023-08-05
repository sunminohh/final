package kr.co.mgv.movie.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Alias("MovieLikes")
public class MovieLikes {
    private String userId;
    private int movieNo;
}
