package kr.co.mgv.support.dto;

import java.util.List;

import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.SupportPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LostList {

	private SupportPagination pagination;
	private List<Lost> lostList;
	
}
