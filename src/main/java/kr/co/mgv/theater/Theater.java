package kr.co.mgv.theater;

import org.apache.ibatis.type.Alias;
import kr.co.mgv.theater.location.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("theater")
public class Theater {

	private int no;
	private Location location;
	private String name;
	private String address;
	private String tel;
	private String disabled;
	private String parkingInfo;
	private String facilityInfo;
	private String description;
	
	public Theater(int no) {
		this.no = no;
	}
}
