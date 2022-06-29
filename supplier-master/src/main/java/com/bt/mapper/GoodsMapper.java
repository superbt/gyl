package com.bt.mapper;

import com.bt.pojo.Goods;
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
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from s_goods")
    List<Goods> selectAll();

    void addGoods(Goods goods);

    @Select("select * from s_goods where id =#{id}")
    Goods getById(String id);
}
