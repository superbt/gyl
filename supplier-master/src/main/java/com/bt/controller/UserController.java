package com.bt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bt.pojo.User;
import com.bt.service.UserService;
import com.bt.utils.OssUtils;
import com.bt.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver viewResolver;

    @RequestMapping(value = "/users",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String userList(Model model, @RequestParam(value = "pageNo",required = true,defaultValue = "1") Integer pageNo,
                           @RequestParam(value = "pageSize",required = true,defaultValue = "10")Integer pageSize, String nickName
                            , HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("user-list");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(nickName)){
            wrapper.like("nick_name",nickName);
        }
        PageHelper.startPage(pageNo,pageSize);
        List<User> list = userService.list(wrapper);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        model.addAttribute("list",list);
        log.info("-------------->pageNum:{} ",pageInfo.getPageNum());
        model.addAttribute("users",pageInfo);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("user-list", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("user-list",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/getByIdForUser/{id}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getById(@PathVariable("id")String id,Model model,HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("update-user");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        User byId = userService.getById(id);
        model.addAttribute("user",byId);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("update-user", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("update-user",html,60,TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("/doUpdateForUser")
    public String doUpdate(User user, @RequestParam("file") MultipartFile files, HttpServletRequest request) throws IOException {
        User byId = userService.getById(user.getId());
        String url = OssUtils.uploadFileAvatar(files);
        byId.setAvatar(url);
        //不等于说明修改
        if (!user.getPassword().equals(byId.getPassword())){
            String salt = byId.getSalt();
            String encode = PasswordUtils.encode(salt, user.getPassword());
            user.setPassword(encode);
        }
        BeanUtils.copyProperties(byId,user);
        boolean b = userService.updateById(user);
        if (b){
            return "redirect:/api/users";
        }
        return "redirect:/api/doUpdateForUser";
    }

    @RequestMapping("/deleteForUser/{id}")
    public String deleteById(@PathVariable("id")String id){
        userService.removeById(id);
        return "redirect:/api/users";
    }

    @RequestMapping("/doAddForUser")
    public String doAdd(User user, @RequestParam("file") MultipartFile files, HttpServletRequest request) throws IOException {
        //String path = null;
//        if (files != null && !files.isEmpty()){
//            String name = UUID.randomUUID().toString().replace("-","");
//            //获取文件的扩展名
//            String ext = FilenameUtils.getExtension(files.getOriginalFilename());
//            //设置文件上传的路径
//            String url =request.getSession().getServletContext().getRealPath("/upload/");
//
//            File file = new File(url);
//            if (!file.exists()){
//                file.mkdir();
//            }
//            //测试路径
//            System.out.println(request.getServletPath()+ "/upload");
//            System.out.println(request.getContextPath() + "/upload/");
//            //以绝对路径保存重命名后的文件
//            files.transferTo(new File(url+"/"+name+"."+ext));
//            user.setAvatar(request.getContextPath() + "/upload/"+name+"."+ext);
//        }

        user.setId(UUID.randomUUID().toString());
        String salt = PasswordUtils.getSalt();
        String password = user.getPassword();
        String encode = PasswordUtils.encode(password, salt);
        String url = OssUtils.uploadFileAvatar(files);
        user.setAvatar(url);
        user.setSalt(salt) ;
        user.setPassword(encode);
        user.setCreateTime(new Date());
        userService.save(user);
        return "redirect:/api/users";
    }

}

