package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> getAllCategories();
}
