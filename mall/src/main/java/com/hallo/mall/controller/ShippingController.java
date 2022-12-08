package com.hallo.mall.controller;

import com.github.pagehelper.PageInfo;
import com.hallo.mall.consts.MallConst;
import com.hallo.mall.form.ShippingForm;
import com.hallo.mall.pojo.User;
import com.hallo.mall.service.ShippingService;
import com.hallo.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-10-14 22:21
 * @description
 */
@RestController
@RequestMapping("/shippings")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping()
    public ResponseVo<Map<String, Integer>> add(@Valid @RequestBody ShippingForm shippingForm, HttpSession session) {

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        Integer uid = user.getId();

        return shippingService.add(uid, shippingForm);

    }

    @DeleteMapping("/{shippingId}")
    public ResponseVo delete(@PathVariable("shippingId") Integer shippingId,
                             HttpSession session) {

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        Integer uid = user.getId();

        return shippingService.delete(uid, shippingId);
    }


    @PutMapping("/{shippingId}")
    public ResponseVo update(@PathVariable("shippingId") Integer shippingId,
                             @Valid @RequestBody ShippingForm shippingForm,
                             HttpSession session) {

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        Integer uid = user.getId();

        return shippingService.update(uid, shippingId, shippingForm);
    }


    @GetMapping("")
    public ResponseVo<PageInfo> list(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                     HttpSession session) {

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        Integer uid = user.getId();

        return shippingService.list(uid, pageNum, pageSize);

    }

















}
