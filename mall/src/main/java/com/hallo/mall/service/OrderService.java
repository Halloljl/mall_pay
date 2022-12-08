package com.hallo.mall.service;

import com.github.pagehelper.PageInfo;
import com.hallo.mall.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hallo.mall.vo.OrderVo;
import com.hallo.mall.vo.ResponseVo;

/**
* @author 72460
* @description 针对表【mall_order】的数据库操作Service
* @createDate 2022-10-14 23:00:33
*/
public interface OrderService extends IService<Order> {

    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId);

    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    public ResponseVo<OrderVo> details(Integer uid, Long orderNo);

    ResponseVo close(Integer uid, Long orderNo);

    void paid(Long orderNo);
}
