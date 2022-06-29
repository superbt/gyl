package com.bt.service;

import com.bt.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 *
 * @since 2022-05-05
 */
public interface RoleService extends IService<Role> {

    List<String> selectByIds(List<String> ids);
}
