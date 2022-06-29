package com.bt.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bt.pojo.News;
import com.bt.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 *
 * @since 2022-05-05
 */
@Controller
@RequestMapping("/api")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver viewResolver;

    @RequestMapping(value = "/news",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String roles(Model model, @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize
            , HttpServletRequest request, HttpServletResponse response){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("news-list");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        PageHelper.startPage(pageNo,pageSize);
        List<News> list = newsService.list();
        PageInfo<News> pageInfo = new PageInfo<>(list);
        model.addAttribute("news",list);
        model.addAttribute("pageInfo",pageInfo);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("news-list", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("news-list",html,60, TimeUnit.SECONDS);
        }
        return html;
    }


    @RequestMapping("/getByIdForNews/{id}")
    public String getById(@PathVariable("id")String id, Model model,HttpServletRequest request,HttpServletResponse response){
        News byId = newsService.getById(id);
        model.addAttribute("news",byId);
        request.getSession().setAttribute("news",byId);
        return "update-news";
    }

    @RequestMapping("/doUpdateForNews")
    public String doUpdate(News role){
        role.setUpdateTime(new Date());
        newsService.updateById(role);
        return "redirect:/api/news";
    }

    @RequestMapping("/deleteForNews/{id}")
    public String deleteById(@PathVariable("id")String id){
        newsService.removeById(id);
        return "redirect:/api/news";
    }

    @RequestMapping("/doAddForNews")
    public String doAdd(News role){
        role.setId(UUID.randomUUID().toString());
        role.setCreateTime(new Date());
        newsService.save(role);
        return "redirect:/api/news";
    }
}

