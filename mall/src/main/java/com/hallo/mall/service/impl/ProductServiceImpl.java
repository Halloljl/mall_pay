package com.hallo.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hallo.mall.enums.ProductStatusEnum;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.pojo.Product;
import com.hallo.mall.mapper.ProductMapper;
import com.hallo.mall.service.CategoryService;
import com.hallo.mall.service.ProductService;
import com.hallo.mall.vo.ProductDetailVo;
import com.hallo.mall.vo.ProductVo;
import com.hallo.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 72460
* @description 针对表【mall_product】的数据库操作Service实现
* @createDate 2022-10-12 19:24:02
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductMapper productMapper;

    @Override
    public ResponseVo<PageInfo> getList(Integer categoryId, Integer pageNum, Integer pageSize) {

        HashSet<Integer> subCategoryIdSet = new HashSet<>();

        if (categoryId != null) {
            categoryService.findSubCategoryId(categoryId, subCategoryIdSet);
            subCategoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Product> getListWrapper = new QueryWrapper<>();
        getListWrapper.eq("status", 1);
        if (subCategoryIdSet.size() != 0) {
            getListWrapper.in("category_id", subCategoryIdSet);
        }

        List<Product> productList = this.list(getListWrapper);
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVO = new ProductVo();
                    BeanUtils.copyProperties(e, productVO);
                    return productVO;
                }).collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = this.getById(productId);

        if (product.getStatus().equals(ProductStatusEnum.OFF_SALE)
                || product.getStatus().equals(ProductStatusEnum.OFF_SALE) ) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        // 敏感数据
        productDetailVo.setStock(productDetailVo.getStock() > 500 ? 500: productDetailVo.getStock());

        return ResponseVo.success(productDetailVo);
    }
}




