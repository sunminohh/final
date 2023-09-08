package kr.co.mgv.admin.sales.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.mgv.admin.sales.service.SalesService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalesScheduler {
	private final SalesService salesService;
	
	@Scheduled(cron="0 0 23 * * *")
	public void DailySalesScheduler() {
		salesService.insertDailySales();
	}
	
	@Scheduled(cron="0 30 23 L * ?")
	public void MonthlySalesScheduler() {
		salesService.insertMonthlySales();
	}
}
