package com.bt.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bt.pojo.Category;
import com.bt.pojo.Goods;
import com.bt.service.CategoryService;
import com.bt.service.GoodsService;
import com.bt.utils.OssUtils;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
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
import java.io.File;
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
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver viewResolver;

    @RequestMapping(value = "/goods",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String GoodsList(Model model, @RequestParam(value = "pageNo",required = true,defaultValue = "1") Integer pageNo,
                            @RequestParam(value = "pageSize",required = true,defaultValue = "10")Integer pageSize, HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goods-list");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        PageHelper.startPage(pageNo,pageSize);
        List<Goods> list = goodsService.findAll();
        List<Category> categories = categoryService.list();
        PageInfo<Goods> pageInfo = new PageInfo<>(list);
        List<Goods> goods = pageInfo.getList();
        model.addAttribute("goods",goods);
        model.addAttribute("categories",categories);
        model.addAttribute("pageInfo",pageInfo);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("goods-list", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goods-list",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/getByIdForGoods/{id}", produces ="text/html;charset=utf-8")
    @ResponseBody
    public String getById(@PathVariable("id")String id, Model model,HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("update-goods");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        Goods byId = goodsService.selectById(id);
        model.addAttribute("goodById",byId);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("update-goods", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("update-goods",html,60,TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("/doUpdateForGoods")
    public String doUpdate(Goods goods, @RequestParam("file") MultipartFile files, HttpServletRequest request) throws IOException {
        String url = OssUtils.uploadFileAvatar(files);
        goods.setPrams(url);
        goods.setUpdateTime(new Date());
        goodsService.updateById(goods);
        return "redirect:/api/goods";
    }

    @RequestMapping("/deleteForGoods/{id}")
    public String deleteById(@PathVariable("id")String id){
        goodsService.removeById(id);
        return "redirect:/api/goods";
    }

    @RequestMapping("/doAddForGoods")
    public String doAdd(Goods goods, @RequestParam("file") MultipartFile files, HttpServletRequest request) throws IOException {
        //String path = null;
        goods.setId(UUID.randomUUID().toString());
        String url = OssUtils.uploadFileAvatar(files);
        goods.setPrams(url);
        goods.setCreateTime(new Date());
        goodsService.addGoods(goods);
        return "redirect:/api/goods";
    }

    /**
     * 抽离的方法体
     * @param goods
     * @param files
     * @param request
     * @throws IOException
     */
    private void extracted(Goods goods, @RequestParam("file") MultipartFile files, HttpServletRequest request) throws IOException {
            if (files != null && !files.isEmpty()){
                String name = UUID.randomUUID().toString().replace("-","");
                //获取文件的扩展名
                String ext = FilenameUtils.getExtension(files.getOriginalFilename());
                //设置文件上传的路径
                String url =request.getSession().getServletContext().getRealPath("/upload/");

                File file = new File(url);
                if (!file.exists()){
                    file.mkdir();
                }
                //测试路径
                System.out.println(request.getServletPath()+ "/upload");
                System.out.println(request.getContextPath() + "/upload/");
                //以绝对路径保存重命名后的文件
                files.transferTo(new File(url+"/"+name+"."+ext));
                    goods.setPrams(request.getContextPath() + "/upload/"+name+"."+ext);
            }
    }
}

