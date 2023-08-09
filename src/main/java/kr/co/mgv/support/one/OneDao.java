package kr.co.mgv.support.one;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OneDao {

	void insertOne(One one);
	
}
