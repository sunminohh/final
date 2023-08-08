package kr.co.mgv.support.faq;

import java.util.List;

import kr.co.mgv.support.SupportPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqList {

	private SupportPagination pagination;
	private List<Faq> faqList;
	
}
