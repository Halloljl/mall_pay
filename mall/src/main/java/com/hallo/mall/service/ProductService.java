package com.hallo.mall.service;

import com.github.pagehelper.PageInfo;
import com.hallo.mall.pojo.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hallo.mall.vo.ProductDetailVo;
import com.hallo.mall.vo.ResponseVo;

/**
* @author 72460
* @description 针对表【mall_product】的数据库操作Service
* @createDate 2022-10-12 19:24:02
*/
public interface ProductService extends IService<Product> {


    /**
     *  查询所有
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> getList(Integer categoryId, Integer pageNum, Integer pageSize);

    /**
     * 查询详细信息
     * @param productId
     * @return
     */
    ResponseVo<ProductDetailVo> detail(Integer productId);

}
