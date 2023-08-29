package kr.co.mgv.theater.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
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
	private String info;
	private List<TheaterFacility> facilities;
	private List<FloorInfo> floorInfos;
	private ParkingInfo parkingInfo;
	
	@Builder
	public Theater(int no, Location location, String name, String address, String tel, String disabled, String info,
			List<TheaterFacility> facilities, List<FloorInfo> floorInfos, ParkingInfo parkingInfo) {
		super();
		this.no = no;
		this.location = location;
		this.name = name;
		this.address = address;
		this.tel = tel;
		this.disabled = disabled;
		this.info = info;
		this.facilities = facilities;
		this.floorInfos = floorInfos;
		this.parkingInfo = parkingInfo;
	}
	
	public String getInfos() {
		if(this.info==null) {
			return null;
		}
		return this.info.replace("\n","<br>");
		
	}

}
