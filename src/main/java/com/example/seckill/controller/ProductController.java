package com.example.seckill.controller;

import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.model.Product;
import com.example.seckill.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/13:59
 * @Description:
 */

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     *
     * @description: 操作商品的上架下架状态
     * @param  * @param null
     * @return:  JsonBean
     * @author: HeSSD
     * @time: 2020/10/26 14:00
     */
    @PostMapping("upStatus")
    public JsonBean upStatus(Product product){

        return productService.upStatusOrSeckill(product);
    }

    /**
     *
     * @description: 操作商品的上架下架状态redis
     * @param  * @param null
     * @return:  JsonBean
     * @author: HeSSD
     * @time: 2020/10/26 14:00
     */
    @PostMapping("upStatusRedis")
    public JsonBean upStatusRedis(Product product){

        return productService.upStatusOrSeckillRedis(product);
    }

    /**
     *
     * @description: 操作是否参与秒杀
     * @param  * @param null
     * @return:  JsonBean
     * @author: HeSSD
     * @time: 2020/10/26 14:01
     */
    @PostMapping("upSeckill")
    public JsonBean upSeckill(Product product){

        return productService.upStatusOrSeckill(product);
    }

    /**
     *
     * @description: 操作是否参与秒杀redis
     * @param  * @param null
     * @return:  JsonBean
     * @author: HeSSD
     * @time: 2020/10/26 14:01
     */
    @PostMapping("upSeckillRedis")
    public JsonBean upSeckillRedis(Product product){

        return productService.upStatusOrSeckillRedis(product);
    }

    /**
     *
     * @description: 查询商品做展示
     * @param  * @param null
     * @return:  JsonBean
     * @author: HeSSD
     * @time: 2020/10/26 14:02
     */
    @RequestMapping("queryProduct")
    public JsonBean queryProduct(Integer id){
        JsonBean jsonBean = productService.queryProduct(id);
        return jsonBean;
    }
}
