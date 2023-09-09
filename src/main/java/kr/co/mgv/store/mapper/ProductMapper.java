package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {

    void insertProduct(Product product);
    List<Product> getProducts();

    Product getProductByNo(int no);

    Product getProductByName(String name);
    List<Product> getProductByCatNo(int catNo);
    void packageUpdateProduct(Map<String, Object> params);
}
