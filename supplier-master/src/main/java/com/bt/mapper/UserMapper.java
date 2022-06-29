package com.bt.mapper;

import com.bt.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 *
 * @since 2022-05-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    boolean updateByUserId(User user);
}
