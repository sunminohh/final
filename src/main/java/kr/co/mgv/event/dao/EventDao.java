package kr.co.mgv.event.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.event.vo.Event;
import kr.co.mgv.event.vo.EventCategory;

@Mapper
public interface EventDao {

	void insertEvent(Event event);
	
	List<EventCategory> getCategories();
	
}
