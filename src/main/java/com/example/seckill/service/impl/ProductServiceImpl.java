package com.example.seckill.service.impl;

import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.mapper.ProductMapper;
import com.example.seckill.domain.model.Product;
import com.example.seckill.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:08
 * @Description:
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public JsonBean upStatus(Product product) {
        return null;
    }

    @Override
    public JsonBean upSeckill(Product product) {
        return null;
    }

    @Override
    public JsonBean queryProduct(Integer id) {
        if (id == null) {
            return new JsonBean(-1, "商品id有误！", null);
        }
        Product product = productMapper.selectByPrimaryKey(id);
        if (product != null) {
            return new JsonBean(0, "查询成功！", product);
        } else {
            return new JsonBean(-1, "未查询到id为" + id + "的商品,请核对商号是否正确！", null);
        }
    }

    @Override
    public JsonBean upStatusOrSeckill(Product product) {
        if (product.getId() == null) {
            return new JsonBean(-1, "请选择商品！", null);
        }
        if ((product.getStatus() != null && !product.getStatus().equals(""))
                && (product.getFlag() == null || product.getFlag().equals(""))) {
            if (product.getStatus().equals("0")) {
                int i = productMapper.updateByPrimaryKeySelective(product);
                if (i > 0) {
                    return new JsonBean(0, "操作成功，商品已上架！", null);
                } else {
                    return new JsonBean(2, "操作失败！", null);
                }
            } else if (product.getStatus().equals("-1")) {
                product.setFlag("0");
                int i = productMapper.updateByPrimaryKeySelective(product);
                if (i > 0) {
                    /*redisTemplate.opsForHash().put("p_"+product.getId(),"status",product.getStatus());
                    redisTemplate.opsForHash().put("p_"+product.getId(),"flag",product.getFlag());*/
                    return new JsonBean(0, "操作成功，商品已下架！", null);
                } else {
                    return new JsonBean(2, "操作失败！", null);
                }
            } else {
                return new JsonBean(2, "操作失败！", null);
            }
        } else if ((product.getFlag() != null && !product.getFlag().equals(""))
                && (product.getStatus() == null || product.getStatus().equals(""))) {
            Product product1 = productMapper.selectByPrimaryKey(product.getId());
            /**
             * 此处商品为上架则不允许操作活动状态，需上架再进行操作
             * 也可以改为未上架也可以操作活动状态，同时将商品上架
             */
            if (product1.getStatus().equals("-1")) {
                return new JsonBean(0, "商品未上架，请上架后再参与活动！", null);
            }
            int i = productMapper.updateByPrimaryKeySelective(product);
            if (i > 0) {
                if (product.getFlag().equals("1")) {
                    return new JsonBean(0, "成功参与秒杀，随时可以开始活动！", null);
                } else if (product.getFlag().equals("2")) {
                    //如果开始秒杀活动则将商品信息放入redis
                    /*redisTemplate.opsForHash().put("p_"+product1.getId(),"id",product1.getId());
                    redisTemplate.opsForHash().put("p_"+product1.getId(),"flag",product1.getFlag());
                    redisTemplate.opsForHash().put("p_"+product1.getId(),"title",product1.getTitle());
                    redisTemplate.opsForHash().put("p_"+product1.getId(),"status",product1.getStatus());
                    redisTemplate.opsForHash().put("p_"+product1.getId(),"seckillNumber",product1.getSeckillNumber());
                    redisTemplate.opsForHash().put("p_"+product1.getId(),"seckillPrice",product1.getId());
                    System.out.println(redisTemplate.opsForHash().get("p_"+product1.getId(),"seckillNumber"));*/
                    return new JsonBean(0, "开始秒杀！", null);
                } else {
                    return new JsonBean(0, "秒杀结束，恢复原价", null);
                }
            } else {
                return new JsonBean(-1, "开启秒杀失败，请核对信息！", null);
            }
        } else {
            return new JsonBean(-1, "商品状态有误,终止操作", null);
        }
    }

    @Override
    public JsonBean upStatusOrSeckillRedis(Product product) {
        if (product.getId() == null) {
            return new JsonBean(-1, "请选择商品！", null);
        }
        if ((product.getStatus() != null && !product.getStatus().equals(""))
                && (product.getFlag() == null || product.getFlag().equals(""))) {
            if (product.getStatus().equals("0")) {
                int i = productMapper.updateByPrimaryKeySelective(product);
                if (i > 0) {
                    /**
                     * 操作成功则把更改使用redisTemplet将值存入redis中
                     */
                    redisTemplate.opsForHash().put("product_" + product.getId(), "status", product.getStatus());
                    return new JsonBean(0, "操作成功，商品已上架！", null);
                } else {
                    return new JsonBean(2, "操作失败！", null);
                }
            } else if (product.getStatus().equals("-1")) {
                product.setFlag("0");
                int i = productMapper.updateByPrimaryKeySelective(product);
                if (i > 0) {
                    redisTemplate.opsForHash().put("product_" + product.getId(), "status", product.getStatus());
                    redisTemplate.opsForHash().put("product_" + product.getId(), "flag", product.getFlag());
                    return new JsonBean(0, "操作成功，商品已下架！", null);
                } else {
                    return new JsonBean(2, "操作失败！", null);
                }
            } else {
                return new JsonBean(2, "操作失败！", null);
            }
        } else if ((product.getFlag() != null && !product.getFlag().equals(""))
                && (product.getStatus() == null || product.getStatus().equals(""))) {
            Product productStatus = productMapper.selectByPrimaryKey(product.getId());
            /**
             * 此处商品为上架则不允许操作活动状态，需上架再进行操作
             * 也可以改为未上架也可以操作活动状态，同时将商品上架
             */
            if (productStatus.getStatus().equals("-1")) {
                return new JsonBean(0, "商品未上架，请上架后再参与活动！", null);
            }
            int i = productMapper.updateByPrimaryKeySelective(product);
            if (i > 0) {
                Product product1 = productMapper.selectByPrimaryKey(product.getId());
                //如果参加秒杀活动则将商品信息放入redis
                redisTemplate.opsForHash().put("product_" + product1.getId(), "id", product1.getId());
                redisTemplate.opsForHash().put("product_" + product1.getId(), "flag", product1.getFlag());
                redisTemplate.opsForHash().put("product_" + product1.getId(), "title", product1.getTitle());
                redisTemplate.opsForHash().put("product_" + product1.getId(), "status", product1.getStatus());
                redisTemplate.opsForHash().put("product_" + product1.getId(), "seckillNumber", product1.getSeckillNumber());
                redisTemplate.opsForHash().put("product_" + product1.getId(), "number", product1.getNumber());
                redisTemplate.opsForHash().put("product_" + product1.getId(), "seckillPrice", product1.getSeckillPrice());
                if (product.getFlag().equals("1")) {
                    return new JsonBean(0, "成功参与秒杀，随时可以开始活动！", null);
                } else if (product.getFlag().equals("2")) {
                    return new JsonBean(0, "开始秒杀！", null);
                } else {
                    return new JsonBean(0, "秒杀结束，恢复原价", null);
                }
            } else {
                return new JsonBean(-1, "开启秒杀失败，请核对信息！", null);
            }
        } else {
            return new JsonBean(-1, "商品状态有误,终止操作", null);
        }
    }

    @Override
    public JsonBean upStatusRedis(Product product) {
        return null;
    }

    @Override
    public JsonBean upFlagRedis(Product product) {
        return null;
    }
}
