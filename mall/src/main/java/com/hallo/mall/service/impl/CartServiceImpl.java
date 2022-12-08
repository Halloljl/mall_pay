package com.hallo.mall.service.impl;

import com.google.gson.Gson;
import com.hallo.mall.enums.ProductStatusEnum;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.form.CartAddForm;
import com.hallo.mall.form.CartUpdateForm;
import com.hallo.mall.mapper.ProductMapper;
import com.hallo.mall.pojo.Cart;
import com.hallo.mall.pojo.Product;
import com.hallo.mall.service.CartService;
import com.hallo.mall.vo.CartProductVo;
import com.hallo.mall.vo.CartVo;
import com.hallo.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-10-13 15:41
 * @description
 */
@Service
public class CartServiceImpl implements CartService {

    private final static String CART_REDIS_KEYS_TEMPLATE = "cart_%d";
    Integer quantity = 1;
    private Gson gson = new Gson();
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private ProductMapper productMapper;

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm cartAddForm) {
        Product product = productMapper.selectById(cartAddForm.getProductId());

        // 商品是否存在
        if (product == null) {
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        // 商品是否正常在售
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        // 商品是否正常在售
        if (product.getStock() <= 0 ) {
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROE);
        }

        // 写入redis
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEYS_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));

        Cart cart;

        if (StringUtils.isEmpty(value)) {
            // 没有该商品 新增
            cart = new Cart(product.getId(), quantity, cartAddForm.getSelected());
        } else {
            // 已经有了 数量加1
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        opsForHash.put(String.format(CART_REDIS_KEYS_TEMPLATE,uid)
                , String.valueOf(product.getId())
                , gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {

        CartVo cartVo = new CartVo();

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEYS_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        ArrayList<CartProductVo> cartProductVoList = new ArrayList<>();

        Boolean selectedAll = true;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        Integer cartTotalQuantity = 0;

        for (Map.Entry<String, String> entry : entries.entrySet()) {

            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            // TODO 优化
            Product product = productMapper.selectById(productId);

            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(
                        productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );

                if (!cart.getProductSelected()) {
                    selectedAll = false;
                }
                // 计算总价 只计算选中的
                if (cart.getProductSelected()) {
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
                cartProductVoList.add(cartProductVo);
            }
            cartTotalQuantity += cart.getQuantity();
        }

        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setSelectedAll(selectedAll);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);


        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm) {

        CartVo cartVo = new CartVo();

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEYS_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));

        // 先判空
        if (StringUtils.isEmpty(value)) {
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        Cart cart = gson.fromJson(value, Cart.class);
        if (cartUpdateForm.getQuantity() != null
                && cartUpdateForm.getQuantity() >= 0) {
            cart.setQuantity(cartUpdateForm.getQuantity());
        }
        if (cartUpdateForm.getSelected() != null) {
            cart.setProductSelected(cartUpdateForm.getSelected());
        }

        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEYS_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));

        // 先判空
        if (StringUtils.isEmpty(value)) {
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        opsForHash.delete(redisKey, String.valueOf(productId));


        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEYS_TEMPLATE, uid);

        List<Cart> carts = listForCart(uid);

        for (Cart cart : carts) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey, String.valueOf(uid), gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {


        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEYS_TEMPLATE, uid);

        List<Cart> carts = listForCart(uid);

        for (Cart cart : carts) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey, String.valueOf(uid), gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);

        return ResponseVo.success(sum);
    }


    @Override
    public List<Cart> listForCart(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEYS_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        ArrayList<Cart> cartArrayList = new ArrayList<>();

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            cartArrayList.add(gson.fromJson(entry.getValue(), Cart.class));
        }

        return cartArrayList;
    }
}
