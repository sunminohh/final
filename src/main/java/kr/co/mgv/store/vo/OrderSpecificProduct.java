package kr.co.mgv.store.vo;

import lombok.Data;

import java.util.Date;
@Data
public class OrderSpecificProduct  extends Product{
    private int amount;


    public OrderSpecificProduct(){
        super();

    }
    public OrderSpecificProduct(Product product,int no){
        super();
        this.setNo(product.getNo());
        this.amount=amount;
    }
}
