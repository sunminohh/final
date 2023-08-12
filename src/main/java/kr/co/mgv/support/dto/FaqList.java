package kr.co.mgv.support.dto;

import java.util.List;

import kr.co.mgv.support.vo.Faq;
import kr.co.mgv.support.vo.SupportPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqList {

	private SupportPagination pagination;
	private List<Faq> faqList;
	
}
