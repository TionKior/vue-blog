package com.tionkior.vueblog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tionkior.vueblog.common.dto.LoginDto;
import com.tionkior.vueblog.common.lang.Result;
import com.tionkior.vueblog.entity.User;
import com.tionkior.vueblog.service.UserService;
import com.tionkior.vueblog.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author : TionKior
 * @date : 2022/1/19 18:04
 */

@RestController
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户登录,@Validated用来校验 @NotBlank
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {

        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        // 断言处理,在全局异常处理中添加一个异常处理
        Assert.notNull(user, "用户不存在");
        // md5加密方式,密码正确就抛出异常
        if (!Objects.equals(user.getPassword(), SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        // 密码正确,生成jwt
        String jwt = jwtUtils.generateToken(user.getId());
        // 把jwt放到handler放回去
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        // 把用户的信息返回回去
        return Result.success(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }

    /**
     * 退出,它需要@RequiresAuthentication认证后才能退出
     * 通过Shiro退出
     *
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }


}
