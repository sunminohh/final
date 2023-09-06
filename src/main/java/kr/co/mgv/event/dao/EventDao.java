package kr.co.mgv.event.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.event.vo.Event;
import kr.co.mgv.event.vo.EventCategory;

@Mapper
public interface EventDao {

	void insertEvent(Event event);
	
	int getTotalRows(Map<String, Object> param);
	List<Event> getEventList(Map<String, Object> param);
	
	
	List<EventCategory> getCategories();
	
}
