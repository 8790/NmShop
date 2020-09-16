package com.wz8790.nmshop.service;

import com.wz8790.nmshop.vo.CategoryVo;
import com.wz8790.nmshop.vo.ResponseVo;

import java.util.List;
import java.util.Set;

public interface ICategoryService {

    /**
     * 查出所有的分类，构建树形结构
     */
    ResponseVo<List<CategoryVo>> categories();

    /**
     * 查出该分类以及下面所有的子分类的id
     * @param id 要查的id
     * @param resultSet 结果集
     */
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
