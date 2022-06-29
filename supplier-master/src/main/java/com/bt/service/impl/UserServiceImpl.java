package com.bt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bt.handler.NotFountException;
import com.bt.pojo.User;
import com.bt.mapper.UserMapper;
import com.bt.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bt.utils.PasswordUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 * @since 2022-05-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = baseMapper.selectOne(wrapper);
        if (user == null){
            throw new NotFountException("用户不存在");
        }
        if (user.getStatus() == 2){
            throw new NotFountException("账号被冻结");
        }
        if (!PasswordUtils.matches(user.getSalt(),password,user.getPassword())){
            throw new NotFountException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public boolean updateByUserId(User user)  {

        return baseMapper.updateByUserId(user);
    }
}
