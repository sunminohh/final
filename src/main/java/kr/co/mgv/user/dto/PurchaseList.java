package kr.co.mgv.user.dto;

import kr.co.mgv.user.vo.Purchase;
import kr.co.mgv.user.vo.UserPagination;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseList {
    private UserPagination pagination;
    private List<Purchase> purchaseList;
}
