package com.example.seckill.service;

import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.model.Orderinfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:12
 * @Description:
 */
public interface OrderService {

    JsonBean insert(Orderinfo orderinfo);

    JsonBean createOrder(Integer productId,Integer userId);

    JsonBean createOrderRedis(Integer productId,Integer userId);
}
