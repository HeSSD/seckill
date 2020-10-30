package com.example.seckill.service;

import com.example.seckill.common.JsonBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:12
 * @Description:
 */
public interface OrderService {

    JsonBean createOrder(Integer productId,Integer userId);

    JsonBean createOrderRedis(Integer productId,Integer userId);
}
