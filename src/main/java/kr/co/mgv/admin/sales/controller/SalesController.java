package kr.co.mgv.admin.sales.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.admin.sales.dto.SalesDTO;
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
	
	@GetMapping("/management")
	public String salesManagement() {
		return "view/admin/sales/management";
	}
	
	@PostMapping("/getSales")
	@ResponseBody
	public Map<String, Object> getSales(@RequestBody SalesDTO dto){
		Map<String, Object> map = salesService.getSales(dto);
		return map;
		
	}
	
}
