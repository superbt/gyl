package com.bt.controller;


import com.bt.pojo.Role;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bt.service.RoleService;
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
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver resolver;

    @RequestMapping(value = "/roles")
    public String roles(Model model, @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize
            , HttpServletRequest request, HttpServletResponse response){
        PageHelper.startPage(pageNo,pageSize);
        List<Role> list = roleService.list();
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        model.addAttribute("roles",list);
        model.addAttribute("pageInfo",pageInfo);
        return "role-list";
    }


    @RequestMapping(value = "/getByIdForRole/{id}",produces ="text/html;charset=utf-8" )
    @ResponseBody
    public String getById(@PathVariable("id")String id, Model model,HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("update-role");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        Role byId = roleService.getById(id);
        model.addAttribute("role",byId);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = resolver.getTemplateEngine().process("update-role", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("update-role",html,60,TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("/doUpdateForRole")
    public String doUpdate(Role role){
        role.setUpdateTime(new Date());
        roleService.updateById(role);
        return "redirect:/api/roles";
    }

    @RequestMapping("/deleteForRole/{id}")
    public String deleteById(@PathVariable("id")String id){
        roleService.removeById(id);
        return "redirect:/api/roles";
    }

    @RequestMapping("/doAddForRole")
    public String doAdd(Role role){
        role.setId(UUID.randomUUID().toString());
        role.setCreateTime(new Date());
        roleService.save(role);
        return "redirect:/api/roles";
    }

}

