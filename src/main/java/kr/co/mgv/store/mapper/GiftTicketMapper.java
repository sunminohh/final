package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.GiftTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface GiftTicketMapper {

    void insertGiftTickets(Map<String, Object> params);
    Character getGiftticketByNo(int no);
}
