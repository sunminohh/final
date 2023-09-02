package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.MypageDao;
import kr.co.mgv.user.vo.Purchase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {
    private final MypageDao mypageDao;

    public List<Purchase> getPurchaseByUserId(String userId, String startDate, String endDate, String status, int page, int rows) {
        int begin = (page - 1) * rows + 1;
        int end = page * rows;

        return mypageDao.getPurchases(userId, startDate, endDate, status, begin, end);
    }

    public int getTotalRowsByUserId(String userId, String startDate, String endDate, String status) {

        return mypageDao.getTotalRowsByUserId(userId, startDate, endDate, status);
    }

}
