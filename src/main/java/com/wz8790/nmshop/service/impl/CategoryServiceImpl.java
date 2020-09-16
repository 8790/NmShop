package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.consts.NmShopConsts;
import com.wz8790.nmshop.dao.CategoryMapper;
import com.wz8790.nmshop.pojo.Category;
import com.wz8790.nmshop.service.ICategoryService;
import com.wz8790.nmshop.vo.CategoryVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> categories() {
        List<Category> categories = categoryMapper.selectAll();

        //查出分类树的根节点
        //使用循环的方法
//        List<CategoryVo> categoryVos = new ArrayList<>();
//        for (Category category : categories) {
//            if (category.getParentId().equals(NmShopConsts.ROOT_PARENT_ID)) {
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category, categoryVo);
//                categoryVos.add(categoryVo);
//            }
//        }
//        categoryVos.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());

        //使用lambda + stream 的方式
        List<CategoryVo> categoryVos = categories.stream()
                .filter(e -> e.getParentId().equals(NmShopConsts.ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());

        //查出子分类
        findSubCategory(categoryVos, categories);

        return ResponseVo.success(categoryVos);
    }

    /**
     * 查出该分类以及下面所有的子分类的id
     * 采用递归方式
     * 定义一个新的方法用来减少数据库访问次数
     *
     * @param id        要查的id
     * @param resultSet 结果集
     */
    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
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

    /**
     * 查出子分类，使用递归的方式
     *
     * @param categoryVos 要查出的子分类,其中的数据都是父节点
     * @param categories  所有分类数据的数据集
     */
    private void findSubCategory(List<CategoryVo> categoryVos, List<Category> categories) {
        for (CategoryVo categoryVo : categoryVos) {
            List<CategoryVo> subCategoryVos = new ArrayList<>();
            for (Category category : categories) {
                //查到内容了！
                if (categoryVo.getId().equals(category.getParentId())) {
                    subCategoryVos.add(category2CategoryVo(category));
                }
            }

            subCategoryVos.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
            findSubCategory(subCategoryVos, categories);
            categoryVo.setSubCategories(subCategoryVos);

        }
    }

    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }
}
