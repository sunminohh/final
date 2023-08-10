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

import kr.co.mgv.board.list.StoreBoardList;
import kr.co.mgv.board.service.StoreBoardService;
import kr.co.mgv.board.vo.BoardProduct;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/store")
@RequiredArgsConstructor
public class StoreBoardController {

	private final StoreBoardService storeBoardService;
	
    @GetMapping("/list")
    public String storeList(@RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
			@RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "opt", required = false, defaultValue = "") String opt,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(name = "productNo", required = false) Integer productNo,
			@RequestParam(name = "catNo", required = false) Integer catNo,
			Model model) {
    	
    	Map<String , Object> param = new HashMap<String, Object>();
    	param.put("sort", sort);
    	param.put("rows", rows);
    	param.put("page", page);
		if (productNo != null) {
			param.put("productNo", productNo);
		}
		if (catNo != null) {
			param.put("catNo", catNo);
		}
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
    	
		// service로 스토어게시물 목록, 카테고리/상품 목록, 페이지네이션 조회하기
		StoreBoardList result = storeBoardService.getSBoards(param);
		
		// model에 조회한 리스트 담기
		model.addAttribute("result", result);
		
        return "/view/board/store/list";
    }

    @GetMapping("/detail")
    public String storeDetail() {
        return "/view/board/store/detail";
    }

    @GetMapping("/add")
    public String storeForm() {
        return "/view/board/store/form";
    }
    
    @GetMapping("/productByCatNo")
    @ResponseBody
    public List<BoardProduct> getProductByCatNo(@RequestParam("catNo") int catNo) {
    	List<BoardProduct> products = storeBoardService.getProductsByCatNo(catNo);
    	return products;
    }

}
