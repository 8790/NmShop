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
import java.util.List;
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

        //使用lambda + stream 的方式
        List<CategoryVo> categoryVos = categories.stream()
                .filter(e -> e.getParentId().equals(NmShopConsts.ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .collect(Collectors.toList());


        return ResponseVo.success(categoryVos);
    }

    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }
}
