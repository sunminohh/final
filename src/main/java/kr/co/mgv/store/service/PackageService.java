package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.PackageMapper;
import kr.co.mgv.store.vo.Package;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageMapper packageMapper;

    public void insertPackage(Package pkg) {

        packageMapper.insertPackage(pkg);
    }

    public Package getPackageByNo(int no) {
        return packageMapper.getPackageByNo(no);
    }

    public List<Package> getAllPackages() {
        return packageMapper.getPackages();
    }

    public List<Package> getPackagesByCatNo(int catNo) {
        return packageMapper.getPackagesByCatNo(catNo);
    }
}
