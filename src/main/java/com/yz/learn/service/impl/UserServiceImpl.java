package com.yz.learn.service.impl;

import com.github.pagehelper.PageHelper;
import com.yz.learn.constants.Constant;
import com.yz.learn.entity.SysDept;
import com.yz.learn.entity.SysUser;
import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.mapper.SysDeptMapper;
import com.yz.learn.mapper.SysUserMapper;
import com.yz.learn.service.DeptService;
import com.yz.learn.service.RedisService;
import com.yz.learn.service.UserRoleService;
import com.yz.learn.service.UserService;
import com.yz.learn.utils.PageUtil;
import com.yz.learn.utils.PasswordUtil;
import com.yz.learn.utils.jwt.JwtProperties;
import com.yz.learn.utils.jwt.JwtTokenUtil;
import com.yz.learn.vo.req.*;
import com.yz.learn.vo.resp.LoginRespVO;
import com.yz.learn.vo.resp.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private JwtProperties jwtProperties;


    @Override
    public LoginRespVO login(LoginReqVO vo) {
        SysUser sysUser = sysUserMapper.getUserInfoByName(vo.getUsername());
        LoginRespVO loginRespVO = new LoginRespVO();
        if(sysUser==null){
            throw new BusinessException(BaseResponseCode.ACCOUNT_NOT_EXIST);
        }
        if (sysUser.getStatus()==2){
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCKED);
        }
        if (!PasswordUtil.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())){
            throw new BusinessException(BaseResponseCode.ACCOUNT_PASSWORD_ERROR);
        }

            BeanUtils.copyProperties(sysUser, loginRespVO);
            List<String> permissions = getPermissionsByUserId(loginRespVO.getId());
            List<String> roles = getRolesByUserId(loginRespVO.getId());
            HashMap<String, Object> claims = new HashMap<>();
            claims.put(Constant.JWT_PERMISSIONS_KEY, permissions);
            claims.put(Constant.JWT_ROLES_KEY, roles);
            claims.put(Constant.JWT_USER_NAME, loginRespVO.getUsername());
            String accessToken = JwtTokenUtil.getAccessToken(loginRespVO.getId(), claims);
            String refreshToken = null;
            if(vo.getType().equals("1")) {
                refreshToken = JwtTokenUtil.getRefreshToken(loginRespVO.getId(), claims);
            }else {
                refreshToken = JwtTokenUtil.getRefreshAppToken(loginRespVO.getId(), claims);
            }
            loginRespVO.setAccessToken(accessToken);
            loginRespVO.setRefreshToken(refreshToken);

        return loginRespVO;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)
                || !JwtTokenUtil.validateToken(accessToken) || !JwtTokenUtil.validateToken(refreshToken)){
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }

//        Subject subject = SecurityUtils.getSubject();
//        log.info("subject.getPrincipals()={}", subject.getPrincipal());
//        if(subject.isAuthenticated()){
//            subject.logout();
//        }

        /**
         * 把token 加入黑名单 禁止再登录
         */
        String userId=JwtTokenUtil.getUserIdFromToken(accessToken);
        redisService.set(Constant.JWT_ACCESS_TOKEN_BLACKLIST+accessToken,userId,JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

        /**
         * 把 refreshToken 加入黑名单 禁止再拿来刷新token
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId,JwtTokenUtil.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);
    }

    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysUser> userList = sysUserMapper.selectAll(vo);
        for(SysUser user : userList){
            SysDept sysDept = sysDeptMapper.selectByPrimaryKey(user.getDeptId());
            if(sysDept!=null)
                user.setDeptName(sysDept.getName());
        }
        return PageUtil.getPageVO(userList);

    }

    @Override
    public void addUser(UserAddReqVO vo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setSalt(PasswordUtil.getSalt());
        sysUser.setPassword(PasswordUtil.encode(vo.getPassword(), sysUser.getSalt()));
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insertSelective(sysUser);
        if(i!=1)
            throw new BusinessException(BaseResponseCode.OPERATION_EROR);
    }

    @Override
    public void setUserOwnRole(UserRoleOperationReqVO vo) {
        userRoleService.addUserRoleInfo(vo);
        redisService.set(Constant.JWT_REFRESH_KEY+vo.getUserId(),vo.getUserId(),jwtProperties.getAccessTokenExpireTime().toMillis(),TimeUnit.MILLISECONDS);
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
