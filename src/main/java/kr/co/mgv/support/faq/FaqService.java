package kr.co.mgv.support.faq;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.SupportPagination;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FaqService {

	private final FaqDao faqDao;
	
	public FaqList search(Map<String, Object> param) {
		
		int totalRows = faqDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<Faq> faqList = faqDao.getFaqListByNo(param);
		FaqList result = new FaqList();
		
		result.setPagination(pagination);
		result.setFaqList(faqList);
		
		return result;
	}
}


























