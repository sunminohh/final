package kr.co.mgv.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.mgv.store.mapper.CategoriesMapper;
import kr.co.mgv.store.vo.Category;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoriesService {

	@Autowired
	private final CategoriesMapper categoriesMapper;
	
	public List<Category> getCategories() {
		return categoriesMapper.getAllCategories();
	}
	
	public Category getCategoryByNo(int catNo) {
		return categoriesMapper.getCategoryByNo();
	}
}
