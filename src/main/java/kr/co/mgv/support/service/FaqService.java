package kr.co.mgv.support.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.dao.FaqDao;
import kr.co.mgv.support.dto.FaqList;
import kr.co.mgv.support.form.AddFaqForm;
import kr.co.mgv.support.vo.Faq;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FaqService {

	private final FaqDao faqDao;
	
	public void insertFaq(AddFaqForm form, User user) {
		
		SupportCategory category = SupportCategory.builder()
								.no(form.getCategoryNo())
								.build();
		
		Faq faq = Faq.builder()
					.user(user)
					.category(category)
					.title(form.getTitle())
					.title(form.getContent())
					.build();
		
		faqDao.insertFaq(faq);
	}
	
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
	
	public List<SupportCategory> getCategoriesByType(String categoryType) {
		
		return faqDao.getCategories(categoryType);
	}
}


























