package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.GiftTicketMapper;
import kr.co.mgv.store.mapper.OrderMapper;
import kr.co.mgv.store.mapper.ProductMapper;
import kr.co.mgv.store.vo.GiftTicket;
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

            list.add(orderProduct);
        }
        return list;

    }

    public List<GiftTicket> getGiftTicketsByUserId(String userId){return giftTicketMapper.getGiftTicketsByUserId(userId);}
	public List<Order> getOrderList() {
		// TODO Auto-generated method stub
		return orderMapper.getOrderList();
	}
}