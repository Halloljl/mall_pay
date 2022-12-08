package com.hallo.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hallo.pay.pojo.PayInfo;
import com.hallo.pay.service.PayInfoService;
import com.hallo.pay.mapper.PayInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 72460
* @description 针对表【mall_pay_info】的数据库操作Service实现
* @createDate 2022-10-09 22:51:27
*/
@Service
public class PayInfoServiceImpl extends ServiceImpl<PayInfoMapper, PayInfo>
    implements PayInfoService{

}




