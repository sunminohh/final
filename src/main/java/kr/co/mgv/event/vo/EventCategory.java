package kr.co.mgv.event.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventCategory {

	/*
	 * SELECT CATEGORY_NO, CATEGORY_NAME
	FROM jhta_2302.MGV_EVENT_CATEGORIES;
	 */
	
	private int no;
	private String name;
	
	@Builder
	public EventCategory(int no, String name) {
		super();
		this.no = no;
		this.name = name;
	}
	
}
