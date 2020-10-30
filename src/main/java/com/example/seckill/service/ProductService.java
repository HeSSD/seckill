package com.example.seckill.service;

import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.model.Product;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:08
 * @Description:
 */
public interface ProductService {

    JsonBean upStatus(Product product);

    JsonBean upSeckill(Product product);

    JsonBean queryProduct(Integer id);

    JsonBean upStatusOrSeckill(Product product);

    JsonBean upStatusOrSeckillRedis(Product product);

    JsonBean upStatusRedis(Product product);

    JsonBean upFlagRedis(Product product);
}
