package kr.co.mgv.user.dao;

import kr.co.mgv.user.vo.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MypageDao {

    // 결제내역 조회
    List<Payment> getAllPaymentByUserId(String userId);

}
