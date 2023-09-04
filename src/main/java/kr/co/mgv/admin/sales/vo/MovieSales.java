package kr.co.mgv.admin.sales.vo;

import org.apache.ibatis.type.Alias;

import kr.co.mgv.movie.vo.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("movieSales")
public class MovieSales {

	private Integer id;
	private Movie movie;
	private Integer audience;
	private String date;
	private Integer totalSales;
}
