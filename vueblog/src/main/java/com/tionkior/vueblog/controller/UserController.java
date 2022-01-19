package com.tionkior.vueblog.controller;


import com.tionkior.vueblog.common.lang.Result;
import com.tionkior.vueblog.entity.User;
import com.tionkior.vueblog.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequiresAuthentication
    @GetMapping("/index")
    public Result index() {
        User user = userService.getById(1L);
        return Result.success(user);
    }

    /**
     * 注解@Validated可以帮助我们校验
     *
     * @param user
     * @return
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        return Result.success(user);
    }

}
