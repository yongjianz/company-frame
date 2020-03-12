package com.yz.learn.shiro;

import com.yz.learn.constants.Constant;
import com.yz.learn.service.RedisService;
import com.yz.learn.utils.jwt.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomPasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String accessToken= (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        String userId= JwtTokenUtil.getUserIdFromToken(accessToken);
        if(redisService.hasKey(Constant.REFRESH_TOKEN+userId)
                && redisService.getExpire(Constant.REFRESH_TOKEN+userId,TimeUnit.MILLISECONDS)>JwtTokenUtil.getRemainingTime(accessToken)){
            List<String> roleList = getRolesByUserId(userId);
            if(roleList!=null && roleList.size()>0){
                info.addRoles(roleList);
            }
            List<String> premissionList = getPermissionsByUserId(userId);
            if(premissionList!=null && premissionList.size()>0){
                info.addRoles(premissionList);
            }
        }else{
            Claims claims= JwtTokenUtil.getClaimsFromToken(accessToken);
            if(claims.get(Constant.JWT_ROLES_KEY)!=null){
                info.addRoles((Collection<String>) claims.get(Constant.JWT_ROLES_KEY));
            }
            if(claims.get(Constant.JWT_PERMISSIONS_KEY)!=null){
                info.addStringPermissions((Collection<String>) claims.get(Constant.JWT_PERMISSIONS_KEY));
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        CustomPasswordToken customPasswordToken = (CustomPasswordToken) token;
        return new SimpleAuthenticationInfo(customPasswordToken.getPrincipal(),null,"");
        //return new SimpleAuthenticationInfo("","","");
    }

    /**
     *获取用户的角色
     *这里先用伪代码代替
     *后面我们讲到权限管理系统后 再从 DB 读取
     *@Version: 0.0.1
     *@param userId
     *@return
     *@throws
     * */

    private List<String> getRolesByUserId(String userId){
        List<String> list=new ArrayList<>();
        if("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8".equals(userId)){
            list.add("admin");
        }else{
            list.add("test");
        }
        return list;
    }

    /**
     *获取用户的权限
     *这里先用伪代码代替
     *后面我们讲到权限管理系统后 再从 DB 读取
     *@Version: 0.0.1
     *@param userId
     *@return
     *@throws
     */

    private List<String>getPermissionsByUserId(String userId){
        List<String> list=new ArrayList<>();
        if("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8".equals(userId)){
            list.add("sys:user:add");
            list.add("sys:user:list");
            list.add("sys:user:update");
            list.add("sys:user:detail");
        }else{
            list.add("sys:user:list");
        }
        return list;
    }
}
