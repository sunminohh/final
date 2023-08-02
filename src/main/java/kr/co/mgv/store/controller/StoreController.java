package kr.co.mgv.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/store")
public class StoreController {

    @GetMapping({"/", ""})
    public String home() {
        return "/view/store/home";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(defaultValue = "1") String storeNo) {
        return "/view/store/detail";
    }
    
    @GetMapping("/ticket")
    public String ticket() {
    	return "/view/store/ticket/list";
    }

    @GetMapping("/food")
    public String food() {
    	return "/view/store/food/list";
    }
    
    @GetMapping("/coupon")
    public String coupon() {
    	return "/view/store/coupon/list";
    }
    
    @GetMapping("/package")
    public String packages() {
    	return "/view/store/package/list";
    }
}
