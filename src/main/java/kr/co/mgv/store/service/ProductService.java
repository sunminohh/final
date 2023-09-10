package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.PackageMapper;
import kr.co.mgv.store.mapper.ProductMapper;
import kr.co.mgv.store.vo.Category;
import kr.co.mgv.store.vo.Package;
import kr.co.mgv.store.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;
    private final PackageMapper packageMapper;

    public void insertProduct(Product product) {

        productMapper.insertProduct(product);
    }

    public Product getProductByNo(int no) {
        return productMapper.getProductByNo(no);
    }

    public Product getProductByName(String name){
        return productMapper.getProductByName(name);
    }
    public List<Product> getAllProducts() {
        return productMapper.getProducts(); //productMapper.getProducts();
    }
//    public List<Product> getNew(){
//        return productMapper.getProducts().stream().map(p->{
//            p.setPackageInfo(p.getNo()+","+p.getName()+","+p.getOriginalPrice()+",1");
//            Map<String,Object> map = new HashMap<>();
//            map.put("packageInfo",p.getPackageInfo());
//            map.put("productNo",p.getNo());
//            productMapper.packageUpdateProduct(map);
//            return p;
//        }).collect(Collectors.toList());
//    }
//    public void pac(){
//        try {
//
//            List<Package> packages = packageMapper.getPackages();
//            log.info("리스트 ", packages.toString());
//
//            for (Package pack : packages) {
//                log.info("패키지 -> {}", pack.toString());
//                Product p = new Product();
//                p.setName(pack.getName());
//                p.setImagePath(pack.getImagePath());
//                p.setStock(1000);
//                p.setOriginalPrice(pack.getOriginalPrice());
//                p.setDiscountedPrice(pack.getDiscountedPrice());
//                p.setDescription(pack.getComposition());
//                productMapper.insertProduct(p);
//            }
//        }catch(Exception e){
//            log.info("에러->{}",e.getMessage());
//        }
//    }
    public List<Product> getProductByCatNo(int catNo) {
        return productMapper.getProductByCatNo(catNo);
    }
}
