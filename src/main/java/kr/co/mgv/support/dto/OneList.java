package kr.co.mgv.support.dto;

import java.util.List;

import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.SupportPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OneList {

	private SupportPagination pagination;
	private List<One> oneList;
	
}
