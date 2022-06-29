package com.bt.controller;

import com.bt.service.RoleUserService;
import com.bt.pojo.User;
import com.bt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @website https://blog.csdn.net/Gaowumao
 * @Date 2022-05-05 01:19
 * @Description
 */
@Controller
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleUserService roleUserService;


    @RequestMapping("/doLogin")
    public String doLogin(String username, String password, Model model, HttpServletRequest request){
        User user = userService.login(username,password);
        if(user==null){
            return "extra-login";
        }
        List<String> list = roleUserService.selectNameById(user.getId());

        model.addAttribute("user",user);
        request.getSession().setAttribute("user",user);
        request.getSession().setAttribute("list",list);

        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "extra-login";
    }


    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        return "extra-login";
    }
}
