package com.bt.service.impl;

import com.bt.service.RoleService;
import com.bt.pojo.RoleUser;
import com.bt.mapper.RoleUserMapper;
import com.bt.service.RoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *
 * @since 2022-05-05
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<String> selectNameById(String id) {
        List<String> ids = baseMapper.selectRoleIdsByUserId(id);
        if (ids.size() == 0 || ids == null){
            return null;
        }
        List<String> list = roleService.selectByIds(ids);
        return list;
    }
}
