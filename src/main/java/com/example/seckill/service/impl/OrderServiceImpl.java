package com.example.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.seckill.common.FastJsonUtils;
import com.example.seckill.common.HttpUtil;
import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.mapper.OrderinfoMapper;
import com.example.seckill.domain.mapper.ProductMapper;
import com.example.seckill.domain.model.Orderinfo;
import com.example.seckill.domain.model.Product;
import com.example.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:12
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${application.rabbitMQ}")
    private String url;
    @Autowired
    private OrderinfoMapper orderinfoMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public JsonBean insert(Orderinfo orderinfo) {
        int i = orderinfoMapper.insertSelective(orderinfo);
        if (i > 0){
            return new JsonBean(0,"下单成功",null);
        }else {
            return new JsonBean(-1,"下单失败",null);
        }
    }

    /**
     * @param * @param productId
     * @description: 创建订单业务逻辑
     * @return:
     * @author: HeSSD
     * @time: 2020/10/26 14:14
     */
    @Override
    public JsonBean createOrder(Integer productId, Integer userId) {
        if (productId == null) {
            return new JsonBean(-1, "参数productId错误,请检查！", null);
        }

        //下列代码需要做事务处理
        Product product = productMapper.selectByPrimaryKey(productId);
        Product product1 = new Product();
        Orderinfo orderinfo = new Orderinfo();
        //判断商品上下架信息
        if (product.getStatus().equals("-1")) {
            return new JsonBean(2, "商品已下架！", null);
        }

        //判断商品是否还有库存
        if (!(product.getNumber() > 0)) {
            return new JsonBean(1, "已经卖完了，去看看其他商品吧！", null);
        }
        if (product.getSeckillNumber() > 0) {
            String productNo = UUID.randomUUID().toString().substring(0, 8) +
                    System.currentTimeMillis();
            /**
             *还有库存并且是处于活动之中
             */
            product1.setId(product.getId());
            if (product.getFlag().equals("2")) {
                product1.setNumber(product.getNumber() - 1);
                product1.setSeckillNumber(product.getSeckillNumber() - 1);
                int i = productMapper.updateByPrimaryKeySelective(product1);
                if (i > 0) {
                    orderinfo.setOrderno(productNo);
                    orderinfo.setProductid(productId);
                    orderinfo.setUserid(userId);
                    orderinfo.setStatus("0");
                    orderinfo.setPrice(product.getSeckillPrice());
                    String orderInfoJson = FastJsonUtils.convertObjectToJSON(orderinfo);
                    // 发送请求
                    String returnData = HttpUtil.doPost2(url, JSON.parseObject(orderInfoJson));
                    JsonBean jsonBean = (JsonBean) FastJsonUtils.convertJsonToObject(returnData, JsonBean.class);
                    if (jsonBean.getCode() == 0){
                        return new JsonBean(0, "恭喜抢到商品，请于下单成功五分钟内支付！", orderinfo);
                    } else {
                        return new JsonBean(-1, "抢购失败！", null);
                    }
                    /*int i1 = orderinfoMapper.insertSelective(orderinfo);
                    if (i1 > 0) {
                        return new JsonBean(0, "恭喜抢到商品，请于下单成功五分钟内支付！", orderinfo);
                    } else {
                        return new JsonBean(-1, "抢购失败！", null);
                    }*/
                } else {
                    return new JsonBean(-1, "操作失败！", null);
                }

            } else if (product.getFlag().equals("0")) {
                product1.setNumber(product.getNumber() - 1);
                int i = productMapper.updateByPrimaryKeySelective(product1);
                if (i > 0) {
                    orderinfo.setOrderno(productNo);
                    orderinfo.setProductid(productId);
                    orderinfo.setUserid(userId);
                    orderinfo.setStatus("0");
                    orderinfo.setPrice(Math.toIntExact(Long.valueOf(product.getPrice())));
                    int i1 = orderinfoMapper.insertSelective(orderinfo);
                    if (i1 > 0) {
                        return new JsonBean(0, "下单成功！", orderinfo);
                    } else {
                        return new JsonBean(-1, "下单失败！", null);
                    }
                } else {
                    return new JsonBean(-1, "操作失败！", null);
                }
            } else {
                return new JsonBean(3, "活动未开始，请等待！", null);
            }
        } else {
            return new JsonBean(1, "您来晚了，促销商品已经卖完了", null);
        }
    }

    @Override
    public JsonBean createOrderRedis(Integer productId, Integer userId) {
        if (productId == null) {
            return new JsonBean(-1, "参数productId错误,请检查！", null);
        }

        String status = (String) redisTemplate.opsForHash().get("product_" + productId, "status");
        //判断商品上下架信息
        if (status.equals("-1")) {
            return new JsonBean(2, "商品已下架！", null);
        }

        Integer number = (Integer) redisTemplate.opsForHash().get("product_" + productId, "number");

        //下列代码需要做事务处理
        Product product = productMapper.selectByPrimaryKey(productId);
        Product product1 = new Product();
        Orderinfo orderinfo = new Orderinfo();

        //判断商品是否还有库存
        if (!(number > 0)) {
            return new JsonBean(1, "已经卖完了，去看看其他商品吧！", null);
        }
        //生成订单号
        String productNo = UUID.randomUUID().toString().substring(0, 8) +
                System.currentTimeMillis();
        /**
         *还有库存并且是处于活动之中
         */
        //判断是否还有秒杀库存
        String flag = (String) redisTemplate.opsForHash().get("product_" + productId, "flag");
        product1.setId(product.getId());
        if (flag.equals("2")) {
            //Integer seckillNumber = (Integer) redisTemplate.opsForHash().get("product_" + productId, "seckillNumber");
            Long seckillNumber = redisTemplate.opsForHash().increment("product_" + productId, "seckillNumber", -1);
            redisTemplate.opsForHash().increment("product_" + productId, "number", -1);
            if (seckillNumber > -1) {
                /**
                 * 去redis对商品数量做减操作
                 */
                orderinfo.setOrderno(productNo);
                orderinfo.setProductid(productId);
                orderinfo.setUserid(userId);
                orderinfo.setStatus("0");
                Integer seckillPrice = (Integer) redisTemplate.opsForHash().get("product_" + productId, "seckillPrice");
                orderinfo.setPrice(seckillPrice);
                int i1 = orderinfoMapper.insertSelective(orderinfo);
                if (i1 > 0) {
                    return new JsonBean(0, "恭喜抢到商品，请于下单成功五分钟内支付！", orderinfo);
                } else {
                    redisTemplate.opsForHash().increment("product_" + productId, "number", 1);
                    redisTemplate.opsForHash().increment("product_" + productId, "seckillNumber", 1);
                    return new JsonBean(-1, "抢购失败！", null);
                }
            } else {
                redisTemplate.opsForHash().increment("product_" + productId, "seckillNumber", 1);
                redisTemplate.opsForHash().increment("product_" + productId, "number", 1);
                return new JsonBean(1, "您来晚了，促销商品已经卖完了", null);
            }

        } else if (flag.equals("0")) {
            redisTemplate.opsForHash().increment("product_" + productId, "number", -1);
            orderinfo.setOrderno(productNo);
            orderinfo.setProductid(productId);
            orderinfo.setUserid(userId);
            orderinfo.setStatus("0");
            orderinfo.setPrice(Math.toIntExact(Long.valueOf(product.getPrice())));
            int i1 = orderinfoMapper.insertSelective(orderinfo);
            if (i1 > 0) {
                return new JsonBean(0, "下单成功！", orderinfo);
            } else {
                redisTemplate.opsForHash().increment("product_" + productId, "number", 1);
                return new JsonBean(-1, "下单失败！", null);
            }
        } else {
            return new JsonBean(3, "活动未开始，请等待！", null);
        }
    }
}
