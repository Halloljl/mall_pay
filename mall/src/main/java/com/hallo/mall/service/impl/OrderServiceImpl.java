package com.hallo.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hallo.mall.enums.OrderStatusEnum;
import com.hallo.mall.enums.PaymentEnum;
import com.hallo.mall.enums.ProductStatusEnum;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.mapper.OrderItemMapper;
import com.hallo.mall.mapper.ProductMapper;
import com.hallo.mall.mapper.ShippingMapper;
import com.hallo.mall.pojo.*;
import com.hallo.mall.service.CartService;
import com.hallo.mall.service.OrderItemService;
import com.hallo.mall.service.OrderService;
import com.hallo.mall.mapper.OrderMapper;
import com.hallo.mall.vo.OrderItemVo;
import com.hallo.mall.vo.OrderVo;
import com.hallo.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 72460
* @description 针对表【mall_order】的数据库操作Service实现
* @createDate 2022-10-14 23:00:33
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Resource
    private ShippingMapper shippingMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private CartService cartService;
    @Resource
    private OrderItemService orderItemService;


    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {

        // 收货地址校验（总之要查出来的）
        QueryWrapper<Shipping> shippingQueryWrapper = new QueryWrapper<>();
        shippingQueryWrapper.eq("id", shippingId)
                .eq("user_id", uid);

        Shipping shipping = shippingMapper.selectOne(shippingQueryWrapper);

        if (shipping == null) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        // 获取购物车，校验（是否有商品，库存）

        // 找到已选中的商品
        List<Cart> cartList = cartService.listForCart(uid)
                .stream()
                .filter(Cart :: getProductSelected)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECT_IS_EMPTY);
        }

        // 获取 products
        Set<Integer> productIdSet = cartList.stream().map(Cart::getProductId).collect(Collectors.toSet());

        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.in("id", productIdSet);

        Map<Integer, Product> productMap = productMapper.selectList(productQueryWrapper).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));


        Long orderNo = generateOrderNo();
        ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();

        for (Cart cart : cartList) {
            //（是否有商品，库存）
            Product product = productMap.get(cart.getProductId());

            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,
                        "商品不存在， productId = " + cart.getProductId());
            }
            // 商品上下架状态
            if(product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode())
                    || product.getStatus().equals(ProductStatusEnum.DELETE.getCode())) {
                return  ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, "商品下架或删除");
            }

            // 库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROE,
                        "库存不正确." + product.getName());
            }

            // 生成orderItem
            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemArrayList.add(orderItem);

            // 减库存
            product.setStock(product.getStock() - cart.getQuantity());

//            UpdateWrapper<Product> productUpdateWrapper = new UpdateWrapper<>();
            int insert = productMapper.updateById(product);
            if (insert < 0) {
                ResponseVo.error(ResponseEnum.ERROR);
            }
        }

        // 生成订单，入库： order 和 order_item 事务
        // 计算总价
        Order order = buildOrder(uid, orderNo, shippingId, orderItemArrayList);
        int insert = orderMapper.insert(order);
        if (insert <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        boolean saveBatch = orderItemService.saveBatch(orderItemArrayList);
        if (!saveBatch) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        // 更新购物车（选中的商品）
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }

        // 构造 orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemArrayList, shipping);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        // 查出 user 所属的所有订单
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", uid);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        // 获取订单列表中的orderNo，备用根据查询 orderNo 查询 orderItem 所属的订单项列表
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());

        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());

        // 查询orderItem的列表

        QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
        orderItemQueryWrapper.in("order_no", orderNoSet);
        List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);

        // 查地址
        QueryWrapper<Shipping> shippingQueryWrapper = new QueryWrapper<>();
        shippingQueryWrapper.in("id", shippingIdSet);
        List<Shipping> shippingList = shippingMapper.selectList(shippingQueryWrapper);

        // 构建orderVo

        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        ArrayList<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order, orderItemMap.get(order.getOrderNo()), shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo orderPageInfo = new PageInfo<>(orderList);
        orderPageInfo.setList(orderVoList);

        return ResponseVo.success(orderPageInfo);
    }

    @Override
    public ResponseVo<OrderVo> details(Integer uid, Long orderNo) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(orderQueryWrapper);

        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIT);
        }

        QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
        orderItemQueryWrapper.eq("order_no", order.getOrderNo());

        List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);

        QueryWrapper<Shipping> shippingQueryWrapper = new QueryWrapper<>();
        shippingQueryWrapper.eq("id", order.getShippingId());
        Shipping shipping = shippingMapper.selectOne(shippingQueryWrapper);
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo close(Integer uid, Long orderNo) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(orderQueryWrapper);

        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIT);
        }
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());

        int i = orderMapper.updateById(order);
        if (i < 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(orderQueryWrapper);
        if (order == null) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单id:" + orderNo);
        }
        //只有[未付款]订单可以变成[已付款]，看自己公司业务
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc() + "订单id:" + orderNo);
        }

        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int i = orderMapper.updateById(order);
        if (i <= 0) {
            throw new RuntimeException("将订单更新为已支付状态失败，订单id:" + orderNo);
        }
    }

    public OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {

        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream()
                .map(e -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    BeanUtils.copyProperties(e, orderItemVo);
                    return orderItemVo;
                })
                .collect(Collectors.toList());

        orderVo.setOrderItemVoList(orderItemVoList);

        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }
        return orderVo;
    }
    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId, List<OrderItem> orderItemList) {

        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }


    /**
     * 企业级：分布式唯一id/主键
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }


    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}




