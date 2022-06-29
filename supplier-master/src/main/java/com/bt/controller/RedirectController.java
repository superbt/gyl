package com.bt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @website https://blog.csdn.net/Gaowumao
 * @Date 2022-05-05 11:07
 * @Description
 */
@Controller
@RequestMapping("/api")
public class RedirectController {

    //登录页面
    @RequestMapping
    public String login(){
        return "extra-login";
    }
    //首页
    @RequestMapping("/index")
    public String toIndex(){
        return "index";
    }

    //跳转用户列表界面
    @RequestMapping("/user-list")
    public String userList(){
        return "user-list";
    }

    //跳转商品列表界面
    @RequestMapping("/goods-list")
    public String goodsList(){
        return "goods-list";
    }
    //跳转角色列表界面
    @RequestMapping("/role-list")
    public String roleList(){
        return "role-list";
    }

    //跳转分类列表界面
    @RequestMapping("/category-list")
    public String categoryList(){
        return "category-list";
    }

    //跳转订单列表界面
    @RequestMapping("/order-list")
    public String OrderList(){
        return "order-list";
    }

    //跳转新闻列表界面
    @RequestMapping("/news-list")
    public String newsList(){
        return "news-list";
    }

    //添加用户
    @RequestMapping("/add-user")
    public String addTemplate(){
        return "add-user";
    }

    //添加新闻
    @RequestMapping("/add-news")
    public String addNews(){
        return "add-news";
    }

    //添加角色
    @RequestMapping("/add-role")
    public String addRole(){
        return "add-role";
    }

    //添加商品
    @RequestMapping("/add-goods")
    public String addGoods(){
        return "add-goods";
    }


    //个人中心
    @RequestMapping("/single-center")
    public String singleCenter(){
        return "single-center";
    }
    //添加分类
    @RequestMapping("/add-category")
    public String addCategory(){
        return "add-category";
    }


    //添加分类
    @RequestMapping("/add-user-role")
    public String addUserRole(){
        return "add-user-role";
    }


}
