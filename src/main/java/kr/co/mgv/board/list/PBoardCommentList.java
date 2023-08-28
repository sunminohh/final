package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.vo.PBoardComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PBoardCommentList {

	private List<PBoardComment> parentComments;
	private List<PBoardComment> childComments;
	@Builder
	public PBoardCommentList(List<PBoardComment> parentComments, List<PBoardComment> childComments) {
		super();
		this.parentComments = parentComments;
		this.childComments = childComments;
	}
}
