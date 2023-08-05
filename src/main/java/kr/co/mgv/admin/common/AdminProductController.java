package kr.co.mgv.admin.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

	@GetMapping("/product-list")
	public String list() {
		return "/view/admin/product/management";
	}
	
	@GetMapping("/addProduct")
	public String addProduct() {
		return "/view/admin/product/addProduct";
	}
}
