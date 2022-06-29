package com.bt.service;

import com.bt.pojo.Goods;
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
public interface GoodsService extends IService<Goods> {

    List<Goods> findAll();

    void addGoods(Goods goods);

    Goods selectById(String id);
}
