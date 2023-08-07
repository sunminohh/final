package kr.co.mgv.support.faq;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FaqDao {

	// Faq 리스트 조회
	List<Faq> getFaqListByNo(int catNo);
}
