package com.hallo.mall.service;

import com.github.pagehelper.PageInfo;
import com.hallo.mall.form.ShippingForm;
import com.hallo.mall.pojo.Shipping;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hallo.mall.vo.ResponseVo;

import java.util.Map;

/**
* @author hallo
* @description 针对表【mall_shipping】的数据库操作Service
* @createDate 2022-10-13 23:16:58
*/
public interface ShippingService extends IService<Shipping> {


    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm shippingForm);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);


}
