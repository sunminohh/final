package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Package;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PackageMapper {
    void insertPackage(Package pkg);
}
