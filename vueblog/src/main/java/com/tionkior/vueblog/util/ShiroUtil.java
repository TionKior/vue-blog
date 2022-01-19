package com.tionkior.vueblog.util;

import com.tionkior.vueblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @author : TionKior
 * @date : 2022/1/19 18:49
 */

public class ShiroUtil {

    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
