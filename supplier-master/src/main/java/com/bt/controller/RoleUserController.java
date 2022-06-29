package com.bt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bt.pojo.Role;
import com.bt.pojo.User;
import com.bt.service.RoleService;
import com.bt.service.RoleUserService;
import com.bt.service.UserService;
import com.bt.pojo.RoleUser;
import com.bt.pojo.vo.DistributionVo;
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
import java.util.ArrayList;
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
public class RoleUserController {

    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver viewResolver;


    @RequestMapping(value = "/roleForUserById/{id}",produces ="text/html;charset=utf-8")
    @ResponseBody
    public String roleForUser(@PathVariable("id")String id, Model model, HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("user-role");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        List<RoleUser> roleUsers = roleUserService.list(new QueryWrapper<RoleUser>().eq("role_id", id));
        List<User> userList = userService.list();
        //List<Role> allRole = new ArrayList<>();
        List<Role> allRole = roleService.list();
        request.getSession().setAttribute("allUsersUnlessMe",userList);
        request.getSession().setAttribute("allRoles",allRole);
        request.getSession().setAttribute("roleId",id);
        List<User> list = new ArrayList<>();
        for (RoleUser roleUser : roleUsers) {
            User byId = userService.getById(roleUser.getUserId());
            list.add(byId);
        }
        model.addAttribute("list",list);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("user-role", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("user-role",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("/deleteRoleForUser/{id}")
    public String deleteRoleForUser(@PathVariable("id")String id,HttpServletRequest request){
        String roleId = (String) request.getSession().getAttribute("roleId");
        roleUserService.remove(new QueryWrapper<RoleUser>().eq("user_id",id).eq("role_id",roleId));
        return "redirect:/api/roleForUserById/"+roleId;
    }

    @RequestMapping("/distributionRole")
    public String distributionRole(DistributionVo vo,HttpServletRequest request){
        String roleId = (String) request.getSession().getAttribute("roleId");
        RoleUser roleUser = new RoleUser();
        roleUser.setId(UUID.randomUUID().toString());
        roleUser.setUserId(vo.getUserId());
        roleUser.setRoleId(vo.getRoleId());
        roleUser.setCreateTime(new Date());
        roleUserService.save(roleUser);
        return "redirect:/api/roleForUserById/"+roleId;
    }
}

