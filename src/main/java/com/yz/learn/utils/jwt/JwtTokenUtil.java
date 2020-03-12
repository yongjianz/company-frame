package com.yz.learn.utils.jwt;

import com.yz.learn.constants.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtTokenUtil {

    private static String secretKey;
    private static Duration accessTokenExpireTime;
    private static Duration refreshTokenExpireTime;
    private static Duration refreshTokenExpireAppTime;
    private static String issuer;

    public static void setTokenSettings(JwtProperties jwtProperties){
        secretKey = jwtProperties.getSecretKey();
        accessTokenExpireTime = jwtProperties.getAccessTokenExpireTime();
        refreshTokenExpireTime = jwtProperties.getRefreshTokenExpireAppTime();
        refreshTokenExpireAppTime = jwtProperties.getRefreshTokenExpireAppTime();
        issuer = jwtProperties.getIssuer();
    }

    /**
     *生成 access_token
     *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param subject
    • * @param claims
     *@return java.lang.String
     *@throws
     */
    public static String getAccessToken(String subject, Map<String,Object> claims){
        return generateToken(issuer,subject,claims,accessTokenExpireTime.toMillis(),secretKey);
    }


    /**
     *签发token
     *@Author:小霍
     *@UpdateUser:
     *@Version:0.0.1
     *@paramissuer 签发人
     *@paramsubject 代表这个JWT的主体，即它的所有人 一般是用户id
     *@paramclaims 存储在JWT里面的信息 一般放些用户的权限/角色信息
     *@paramttlMillis 有效时间(毫秒)
     *@return java.lang.String
     *@throws
     */
    public static String generateToken(String issuer, String subject, Map<String,Object> claims, Long ttlMills, String secretKey){
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signingKey = DatatypeConverter.parseBase64Binary(secretKey);

        JwtBuilder jwtBuilder = Jwts.builder();

        if(null!=claims){
            jwtBuilder.setClaims(claims);
        }
        if(!StringUtils.isEmpty(issuer)){
            jwtBuilder.setIssuer(issuer);
        }
        if (!StringUtils.isEmpty(subject)){
            jwtBuilder.setSubject(subject);
        }

        jwtBuilder.setIssuedAt(now);

        if(ttlMills >=0){
            Long expMills = nowMillis + ttlMills;
            Date expDate = new Date(expMills);
            jwtBuilder.setExpiration(expDate);
        }

        jwtBuilder.signWith(algorithm, signingKey);
        return jwtBuilder.compact();
    }


    /**
     *生产 PC refresh_token
     *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param subject
    •* @param claims
     *@return java.lang.String
     *@throws
     */ public static String getRefreshToken(String subject,Map<String,Object> claims){
         return generateToken(issuer,subject,claims,refreshTokenExpireTime.toMillis(),secretKey);
     }

    /**
     *生产 App端 refresh_token
     *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param subject
    •* @param claims
     *@return java.lang.String
     *@throws
     * */

    public static String getRefreshAppToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireAppTime.toMillis(),secretKey);
    }

    /**
     *从令牌中获取数据声明
     *@Author: 小霍 *@UpdateUser:
     *@Version: 0.0.1
     *@param token
     *@return io.jsonwebtoken.Claims *@throws
     */

    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try{
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        }catch (Exception e) {
            claims= null;
        }
        return claims;
    }

    /**
     *获取用户id
     *@Author:小霍
     *@UpdateUser:
     *@Version:0.0.1
     *@param token
     *@return java.lang.String
     *@throws
     */
    public static String getUserIdFromToken(String token) {
       String userId = null;
        try{
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        }catch (Exception e) {
            log.error("error{}", e);
        }
        return userId;
    }

    /**
     *获取用户名
     *@Author:小霍
     *@UpdateUser:
     *@Version:0.0.1
     *@param token
     *@return java.lang.String
     *@throws
     */
    public static String getUserNameFromToken(String token) {
        String userName = null;
        try{
            Claims claims = getClaimsFromToken(token);
            userName = (String) claims.get(Constant.JWT_USER_NAME);
        }catch (Exception e) {
            log.error("error{}", e);
        }
        return userName;
    }

    /**
     *验证token 是否过期(true:已过期 false:未过期) *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param token
    •* @param secretKey
     *@return java.lang.Boolean
     *@throws
     */
    public static boolean isTokenExpired(String token){
       try{
           Claims claims = getClaimsFromToken(token);
           Date expiration = claims.getExpiration();
           return expiration.before(new Date());
       }catch (Exception e){
           log.error("error{}", e);
           return true;
       }
    }

    /**
     *校验令牌(true：验证通过 false：验证失败)
     *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param token
     *@return java.lang.Boolean
     *@throws
     */

    public static boolean validateToken(String token){
        return (null!=getClaimsFromToken(token) && !isTokenExpired(token));
    }

    /**
     *刷新token
     *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param refreshToken
     *@param claims 主动去刷新的时候 改变JWT payload 内的信息
     *@return java.lang.String
     *@throws
     */
    public static String refreshToken(String refreshToken,Map<String, Object> claims) {
        String refreshedToken;
        try {
                Claims parserclaims = getClaimsFromToken(refreshToken);
            /**
             * 刷新token的时候如果为空说明原先的 用户信息不变 所以就引用上个token里的内容
             */
                if(null==claims){
                    claims=parserclaims;
                }
                refreshedToken = generateToken(parserclaims.getIssuer(),parserclaims.getSubject(),claims,accessTokenExpireTime.toMillis(),secretKey);
        }catch (Exception e) {
            refreshedToken= null;
            log.error("error={}",e);
        }
        return refreshedToken;
    }

    /**
     *获取token的剩余过期时间
     *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param token
    •* @param secretKey
     *@return long
     *@throws
     */
    public static long getRemainingTime(String token){
        long result=0;
        try{
            long nowMillis = System.currentTimeMillis();
            result= getClaimsFromToken(token).getExpiration().getTime()-nowMillis;
        }
        catch (Exception e) {
            log.error("error={}",e);
        }
        return result;
    }
}
