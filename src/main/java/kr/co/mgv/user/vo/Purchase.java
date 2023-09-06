package kr.co.mgv.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.mgv.store.vo.Package;
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
    private Date canceledDate;

    private String status;
    private int price;
    private User user;
    private Product product;
    private Package pkg;

    @Builder
    public Purchase(Date purchaseDate, Date canceledDate, String status, int price, User user, Product product, Package pkg) {
        this.purchaseDate = purchaseDate;
        this.canceledDate = canceledDate;
        this.status = status;
        this.price = price;
        this.user = user;
        this.product = product;
        this.pkg = pkg;
    }
}
