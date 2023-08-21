package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.ProductMapper;
import kr.co.mgv.store.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public void insertProduct(Product product) {

        productMapper.insertProduct(product);
    }

    public Product getProductByNo(int no) {
        return productMapper.getProductByNo(no);
    }

    public List<Product> getAllProducts() {

        return productMapper.getProducts(); //productMapper.getProducts();
    }

    public List<Product> getProductByCatNo(int catNo) {
        return productMapper.getProductByCatNo(catNo);
    }
}
