package com.example.seckill.controller;

import com.example.seckill.common.JsonBean;
import com.example.seckill.domain.model.User;
import com.example.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: HeSSD
 * @Date: 2020/10/26/13:52
 * @Description:
 */

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @param * @param null
     * @description: 注册
     * @return: JsonBean
     * @author: HeSSD
     * @time: 2020/10/26 13:55
     */
    @PostMapping("register")
    public JsonBean register(User user) {
        JsonBean jsonBean = userService.addUser(user);
        return jsonBean;
    }

    /**
     * @param * @param null
     * @description: 登录
     * @return: JsonBean
     * @author: HeSSD
     * @time: 2020/10/26 13:57
     */
    @PostMapping("login")
    public JsonBean login(HttpServletRequest request,User user) {
        JsonBean jsonBean = userService.login(user);
        if (jsonBean.getCode() == 0){
            request.getSession().setAttribute("userInfo",jsonBean.getData());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

}
