package com.brecycle.config.shiro;

import com.brecycle.entity.dto.UserInfo;
import com.brecycle.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对登录用户进行权限验证
 *
 * @author cmgun
 */
@Slf4j
@Component
public class UserRealm extends AuthorizingRealm {

    @Lazy
    @Autowired
    private UserService userService;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UserToken;
    }

    /**
     * 授权
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("授权验证->doGetAuthenticationInfo()");
        String token = principals.toString();
        String username = JwtTokenUtil.getUsername(token);
//        UserInfo userInfo = userInfoService.getUserByUsername(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //查询数据库来获取用户的角色
        info.addRoles(Arrays.asList("SYS", "ARR", "TTT"));
        //查询数据库来获取用户的权限
        return info;
    }

    /**
     * 身份验证
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     *
     * @param authenticationToken token
     * @return SimpleAuthenticationInfo
     * @throws AuthenticationException 登录异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("登录验证->doGetAuthenticationInfo()");
        String token = (String) authenticationToken.getCredentials();
        log.info("当前token : {}", token);
        String username = JwtTokenUtil.getUsername(token);
        if (Objects.isNull(username)) {
            throw new AuthenticationException("登录过期,请重新登录!");
        }
        // TODO 增加数据库验证
//        UserInfo userInfo = userInfoService.getUserByUsername(username);
        UserInfo userInfo = UserInfo.builder().userName(username).build();
        if (null == userInfo) {
            throw new AuthenticationException("该用户不存在");
        }
        log.info("当前用户 : {}", userInfo);
        return new SimpleAuthenticationInfo(userInfo, token, "UserRealm");
    }
}
