package kr.co.mgv.store.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GiftTicketMapper {

    public void insertGiftTickets(Map<String, Object> params);
}
