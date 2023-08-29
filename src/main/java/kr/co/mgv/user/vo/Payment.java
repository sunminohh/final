package kr.co.mgv.user.vo;

import kr.co.mgv.store.vo.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Alias("Payment")
public class Payment {
    private int no;
    private Date purchaseDate;
    private Date cancelDate;
    private String status;
    private int price;
    private User user;
    private Product product;

    public Payment(Date purchaseDate, Date cancelDate, String status, int price, User user, Product product) {
        this.purchaseDate = purchaseDate;
        this.cancelDate = cancelDate;
        this.status = status;
        this.price = price;
        this.user = user;
        this.product = product;
    }
}
