package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.vo.TBoardComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TBoardCommentList {

	private List<TBoardComment> parentComments;
	private List<TBoardComment> childComments;
	@Builder
	public TBoardCommentList(List<TBoardComment> parentComments, List<TBoardComment> childComments) {
		super();
		this.parentComments = parentComments;
		this.childComments = childComments;
	}
}
