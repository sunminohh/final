package kr.co.mgv.admin.sales.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.admin.sales.vo.TotalSales;

@Mapper
public interface SalesDao {

	List<TotalSales> getDailyTotalSales();
	
	List<TotalSales> getDailySalesbyDate(Map<String, Object> param);
	List<TotalSales> getDailyMovieSalesbyDate(Map<String, Object> param);
	List<TotalSales> getDailyProductSalesbyDate(Map<String, Object> param);
	
	List<TotalSales> getMonthlySalesbyDate(Map<String, Object> param);
	List<TotalSales> getMonthlyMovieSalesbyDate(Map<String, Object> param);
	List<TotalSales> getMonthlyProductSalesbyDate(Map<String, Object> param);
	
	List<TotalSales> getYearlySalesbyDate(Map<String, Object> param);
	List<TotalSales> getYearlyMovieSalesbyDate(Map<String, Object> param);
	List<TotalSales> getYearlyProductSalesbyDate(Map<String, Object> param);

	void insertDailySales();

	void insertDailyMovieSales();

	void insertDailyProductSales();

	void insertMonthlySales();

	void insertMonthlyMovieSales();

	void insertMonthlyProductSales();
	
	void insertYearlySales();
	
	void insertYearlyMovieSales();
	
	void insertYearlyProductSales();
	
	


}
