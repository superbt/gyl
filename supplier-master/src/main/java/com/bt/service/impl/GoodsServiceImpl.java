package com.bt.service.impl;

import com.bt.pojo.Goods;
import com.bt.mapper.GoodsMapper;
import com.bt.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public List<Goods> findAll() {
        return baseMapper.selectAll();
    }

    @Override
    public void addGoods(Goods goods) {
        baseMapper.addGoods(goods);
    }

    @Override
    public Goods selectById(String id) {
        return baseMapper.getById(id);
    }
}
