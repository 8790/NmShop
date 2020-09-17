package com.wz8790.nmshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.dao.ProductMapper;
import com.wz8790.nmshop.pojo.Product;
import com.wz8790.nmshop.service.ICategoryService;
import com.wz8790.nmshop.service.IProductService;
import com.wz8790.nmshop.vo.ProductVo;
import com.wz8790.nmshop.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ICategoryService categoryService;

    /**
     * 查出该分类下的所有商品
     * @param categoryId 类名，查出该类及子类是所有商品，如果为null则查出所有商品
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 商品列表
     */
    @Override
    public ResponseVo<PageInfo<ProductVo>> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        HashSet<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null) {
            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum, pageSize);

        List<Product> products = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVo> productVoList = products.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                })
                .collect(Collectors.toList());

        PageInfo<ProductVo> pageInfo = new PageInfo<>(productVoList);
        return ResponseVo.success(pageInfo);
    }
}
