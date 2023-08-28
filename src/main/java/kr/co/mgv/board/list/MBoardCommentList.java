package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.vo.MBoardComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MBoardCommentList {

	private List<MBoardComment> parentComments;
	private List<MBoardComment> childComments;
	@Builder
	public MBoardCommentList(List<MBoardComment> parentComments, List<MBoardComment> childComments) {
		super();
		this.parentComments = parentComments;
		this.childComments = childComments;
	}
}
