package kr.co.mgv.support.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.vo.Faq;
import kr.co.mgv.support.vo.SupportCategory;

@Mapper
public interface FaqDao {

	// Faq 리스트 조회
	int getTotalRows(Map<String, Object> param);
	List<Faq> getFaqListByNo(Map<String, Object> param);
	
	// faq 카테고리 조회
	List<SupportCategory> getCategories(String categoryType);
	
	// faq 등록
	void insertFaq(Faq faq);
}
