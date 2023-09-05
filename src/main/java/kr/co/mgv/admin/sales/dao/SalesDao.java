package kr.co.mgv.admin.sales.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.admin.sales.vo.TotalSales;

@Mapper
public interface SalesDao {

	List<TotalSales> getDailyTotalSales();
	
	TotalSales getDailySalesbyDate(String date);
	TotalSales getDailyMovieSalesbyDate(String date);
	TotalSales getDailyProductSalesbyDate(String date);
	
	TotalSales getMonthlySalesbyDate(String date);
	TotalSales getMonthlyMovieSalesbyDate(String date);
	TotalSales getMonthlyProductSalesbyDate(String date);

	void insertDailySales();

	void insertDailyMovieSales();

	void insertDailyProductSales();

	void insertMonthlySales();

	void insertMonthlyMovieSales();

	void insertMonthlyProductSales();
	
	void updateDailySales(String date);
	
	void updateDailyMovieSales(String date);
	
	void updateDailyProductSales(String date);
	
	void updateMonthlySales(String date);
	
	void updateMonthlyMovieSales(String date);
	
	void updateMonthlyProductSales(String date);

}
