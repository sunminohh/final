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

}
