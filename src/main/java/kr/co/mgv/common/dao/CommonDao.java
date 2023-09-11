package kr.co.mgv.common.dao;

import kr.co.mgv.common.vo.MgvFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonDao {
    String selectTopMovieImageUrl();
    void insertMgvFile(MgvFile file);
    MgvFile getMgvFile(long fileId);
}
