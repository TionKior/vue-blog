package com.tionkior.vueblog.shiro;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.tionkior.vueblog.common.lang.Result;
import com.tionkior.vueblog.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : TionKior
 * @date : 2022/1/19 16:29
 */
@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    // 有jwt生成token信息
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 从请求头获取jwt
        String jwt = request.getHeader("Authorization");
        // 没有获取到jwt,直接返回null
        if (StringUtils.isEmpty(jwt)) {
            return null;
        }
        return new JwtToken(jwt);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 判断获取到jwt后是不是过期,不对
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 从请求头获取jwt
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)) {
            // 直接放行,在后面会有一个注解来判断是否有权限
            return true;
        } else {
            // 检验jwt是否过期
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (ObjectUtil.isNull(claim) || jwtUtils.isTokenExpired(claim.getExpiration())) {
                throw new ExpiredCredentialsException("token 已失效,请重新登录");
            }
            // 执行登录
            return executeLogin(servletRequest, servletResponse);

        }
    }

    // 登陆失败返回具体失败原因
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 看有没有错误信息
        Throwable throwable = ObjectUtil.isNull(e.getCause()) ? e : e.getCause();
        // 封装错误结果
        Result result = Result.fail(throwable.getMessage());
        // 把错误信息封装成json数据
        String json = JSONUtil.toJsonStr(result);
        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ex) {

        }
        return false;
    }


    /**
     * 对跨域提供支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求,这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}
