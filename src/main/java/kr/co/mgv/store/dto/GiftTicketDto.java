package kr.co.mgv.store.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("GiftTicketDto")
public class GiftTicketDto {
    private String isUsed;
    private Boolean isExpired;
}
