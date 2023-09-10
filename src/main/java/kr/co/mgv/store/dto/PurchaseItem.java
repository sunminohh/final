package kr.co.mgv.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseItem {
    private int productNo;
    private String productName;
    private int originalPrice;
    private int quantity;
}
