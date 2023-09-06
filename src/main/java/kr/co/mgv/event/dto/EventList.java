package kr.co.mgv.event.dto;

import java.util.List;

import kr.co.mgv.event.vo.Event;
import kr.co.mgv.support.vo.SupportPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventList {

	private SupportPagination pagination;
	private List<Event> eventList;
	
}
