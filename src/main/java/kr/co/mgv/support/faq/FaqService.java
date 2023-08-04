package kr.co.mgv.support.faq;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FaqService {

	private final FaqDao faqDao;
	
	// 자주묻는질문 목록 조회하기
	public List<Faq> getFaqs() {
		return faqDao.getFaqs();
	}
}
