package kr.co.mgv.theater;

import java.util.List;

import org.apache.ibatis.type.Alias;
<<<<<<< HEAD
=======

import kr.co.mgv.theater.facility.TheaterFacility;
import kr.co.mgv.theater.floors.FloorInfo;
>>>>>>> develop
import kr.co.mgv.theater.location.Location;
import kr.co.mgv.theater.parking.ParkingInfo;
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
<<<<<<< HEAD
	private String parkingInfo;
	private String facilityInfo;
	private String description;
	
	public Theater(int no) {
		this.no = no;
	}
=======
	private String info;
	private List<TheaterFacility> facilities;
	private List<FloorInfo> floorInfos;
	private ParkingInfo parkingInfo;
>>>>>>> develop
}
