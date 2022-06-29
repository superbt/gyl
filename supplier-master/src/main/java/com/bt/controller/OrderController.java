package com.bt.controller;


import com.bt.pojo.Order;
import com.bt.pojo.User;
import com.bt.pojo.vo.OrderVo;
import com.bt.service.GoodsService;
import com.bt.service.OrderService;
import com.bt.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bt.pojo.Goods;
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
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver resolver;

    @RequestMapping(value = "/orders",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String roles(Model model, @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize
            , HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("order-list");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        PageHelper.startPage(pageNo,pageSize);
        List<Order> list = orderService.list();
        List<OrderVo> voList = new ArrayList<>();
        for (Order order : list) {
            OrderVo vo = new OrderVo();
            vo.setOrderId(order.getId());
            User byId = userService.getById(order.getUserId());
            Goods goods = goodsService.selectById(order.getGoodsId());
            vo.setUsername(byId.getNickName());
            vo.setGoodsName(goods.getTitle());
            vo.setOrderNum(order.getOrderNum());
            vo.setType(order.getType());
            vo.setTotalPrice(order.getTotalPrice());
            voList.add(vo);
        }
        PageInfo<OrderVo> pageInfo = new PageInfo<>(voList);
        model.addAttribute("orders",voList);
        model.addAttribute("pageInfo",pageInfo);
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = resolver.getTemplateEngine().process("order-list", webContext);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("order-list",html,60, TimeUnit.SECONDS);
        }
        return html;
    }


    @RequestMapping("/getByIdForOrder/{id}")
    public String getById(@PathVariable("id")String id, Model model){
        Order byId = orderService.getById(id);
        model.addAttribute("order",byId);
        return "";
    }

    @RequestMapping("/doUpdateForOrder")
    public String doUpdate(Order role){
        role.setUpdateTime(new Date());
        orderService.updateById(role);
        return "redirect:/api/orders";
    }

    @RequestMapping("/deleteForOrder/{id}")
    public String deleteById(@PathVariable("id")String id){
        orderService.removeById(id);
        return "redirect:/api/orders";
    }

    @RequestMapping("/doAddForOrder")
    public String doAdd(Order role){
        role.setId(UUID.randomUUID().toString());
        role.setCreateTime(new Date());
        orderService.save(role);
        return "redirect:/api/orders";
    }
}

