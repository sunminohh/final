package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.PackageMapper;
import kr.co.mgv.store.vo.Package;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageMapper packageMapper;

    public void insertPackage(Package pkg) {

        packageMapper.insertPackage(pkg);
    }
}
