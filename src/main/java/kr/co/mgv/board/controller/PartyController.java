package kr.co.mgv.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.board.service.MovieBoardService;
import kr.co.mgv.board.service.PartyBoardService;
import kr.co.mgv.board.service.TheaterBoardService;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.BoardTheater;
import kr.co.mgv.movie.vo.Movie;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/party")
@RequiredArgsConstructor
public class PartyController {

		private final PartyBoardService partyBoardService;
		private final TheaterBoardService theaterBoardService;
		private final MovieBoardService movieBoardService;

		@GetMapping("/list")
	    public String theaterList(@RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
				@RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
				@RequestParam(name = "page", required = false, defaultValue = "1") int page,
				@RequestParam(name = "opt", required = false, defaultValue = "") String opt,
				@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
				@RequestParam(name = "theaterNo", required = false) Integer theaterNo,
				@RequestParam(name = "locationNo", required = false) Integer locationNo,
				Model model) {
	    	
	    	Map<String, Object> param = new HashMap<String, Object>();
			param.put("sort", sort);
			param.put("rows", rows);
			param.put("page", page);
			if (theaterNo != null) {
				param.put("theaterNo", theaterNo);
			}
			if (locationNo != null) {
				param.put("locationNo", locationNo);
			}
			if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
				param.put("opt", opt);
				param.put("keyword", keyword);
			}
	    	
			// service로 극장게시물 목록 조회하기 
			
			// model에 조회한 극장게시물 담기
			
	        return "/view/board/party/list";
	    }

	    @GetMapping("/detail")
	    public String theaterDetail() {
	        return "/view/board/party/detail";
	    }

	    
	    // 게시물 등록 관련
		@GetMapping("/theaterByLocationNo")
		@ResponseBody
		public List<BoardTheater> getTheaterbyLocationNo(@RequestParam("locationNo") int locationNo) {
			List<BoardTheater> theaters = theaterBoardService.getTheatersByLocationNo(locationNo);
			return theaters;
		}
	    
	    @GetMapping("/add")
	    public String theaterForm(Model model) {
	    	List<BoardLocation> locations = theaterBoardService.getLocations();
	    	model.addAttribute("locations", locations);
	    	
	    	List<Movie> movies = movieBoardService.getMovieTitle();
	    	model.addAttribute("movies", movies);
	        return "/view/board/party/form";
	    }
	
}
