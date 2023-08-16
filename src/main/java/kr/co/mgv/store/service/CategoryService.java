package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.CategoryMapper;
import kr.co.mgv.store.vo.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<Category> getCategories() {

        return categoryMapper.getAllCategories();
    }
}
