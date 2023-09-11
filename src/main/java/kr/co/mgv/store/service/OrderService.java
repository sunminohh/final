package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.GiftTicketMapper;
import kr.co.mgv.store.mapper.OrderMapper;
import kr.co.mgv.store.mapper.ProductMapper;
import kr.co.mgv.store.vo.Order;
import kr.co.mgv.store.vo.OrderProduct;
import kr.co.mgv.store.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final GiftTicketMapper giftTicketMapper;
    private final ProductMapper productMapper;


    public void insertOrder(Order order) {
        orderMapper.insertOrder(order);
    }
    public Order getOrderById(long id){
        return orderMapper.getOrderById(id);
    }

    public void updateOrder(Order order){
        orderMapper.updateOrder(order);
    }

    public void generateGiftTickets(String userId, int quantity){
        if(quantity==0){
            return;
        }

        long giftTicketNo;

        Map<String,Object> params= new HashMap<>();
        params.put("userId",userId);
        List<Object> nos= new ArrayList<>();
        for(int i=0; i<quantity; i++){
            giftTicketNo=(long)Math.max((Math.random()*10),1);
            for(int j=0; j<15; j++){
                giftTicketNo*=10;
                giftTicketNo+=(long)Math.max((Math.random()*10),1);
            }
            nos.add(giftTicketNo);
        }
        params.put("nos",nos);
        giftTicketMapper.insertGiftTickets(params);

    }

    public List<OrderProduct> getOrderProducts(Order order) {
        List<OrderProduct> list = new ArrayList<>();
        String orderProductsString = order.getOrderProducts();
        String[] ops = orderProductsString.split("\\+");

        for (String p : ops) {
            OrderProduct orderProduct= new OrderProduct();
            String[] op = p.split(",");
            Product product = productMapper.getProductByNo(Integer.parseInt(op[0]));
            int orderAmount = Integer.parseInt(op[3]);

            orderProduct.setProductNo(product.getNo());
            orderProduct.setAmount(orderAmount);
            orderProduct.setUnitPrice(product.getOriginalPrice());
            orderProduct.setImagePath(product.getImagePath());
            orderProduct.setName(product.getName());
            orderProduct.setOrderDate(order.getCreateDate());


            Map<Product,Integer> map = new HashMap<>();
            boolean isPackage= product.getNo()>100;

//            if (isPackage){
//                String packageInfo = product.getPackageInfo();
//                String[] eachItem = packageInfo.split("\\+");
//
//                for(String each : eachItem){
//                    String[] e = each.split(",");
//                    Product innerProduct = productMapper.getProductByNo(Integer.parseInt(e[0]));
//                    map.put(innerProduct,Integer.parseInt(e[3])*orderAmount);
//                    orderProduct.setPkg(map);
//                    orderProduct.setPackage(true);
//                }
//            }else {
//                map.put(product,orderAmount);
//                orderProduct.setPackage(false);
//                orderProduct.setPkg(map);
//            }
            list.add(orderProduct);
        }
        return list;
/*
            int productNo= Integer.parseInt(op[0]);
            String name = op[1];
            orderProduct.setProductNo(productNo);
            orderProduct.setName(op[1]);
            orderProduct.setUnitPrice(Integer.parseInt(op[2]));
            orderProduct.setAmount(Integer.parseInt(op[3]));
            List<Product> productList = new ArrayList<>();
            if(productNo>100){
                Product Product = productMapper.getProductByNo(productNo);
*/


        }
//    public void insertOrderProduct(String orderId, int productNo, int productAmount, int productPrice, int catNo) {
//        OrderProduct orderProduct = new OrderProduct();
//
//        orderProduct.setOrderId(orderId);
//        orderProduct.setProductNo(productNo);
//        orderProduct.setAmount(productAmount);
//        orderProduct.setPrice(productPrice);
//        orderProduct.setCatNo(catNo);
//
//        orderProductMapper.insertOrderProduct(orderProduct);
//    }
//
//    public void insertOrderPackage(String orderId, int packageNo, int packageAmount, int packagePrice, int catNo) {
//        OrderPackage orderPackage = new OrderPackage();
//
//        orderPackage.setOrderId(orderId);
//        orderPackage.setPackageNo(packageNo);
//        orderPackage.setAmount(packageAmount);
//        orderPackage.setPrice(packagePrice);
//        orderPackage.setCatNo(catNo);
//
//        orderPackageMapper.insertOrderPackage(orderPackage);
//    }

//    public List<OrderProduct> getOrderProducts(Order order){
//        String orderProductsString = order.getOrderProducts();
//
//        List<OrderProduct> products = new ArrayList<>();
//        String[] ops = orderProductsString.split("\\+");
//
//        for (String p : ops){
//            OrderProduct orderProduct = new OrderProduct();
//
//
//            String[] op = p.split(",");
//            int productNo= Integer.parseInt(op[0]);
//            String name = op[1];
//            orderProduct.setProductNo(productNo);
//            orderProduct.setName(op[1]);
//            orderProduct.setUnitPrice(Integer.parseInt(op[2]));
//            orderProduct.setAmount(Integer.parseInt(op[3]));
//            List<Product> productList = new ArrayList<>();
//            if(productNo>100){
//                Product Product = productMapper.getProductByNo(productNo);
//
//
//            }
//
//        }
//    }


//    public List<OrderSpecificProduct> getProductListByNo(int no, int amount){
//        List<OrderSpecificProduct> productList = new ArrayList<>();
//        if(no>100){
//            Product product = productMapper.getProductByNo(no);
//            String x=product.getPackageInfo();
//
//            String[] xx= x.split("\\+");
//
//            for(String eachProduct : xx){
//
//            }
//
//
//        }else{
//            productList.add(OrderSpecificProduct(productMapper.getProductByNo(no),amount));
//        }
//        return productList;
//    }

//	public List<Order> getOrderList() {
//		// TODO Auto-generated method stub
//		return orderMapper.getOrderList();
//	}
    }