package com.hallo.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.form.ShippingForm;
import com.hallo.mall.pojo.Shipping;
import com.hallo.mall.service.ShippingService;
import com.hallo.mall.mapper.ShippingMapper;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 72460
* @description 针对表【mall_shipping】的数据库操作Service实现
* @createDate 2022-10-13 23:16:58
*/
@Service
@Slf4j
public class ShippingServiceImpl extends ServiceImpl<ShippingMapper, Shipping>
    implements ShippingService{

    @Resource
    private ShippingMapper shippingMapper;

    @Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm shippingForm) {

        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm, shipping);
        shipping.setUserId(uid);

        int i = shippingMapper.insert(shipping);
        log.info("shippingMapper.insert ===> i = {}", i);

        if (i < 1) {
            // 插入数据库失败
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        HashMap<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());

        log.info("shippingId = {}", shipping.getId());
        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {

        QueryWrapper<Shipping> shippingQueryWrapper = new QueryWrapper<>();
        shippingQueryWrapper.eq("user_id", uid)
                        .eq("id", shippingId);

        int i = shippingMapper.delete(shippingQueryWrapper);

        if (i < 1) {
            return ResponseVo.error(ResponseEnum.DELETE_SHIPPING_FAIL);
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm) {

        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        QueryWrapper<Shipping> shippingQueryWrapper = new QueryWrapper<>();
        shippingQueryWrapper.eq("user_id", uid)
                .eq("id", shippingId);

        int update = shippingMapper.update(shipping, shippingQueryWrapper);
        if (update < 1) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {

        if (uid == null) {
            return ResponseVo.error(ResponseEnum.PARAM_ERROR);
        }

        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<Shipping> shippingQueryWrapper = new QueryWrapper<>();
        shippingQueryWrapper.eq("user_id", uid);

        List<Shipping> shippingList = shippingMapper.selectList(shippingQueryWrapper);

        PageInfo<Shipping> shippingPageInfo = new PageInfo<>(shippingList);
//        shippingPageInfo.setList(shippingList);

        return ResponseVo.success(shippingPageInfo);
    }
}




