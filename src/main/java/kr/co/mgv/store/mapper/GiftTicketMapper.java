package kr.co.mgv.store.mapper;

import kr.co.mgv.store.dto.GiftTicketDto;
import kr.co.mgv.store.vo.GiftTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GiftTicketMapper {

    void insertGiftTickets(Map<String, Object> params);
    GiftTicketDto checkGiftTicketByNo(long no);
    List<GiftTicket> getGiftTicketsByUserId(String userId);
    List<GiftTicket> getGiftTicketsByBookingNo(long bookingNo);
    void updateGiftTicket(Map<String,Object> params);
}
