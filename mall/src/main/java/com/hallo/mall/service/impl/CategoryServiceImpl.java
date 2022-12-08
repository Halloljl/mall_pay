package com.hallo.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hallo.mall.consts.MallConst;
import com.hallo.mall.pojo.Category;
import com.hallo.mall.service.CategoryService;
import com.hallo.mall.mapper.CategoryMapper;
import com.hallo.mall.vo.CategoryVo;
import com.hallo.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 72460
* @description 针对表【mall_category】的数据库操作Service实现
* @createDate 2022-10-11 20:49:23
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {

        List<Category> categories = this.list();

//        for (Category category : categories) {
//            if (category.getParentId().equals(MallConst.ROOT_PARENT_ID)) {
//                CategoryVO categoryVO = new CategoryVO();
//                BeanUtils.copyProperties(category, categoryVO);
//                categoryVOList.add(categoryVO);
//            }
//        }

        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(MallConst.ROOT_PARENT_ID))
                .map(this::categoryToCategoryVO)
                .sorted(Comparator.comparing(CategoryVo:: getSortOrder).reversed())
                .collect(Collectors.toList());

        findSubCategory(categoryVoList,categories);
        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = this.list();

        findSubCategoryId(id, resultSet, categories);

    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {

        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());

                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }


    private void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categories) {

        for (CategoryVo categoryVO : categoryVoList) {
            ArrayList<CategoryVo> subCategoriesList = new ArrayList<>();

            for (Category category : categories) {
                if (categoryVO.getId().equals(category.getParentId())) {
                    CategoryVo categoryVo1 = categoryToCategoryVO(category);
                    subCategoriesList.add(categoryVo1);
                }
                subCategoriesList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
//                Collections.sort(subCategoriesList, Comparator.comparingInt(CategoryVO::getSortOrder));

                categoryVO.setSubCategories(subCategoriesList);
                findSubCategory(subCategoriesList, categories);
            }
        }
     }


    private CategoryVo categoryToCategoryVO(Category category) {
        CategoryVo categoryVO = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVO);
        return categoryVO;
    }
}




