package kr.co.mgv.event.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.event.vo.Event;

@Mapper
public interface EventDao {

	void insertEvent(Event event);
	
}
