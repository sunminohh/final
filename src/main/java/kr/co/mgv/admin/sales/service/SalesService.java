package kr.co.mgv.admin.sales.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.admin.sales.dao.SalesDao;
import kr.co.mgv.admin.sales.vo.TotalSales;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesService {
	
	private final SalesDao salesDao;
	
	public List<TotalSales> getDailyTotalSales() {

		return salesDao.getDailyTotalSales();
	}

	public void updateDailySales(String date) {
		TotalSales dailySales = salesDao.getDailySalesbyDate(date);
		if(dailySales != null) {
			
		}else {
			
		}
		
		TotalSales movieSales =salesDao.getDailyMovieSalesbyDate(date);
		if(movieSales != null) {
			
		}else {
			
		}
		
		
		
		TotalSales productSales =salesDao.getDailyProductSalesbyDate(date);
		if(productSales != null) {
			
		}else {
			
		}
	}
	
	public void insertDailySales() {
		salesDao.insertDailySales();
		salesDao.insertDailyMovieSales();
		salesDao.insertDailyProductSales();
	}

	public void insertMonthlySales() {
		salesDao.insertMonthlySales();
		salesDao.insertMonthlyMovieSales();
		salesDao.insertMonthlyProductSales();
	}

}
