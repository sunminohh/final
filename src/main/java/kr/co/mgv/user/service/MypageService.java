package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.MypageDao;
import kr.co.mgv.user.vo.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {
    private final MypageDao mypageDao;

    public List<Payment> getAllPaymentsByUserId(String userId) {

        return mypageDao.getAllPaymentByUserId(userId);
    }

}
