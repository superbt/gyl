package com.bt.controller;


import com.bt.pojo.Category;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bt.service.CategoryService;
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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/categorys",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String roles(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        //判断Redis是否为空，如果不为空，则直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("category-list");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        PageHelper.startPage(pageNo,pageSize);
        List<Category> list = categoryService.list();
        PageInfo<Category> pageInfo = new PageInfo<>(list);
        List<Category> categories = pageInfo.getList();
        request.getSession().setAttribute("categories",categories);
        model.addAttribute("pageInfo",pageInfo);
        request.getSession().setAttribute("pageInfo",pageInfo);
        //如果为空，手动渲染，存入Redis中并返回
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("category-list", webContext);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("category-list",html,60, TimeUnit.SECONDS);
        }
        return html;
    }


    @RequestMapping(value = "/getByIdForCate/{id}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getById(@PathVariable("id")String id, Model model,HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("getByIdForCate");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        Category byId = categoryService.getById(id);
        request.getSession().setAttribute("category",byId);
        model.addAttribute("category",byId);
        //如果为空，手动渲染，存入Redis中并返回
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("getByIdForCate", webContext);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("getByIdForCate",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("/doUpdateForCate")
    public String doUpdate(Category role){
        role.setUpdateTime(new Date());
        categoryService.updateById(role);
        return "redirect:/api/categorys";
    }

    @RequestMapping("/deleteForCate/{id}")
    public String deleteById(@PathVariable("id")String id){
        categoryService.removeById(id);
        return "redirect:/api/categorys";
    }

    @RequestMapping("/doAddForCate")
    public String doAdd(Category role){
        role.setId(UUID.randomUUID().toString());
        role.setCreateTime(new Date());
        categoryService.save(role);
        return "redirect:/api/categorys";
    }
}

