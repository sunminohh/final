package kr.co.mgv.admin.common;

import kr.co.mgv.store.service.CategoryService;
import kr.co.mgv.store.service.ProductService;
import kr.co.mgv.store.vo.Category;
import kr.co.mgv.store.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/product")
@Slf4j
@RequiredArgsConstructor
public class AdminProductController {

	private final ProductService productService;
	private final CategoryService categoryService;

	@GetMapping("/package-list")
	public String packageList() {
		return "view/admin/product/package-list";
	}

	@GetMapping("/product-list")
	public String productList(Model model) {
		List<Product> products = productService.getAllProducts();
		model.addAttribute("products", products);

		return "view/admin/product/product-list";
	}

	@GetMapping("/add-form")
	public String form(Model model) {
		List<Category> categories = categoryService.getCategories();
		model.addAttribute("categories", categories);
		return "/view/admin/product/addProduct";
	}
	@Value("${upload.directory}")
	private String uploadDirectory;

	@PostMapping("/add-form")
	public String add(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile) {

		if (!imageFile.isEmpty()) {
			try {
				String filePath = uploadDirectory + UUID.randomUUID() + imageFile.getOriginalFilename();
				imageFile.transferTo(new File(filePath));
				product.setImagePath(filePath);
			} catch (IOException e) {
				return "/view/admin/product/addProduct";
			}
		}

		productService.insertProduct(product);
		log.info("상품정보 ->{}", product);
		return "product-list";
	}
}
