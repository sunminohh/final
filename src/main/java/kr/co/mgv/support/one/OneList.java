package kr.co.mgv.support.one;

import java.util.List;

import kr.co.mgv.support.SupportPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OneList {

	private SupportPagination pagination;
	private List<One> oneList;
	
}
