package com.bt.mapper;

import com.bt.pojo.RoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 *
 * @since 2022-05-05
 */
@Mapper
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    @Select("select role_id from s_role_user where user_id = #{id}")
    List<String> selectRoleIdsByUserId(String id);
}
