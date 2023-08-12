package kr.co.mgv.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.store.vo.Category;

@Mapper
public interface CategoriesMapper {

	/* 모든 상품 카테고리 정보 */
	List<Category> getAllCategories();
	Category getCategoryByNo();
	
}
