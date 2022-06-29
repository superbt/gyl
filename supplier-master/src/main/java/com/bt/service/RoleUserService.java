package com.bt.service;

import com.bt.pojo.RoleUser;
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
public interface RoleUserService extends IService<RoleUser> {

    List<String> selectNameById(String id);
}
