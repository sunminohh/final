package kr.co.mgv.support.faq;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FaqDao {

	// Faq 리스트 조회
	int getTotalRows(Map<String, Object> param);
	List<Faq> getFaqListByNo(Map<String, Object> param);
	
}
