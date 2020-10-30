package com.example.seckill.service.impl;

import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.mapper.UserMapper;
import com.example.seckill.domain.model.User;
import com.example.seckill.service.UserService;
import com.sun.org.apache.bcel.internal.generic.RET;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/14:25
 * @Description:
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * @param * @param null
     * @description: 判断用户是否存在，添加用户
     * @return:
     * @author: HeSSD
     * @time: 2020/10/26 14:26
     */
    @Override
    public JsonBean addUser(User user) {
        if ((user.getUsername() == "" || user.getUsername() == null) ||
                (user.getPassword() == "" || user.getPassword() == null)) {
            return new JsonBean(-1, "请输入完整用户信息!", null);
        }
        User getUser = userMapper.selectUserByUsername(user);
        if (getUser == null){
            int insert = userMapper.insert(user);
            if (insert > 0) {
                return new JsonBean(1, "注册成功,请前往登录!", user);
            } else {
                return new JsonBean(0, "注册失败!", user);
            }
        }else {
            return new JsonBean(2, "用户名已被使用!", user);
        }
    }

    /**
     * @param * @param null
     * @description: 判断用户名密码是否匹配，返回用户id
     * @return:
     * @author: HeSSD
     * @time: 2020/10/26 14:27
     */
    @Override
    public JsonBean login(User user) {
        if ((user.getUsername() == "" || user.getUsername() == null) ||
                (user.getPassword() == "" || user.getPassword() == null)) {
            return new JsonBean(-1, "请输入用户信息!", null);
        }

        User getUser = userMapper.selectUserByUsername(user);
        if (getUser != null) {
            if (getUser.getPassword().equals(user.getPassword())){
                return new JsonBean(0, "登录成功!", getUser);
            }else {
                return new JsonBean(1, "密码错误!", user);
            }
        }
        return new JsonBean(2, "输入信息有误,没有该用户,请前往注册!", user);
    }
}
