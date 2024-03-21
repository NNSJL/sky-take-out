package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sky-take-out
 * @description: UserService的实现类
 * @author: grn
 * @createTime: 2024-03-21 12:11
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        //获取到当前用户id
        String openid = getOpenId(userLoginDTO.getCode());

        //判断openId是否为空，如果为空，表示登录失败，抛出业务异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否是新用户
        User user = userMapper.getByOpenId(openid);

        //如果是新用户，自动完成注册
        if(user == null){
           user =  User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
           userMapper.insert(user);
        }

        //返回这个用户对象

        return user;
    }

    /**
     * 调用微信接口服务，由微信用户授权码获取微信用户openId
     * @param code 授权码
     * @return
     */
    private String getOpenId(String code){
        //封装请求参数
        Map<String,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        //调用微信接口服务，获得当前微信用户的openId
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);

        //获取到当前用户id
        String openid = jsonObject.getString("openid");
        return openid;
    }

}
