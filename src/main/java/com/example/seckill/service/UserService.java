package com.example.seckill.service;

import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.model.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:24
 * @Description:
 */
public interface UserService {

    JsonBean addUser(User user);

    JsonBean login(User user);
}
