package kr.co.mgv.admin.sales.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.admin.sales.dto.SalesDTO;
import kr.co.mgv.admin.sales.vo.TotalSales;

@Mapper
public interface SalesDao {

	List<TotalSales> getDailyTotalSales();
	
	List<TotalSales> getDailySalesbyDate(SalesDTO dto);
	List<TotalSales> getDailyMovieSalesbyDate(SalesDTO dto);
	List<TotalSales> getDailyProductSalesbyDate(SalesDTO dto);
	List<TotalSales> getDailyMovieTotalSalesbyDate(SalesDTO dto);
	List<TotalSales> getDailyProductTotalSalesbyDate(SalesDTO dto);
	
	List<TotalSales> getMonthlySalesbyDate(SalesDTO dto);
	List<TotalSales> getMonthlyMovieSalesbyDate(SalesDTO dto);
	List<TotalSales> getMonthlyProductSalesbyDate(SalesDTO dto);
	List<TotalSales> getMonthlyMovieTotalSalesbyDate(SalesDTO dto);
	List<TotalSales> getMonthlyProductTotalSalesbyDate(SalesDTO dto);
	
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
