package kr.co.mgv.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.mgv.store.vo.Product;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Date;


@Data
@NoArgsConstructor
@Alias("Purchase")
public class Purchase {
    private int no;

    private Date purchaseDate;
    private Date cancelDate;

    private String status;
    private int price;
    private User user;
    private Product product;

    @Builder
    public Purchase(Date purchaseDate, Date cancelDate, String status, int price, User user, Product product) {
        this.purchaseDate = purchaseDate;
        this.cancelDate = cancelDate;
        this.status = status;
        this.price = price;
        this.user = user;
        this.product = product;
    }
}
