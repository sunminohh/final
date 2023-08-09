package kr.co.mgv.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.mgv.store.service.CategoriesService;
import kr.co.mgv.store.vo.Category;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/store")
@AllArgsConstructor
public class StoreController {

	@Autowired
	private final CategoriesService categoriesService;
	
    @GetMapping({"/", ""})
    public String home(Model model) {
    	List<Category> categories = categoriesService.getCategories();
    	model.addAttribute("categories", categories);
        return "/view/store/home";
    }

    // 상품 카테고리가 아닌 상품 테이블에 외래키로 되어있는 카테고리를 사용해야함
    @GetMapping("/list")
    public String list(@RequestParam("catNo") int catNo, Model model) {
    	model.addAttribute("category", categoriesService.getCategoryByNo(catNo));
    	return "/view/store/list";
    }
    
    @GetMapping("/detail")
    public String detail(@RequestParam(defaultValue = "1") String storeNo) {
        return "/view/store/detail";
    }
    
}
