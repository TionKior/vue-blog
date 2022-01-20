package com.tionkior.vueblog.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tionkior.vueblog.common.lang.Result;

import com.tionkior.vueblog.entity.Blog;
import com.tionkior.vueblog.service.BlogService;
import com.tionkior.vueblog.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
public class BlogController {


    @Autowired
    private BlogService blogService;

    /**
     * 查询博客列表,给一个默认值
     *
     * @param currentPage
     * @return
     */
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

        Page page = new Page(currentPage, 5);
        // 倒叙查询
        Page pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.success(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        if (ObjectUtil.isNull(id)) {
            return Result.fail("请传入删除的博客ID");
        }
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.success(blog);
    }

    /**
     * 编辑添加是在一起的,如果有blog,则编辑,如果没有blog,则添加
     * 注解@RequiresAuthentication证明这个接口需要认证之后才能使用
     *
     * @param blog
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

        Blog temp = null;

        // 如果有这个BlogId,说明这个Blog是添加
        if (ObjectUtil.isNotEmpty(blog.getId())) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            Assert.isTrue(Objects.equals(temp.getUserId(), ShiroUtil.getProfile().getId()), "没有权限编辑");
        } else {
            // 添加博客
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }

        // 复制前台传入的blog给temp,后面的参数是忽略一些字段,因为那些字段是从Shiro中获取的
        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        // 保存或更新
        blogService.saveOrUpdate(temp);

        return Result.success(null);
    }

}
