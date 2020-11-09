package com.example.seckill.controller;

import com.example.seckill.common.FastJsonUtils;
import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.model.Orderinfo;
import com.example.seckill.domain.model.User;
import com.example.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:04
 * @Description:
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     *
     * @description: 创建订单
     * @param  * @param null
     * @return:
     * @author: HeSSD
     * @time: 2020/10/26 14:05
     */
    @RequestMapping("createOrder")
    public JsonBean createOrder(HttpServletRequest request,Integer productId,Integer userId){
        /*User userInfo = (User)request.getSession().getAttribute("userInfo");
        if (userInfo == null){
            return new JsonBean(-1,"未登录，请登录后下单！",null);
        }*/
        if (userId == null){
            return new JsonBean(-1,"未登录，请登录后下单！",null);
        }
        return orderService.createOrder(productId,userId);
    }

    /**
     *
     * @description: 创建订单
     * @param  * @param null
     * @return:
     * @author: HeSSD
     * @time: 2020/10/26 14:05
     */
    @RequestMapping("createOrderRedis")
    public JsonBean createOrderRedis(HttpServletRequest request,Integer productId,Integer userId){
        /*User userInfo = (User)request.getSession().getAttribute("userInfo");
        if (userInfo == null){
            return new JsonBean(-1,"未登录，请登录后下单！",null);
        }*/
        if (userId == null){
            return new JsonBean(-1,"未登录，请登录后下单！",null);
        }
        return orderService.createOrderRedis(productId,userId);
    }

    /**
     *
     * @description: 通过RabbitMQ分发消息后入库
     * @param  * @param null
     * @return:
     * @author: HeSSD
     * @time: 2020/11/6 13:54
     */
    @RequestMapping("orderWarehousing")
    public JsonBean orderWarehousing(@RequestBody Orderinfo orderinfo){

        System.out.println(FastJsonUtils.convertObjectToJSON(orderinfo));
        return orderService.insert(orderinfo);
    }

}
