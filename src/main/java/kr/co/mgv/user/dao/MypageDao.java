package kr.co.mgv.user.dao;

import kr.co.mgv.user.vo.Purchase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface MypageDao {

    // 결제내역 조회
    List<Purchase> getAllPurchaseByUserId(@Param("userId") String userId,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("status") String status);
    List<Purchase> getPurchaseByUserId(@Param("userId") String userId,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("status") String status);
    List<Purchase> getCancelByUserId(@Param("userId") String userId,
                                     @Param("startDate") String startDate,
                                     @Param("endDate") String endDate,
                                     @Param("status") String status);

}
