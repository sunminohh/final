package kr.co.mgv.admin.sales.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.admin.sales.service.SalesService;
import kr.co.mgv.admin.sales.vo.TotalSales;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/sales")
@RequiredArgsConstructor
public class SalesController {

	private final SalesService salesService;
	
	@GetMapping("/dailyTotalSales")
	@ResponseBody
	public List<TotalSales> getDailytTotalSales(){
		return salesService.getDailyTotalSales();
	}
}
