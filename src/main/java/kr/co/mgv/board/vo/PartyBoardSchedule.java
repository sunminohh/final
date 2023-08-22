package kr.co.mgv.board.vo;

import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.theater.vo.Screen;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PartyBoardSchedule {

	private int id;
	private String date;
	private String start;
	private String end;
	private int remainingSeats;
	private int seates;
	private BoardMovie movie;
	private BoardTheater theater;
	private Screen screen;
	@Builder
	public PartyBoardSchedule(int id, String date, String start, String end, int remainingSeats, int seates,
			BoardMovie movie, BoardTheater theater, Screen screen) {
		super();
		this.id = id;
		this.date = date;
		this.start = start;
		this.end = end;
		this.remainingSeats = remainingSeats;
		this.seates = seates;
		this.movie = movie;
		this.theater = theater;
		this.screen = screen;
	}
}
