package com.example.seckill.domain.mapper;

import com.example.seckill.domain.model.Orderinfo;

public interface OrderinfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orderinfo record);

    int insertSelective(Orderinfo record);

    Orderinfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Orderinfo record);

    int updateByPrimaryKey(Orderinfo record);
}