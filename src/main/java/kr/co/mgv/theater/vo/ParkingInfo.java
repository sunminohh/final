package kr.co.mgv.theater.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Alias("Parkinginfo")
public class ParkingInfo {

	private int theaterNo;
	private String id;
	private String info;
	private String confirm;
	private String cash;
	
	public String[] getInfoList() {
		return this.info.split("\n");
	}
	public String[] getConfirmList() {
		return this.confirm.split("\n");
	}
	public String[] getCashList() {
		return this.cash.split("\n");
	}
}
