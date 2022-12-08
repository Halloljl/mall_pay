package com.hallo.mall.controller;

import com.github.pagehelper.PageInfo;
import com.hallo.mall.consts.MallConst;
import com.hallo.mall.form.OrderCreateForm;
import com.hallo.mall.pojo.Shipping;
import com.hallo.mall.pojo.User;
import com.hallo.mall.service.OrderService;
import com.hallo.mall.vo.OrderVo;
import com.hallo.mall.vo.ResponseVo;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author hallo
 * @datetime 2022-10-15 22:43
 * @description
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping()
    public ResponseVo<OrderVo> create(HttpSession session,@Valid @RequestParam OrderCreateForm form) {

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.create(user.getId(), form.getShippingId());
    }


    @GetMapping()
    public ResponseVo<PageInfo> list(HttpSession session,
                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(required = false, defaultValue = "1") Integer pageNum) {

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.list(user.getId(), pageSize, pageNum);
    }

    @GetMapping("/{orderNo}")
    public ResponseVo<OrderVo> details(@PathVariable Long orderNo,HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.details(user.getId(), orderNo);
    }


    @PutMapping("/{orderNo}")
    public ResponseVo<OrderVo> cancel(@PathVariable Long orderNo,HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.close(user.getId(), orderNo);
    }



}
