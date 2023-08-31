package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.MypageDao;
import kr.co.mgv.user.vo.Purchase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {
    private final MypageDao mypageDao;

    /*public List<Purchase> getAllPurchaseByUserId(String userId, String startDate, String endDate, String status) {
        return mypageDao.getAllPurchaseByUserId(userId, startDate, endDate, status);
    }*/

    public List<Purchase> getPurchaseByUserId(String userId, String startDate, String endDate, String status) {
        return mypageDao.getPurchaseByUserId(userId, startDate, endDate,status);
    }

}
