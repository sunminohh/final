package kr.co.mgv.admin.sales.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.admin.sales.vo.TotalSales;

@Mapper
public interface SalesDao {

	List<TotalSales> getDailyTotalSales();

}
