package com.bt.service.impl;

import com.bt.pojo.News;
import com.bt.mapper.NewsMapper;
import com.bt.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *
 * @since 2022-05-05
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

}
