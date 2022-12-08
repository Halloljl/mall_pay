package com.hallo.mall.service;

import com.hallo.mall.form.CartAddForm;
import com.hallo.mall.form.CartUpdateForm;
import com.hallo.mall.pojo.Cart;
import com.hallo.mall.vo.CartVo;
import com.hallo.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author hallo
 * @datetime 2022-10-13 15:40
 * @description
 */
public interface CartService {

    ResponseVo<CartVo> add(Integer uid, CartAddForm cartAddForm);

    ResponseVo<CartVo> list(Integer uid);

    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm);

    ResponseVo<CartVo> delete(Integer uid, Integer productId);


    ResponseVo<CartVo> selectAll(Integer uid);

    ResponseVo<CartVo> unSelectAll(Integer uid);

    ResponseVo<Integer> sum(Integer uid);

    public List<Cart> listForCart(Integer uid);

}
