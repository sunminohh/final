package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Package;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PackageMapper {
    void insertPackage(Package pkg);
    Package getPackageByNo(int no);

    List<Package> getPackages();
    List<Package> getPackagesByCatNo(int catNo);
}
