package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.vo.PartyJoin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinList {

	private List<PartyJoin> acceptedJoins;
	private List<PartyJoin> notAcceptedJoins;
	private int acceptCount;
	
	@Builder
	public JoinList(List<PartyJoin> acceptedJoins, List<PartyJoin> notAcceptedJoins, int acceptCount) {
		super();
		this.acceptedJoins = acceptedJoins;
		this.notAcceptedJoins = notAcceptedJoins;
		this.acceptCount = acceptCount;
	}
}
