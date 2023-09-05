package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.MypageDao;
import kr.co.mgv.user.vo.Purchase;
import kr.co.mgv.user.vo.UserPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {
    private final MypageDao mypageDao;

    public HashMap<String, Object> getPurchaseByUserId(String userId, String startDate, String endDate, String status, int page) {

        int totalRows = this.getTotalRowsByUserId(userId, startDate, endDate, status);
        UserPagination pagination = new UserPagination(page, totalRows);

        List<Purchase> purchases = mypageDao.getPurchases(userId, startDate, endDate, status, pagination.getBegin(), pagination.getEnd());
        log.info("begin -> {}", pagination.getBegin());
        log.info("end -> {}", pagination.getEnd());
        HashMap<String, Object> result = new HashMap<>();
        result.put("purchases", purchases);
        result.put("pagination", pagination);
        result.put("totalRows", totalRows);
        return result;
    }

    public int getTotalRowsByUserId(String userId, String startDate, String endDate, String status) {

        return mypageDao.getTotalRowsByUserId(userId, startDate, endDate, status);
    }

    public boolean cancelPurchase(int purchaseNo) {
        int updateRows = mypageDao.updatePurchaseByNo(purchaseNo);
        return updateRows > 0;
    }
}
