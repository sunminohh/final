package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.vo.MBoardComment;
import kr.co.mgv.board.vo.SBoardComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SBoardCommentList {

	private List<SBoardComment> parentComments;
	private List<SBoardComment> childComments;
	@Builder
	public SBoardCommentList(List<SBoardComment> parentComments, List<SBoardComment> childComments) {
		super();
		this.parentComments = parentComments;
		this.childComments = childComments;
	}
}
