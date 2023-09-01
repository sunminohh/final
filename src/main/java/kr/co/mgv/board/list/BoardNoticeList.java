package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.vo.BoardNotice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardNoticeList {

	private int totalNotice;
	private List<BoardNotice> notices;
	@Builder
	public BoardNoticeList(int totalNotice, List<BoardNotice> notices) {
		super();
		this.totalNotice = totalNotice;
		this.notices = notices;
	}
}
