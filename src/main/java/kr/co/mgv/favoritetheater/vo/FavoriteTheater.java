package kr.co.mgv.favoritetheater.vo;


import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Alias("FavoriteTheater")
public class FavoriteTheater {
	
	private String userId;
	private int rank;
	private String theaterName;
	private int theaterNo;
}
