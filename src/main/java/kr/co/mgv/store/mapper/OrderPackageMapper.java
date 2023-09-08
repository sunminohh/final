package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.OrderPackage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderPackageMapper {

    void insertOrderPackage(OrderPackage orderPackage);
}
