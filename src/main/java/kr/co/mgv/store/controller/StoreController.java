package kr.co.mgv.store.controller;

import java.util.List;
import java.util.stream.Collectors;

import kr.co.mgv.store.service.CategoryService;
import kr.co.mgv.store.service.PackageService;
import kr.co.mgv.store.service.ProductService;
import kr.co.mgv.store.vo.Package;
import kr.co.mgv.store.vo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.mgv.store.vo.Category;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/store")
@AllArgsConstructor
public class StoreController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final PackageService packageService;

    @GetMapping({"/", ""})
    public String home(Model model) {
    	List<Category> categories = categoryService.getCategories();
       List<Product> products = productService.getAllProducts();
        List<Product> packages = products.stream().filter(p->p.getNo()>99).collect(Collectors.toList());
    	model.addAttribute("categories", categories);
       model.addAttribute("products", products);
       model.addAttribute("packages", packages);
        return "view/store/home";
    }

    @GetMapping("/detail/product")
    public String productDetail(@RequestParam(defaultValue = "19") int productNo, Model model) {
        Product product = productService.getProductByNo(productNo);

        model.addAttribute("product", product);

        return "view/store/productDetail";
    }

    @GetMapping("/detail/package")
    public String packageDetail(@RequestParam(defaultValue = "1") int packageNo, Model model) {
        Package productPackage = packageService.getPackageByNo(packageNo);
        Product product= productService.getProductByName(productPackage.getName());
        model.addAttribute("package", productPackage);
        model.addAttribute("product", product);
        return "view/store/packageDetail";
    }

    @GetMapping("/list")
    public String list(@RequestParam(name = "catNo", defaultValue = "1") int catNo, Model model) {

        List<Product> productList = productService.getProductByCatNo(catNo);
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("products", productList);


        return "view/store/list";
    }

    @GetMapping("/cart")
    public String cart() {
        return "view/store/cart";
    }
}

















