package com.hallo.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hallo.mall.pojo.OrderItem;
import com.hallo.mall.service.OrderItemService;
import com.hallo.mall.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 72460
* @description 针对表【mall_order_item】的数据库操作Service实现
* @createDate 2022-10-14 23:00:33
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




