package kr.co.mgv.support.dto;

import java.util.List;

import kr.co.mgv.support.vo.Notice;
import kr.co.mgv.support.vo.SupportPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeList {

	private SupportPagination pagination;
	private List<Notice> noticeList;
}
