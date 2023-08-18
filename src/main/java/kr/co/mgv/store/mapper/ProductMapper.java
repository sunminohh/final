package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    void insertProduct(Product product);
    List<Product> getProducts();

    Product getProductByNo(int no);
}
