package kr.co.mgv.admin.sales.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.admin.sales.dao.SalesDao;
import kr.co.mgv.admin.sales.dto.SalesDTO;
import kr.co.mgv.admin.sales.vo.TotalSales;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesService {
	
	private final SalesDao salesDao;
	
	public List<TotalSales> getDailyTotalSales() {

		return salesDao.getDailyTotalSales();
	}

	
	
	public void insertDailySales() {
		salesDao.insertDailySales();
		salesDao.insertDailyMovieSales();
		salesDao.insertDailyProductSales();
	}

	public void insertMonthlySales( ) {
		
		salesDao.insertMonthlySales();
		salesDao.insertMonthlyMovieSales();
		salesDao.insertMonthlyProductSales();
	}



	public Map<String, Object> getSales(SalesDTO dto) {
		log.info("dto->{}",dto);
		Map<String, Object> map = new HashMap<>();
		
		if("daily".equals(dto.getPeriod())){// daily monthly-> first~last까지
			List<TotalSales> totalSales= salesDao.getDailySalesbyDate(dto);
			List<TotalSales> movieTotalSales=salesDao.getDailyMovieTotalSalesbyDate(dto);
			List<TotalSales> productTotalSales=salesDao.getDailyProductTotalSalesbyDate(dto);
			List<TotalSales> movieSales=salesDao.getDailyMovieSalesbyDate(dto);
			List<TotalSales> productSales=salesDao.getDailyProductSalesbyDate(dto);
			
			map.put("totalSales", totalSales);
			map.put("movieTotalSales", movieTotalSales);
			map.put("productTotalSales", productTotalSales);
			map.put("movieSales", movieSales);
			map.put("productSales", productSales);
		}else if("monthly".equals(dto.getPeriod())) {
			List<TotalSales> totalSales= salesDao.getMonthlySalesbyDate(dto);
			List<TotalSales> movieTotalSales=salesDao.getMonthlyMovieTotalSalesbyDate(dto);
			List<TotalSales> productTotalSales=salesDao.getMonthlyProductTotalSalesbyDate(dto);
			List<TotalSales> movieSales=salesDao.getMonthlyMovieSalesbyDate(dto);
			List<TotalSales> productSales=salesDao.getMonthlyProductSalesbyDate(dto);
			
			map.put("totalSales", totalSales);
			map.put("movieTotalSales", movieTotalSales);
			map.put("productTotalSales", productTotalSales);
			map.put("movieSales", movieSales);
			map.put("productSales", productSales);
		}
		
		return map;
	}

}
