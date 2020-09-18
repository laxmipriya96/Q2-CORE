/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2api.configuration.ConfigProp;
import com.vm.qsmart2api.configuration.JwtRedisKeystore;
import com.vm.qsmart2api.dtos.UserDetails;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.jwt.JwtUserDto;
import com.vm.qsmart2api.mapper.Mapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.DigestUtils;

/**
 *
 * @author Ashok
 */
@Configuration
public class JwtTokenUtil {

    private static final Logger logger = LogManager.getLogger(JwtTokenUtil.class);

    private final static String TOKENPREFIX = "Bearer";

    private final static String HEADERSTRING = "Authorization";

    private final static String HEADER = "[JWT_TOKEN_UTILS] ";

    private static final String SECRET = "vectramind";

    @Autowired
    ConfigProp configProp;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtRedisKeystore jwtRedisKeystore;

    @Autowired
    DateUtils dateUtils;
    
    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    public String getHash(String username, Date curerntDate) {
        return DigestUtils.md5DigestAsHex(String.format("%s_%d", username, curerntDate.getTime()).getBytes());
    }

    public String genrateJwtToken(UserDetails userDetails, Long enterpriseId) {
        Map<String, Object> claimMap = null;
        String token = null;
        Date createdDate = null;
        try {
            if (isLogEnabled) {
                logger.info("{}>>:User:[{}]:EnterPriseId:{}", HEADER, userDetails, enterpriseId);
            }
            JwtUserDto user = Mapper.INSTANCE.userDetailsToJwtUserDto(userDetails);
            createdDate = dateUtils.getdate();
            claimMap = new HashMap<>();
            claimMap.put("userid", (Long)user.getUserId());
            claimMap.put("username", user.getUserName().toUpperCase());
            claimMap.put("roleId", user.getRoles().getRoleId());
            claimMap.put("enterpriseId", enterpriseId);
            claimMap.put("hash", getHash(user.getUserName(), createdDate));
//            user.setEnterprise(null);
//            user.setLocation(null);
            token = Jwts.builder()
                    .setClaims(claimMap)
                    .setSubject(mapper.writeValueAsString(user))
                    .setIssuedAt(dateUtils.getdate())
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();
//            try {
//                if (configProp.getExpiryMap().containsKey(user.getRoles().getRoleId())) {
//                    expiration = configProp.getExpiryMap().get(user.getRoles().getRoleId());
//                }
//            } catch (Exception e) {
//                logger.error("{}Excep:Config:getExpiryMap:genrateJwtToken:Config:Error:[{}]", HEADER, ExceptionUtils.getRootCauseMessage(e));
//            }
            String key = String.format("%s:%s", user.getUserName(), getHash(user.getUserName(), createdDate))
                    .toUpperCase();
            if (isLogEnabled) {
                logger.info("{}Key:[{}]ExpirationTime:[{}]", HEADER, key, configProp.getExpiration());
            }
            jwtRedisKeystore.setValue(key, user, TimeUnit.MINUTES, configProp.getExpiration(), true);
           // jwtRedisKeystore.removeJWTToken(key);
            if (isLogEnabled) {
                logger.info("{}<<:GeneratedToken:[{}]:Token:[{}]", HEADER, (token != null), token);
            }
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:genrateJwtToken:Error:[{}]", HEADER, ExceptionUtils.getRootCauseMessage(e));
            return null;
        } finally {
            createdDate = null;
            claimMap = null;
            token = null;
        }
    }
    
    public boolean tokenLogout(String token) {
        token = token.replace(TOKENPREFIX, "").trim();
        try {
            if (isLogEnabled) {
                logger.info("{}>>:tokenLogout:Request:[{}]", HEADER, (token != null));
            }
            if (token != null && !token.isEmpty()) {
                Claims claims = null;
                claims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token).getBody();
                if (claims != null && claims.containsKey("username")) {
                    String username = claims.get("username").toString();
                    String hash = claims.get("hash").toString().trim();
                    String key = String.format("%s:%s", username, hash)
                            .toUpperCase();
                    jwtRedisKeystore.removeJWTToken(key);
                    return true;
                } else {
                    if (isLogEnabled) {
                        logger.info("{}<<:tokenLogout:UnableToFind:From:Token:[{}]", HEADER, (token != null));
                    }
                    return false;
                }
            } else {
                if (isLogEnabled) {
                    logger.info("{}<<:tokenLogout:UnableToFind:From:Token:[{}]", HEADER, (token != null));
                }
                return false;
            }
        } catch (Exception e) {
            logger.info("{}<<:tokenLogout:UnableToFind:From:Token:[{}]", HEADER, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }
	


    public Authentication validateAuthentication(HttpServletRequest request) {
        //logger.info("{}Enter:validateAuthentication:Request:[{}]", header, request.toString());
        try {
            if (isLogEnabled) {
                logger.info("{}>>:validateAuthentication:Request:[{}]", HEADER, (request != null));
            }
            String token = request.getHeader(HEADERSTRING);
            if (isLogEnabled) {
                logger.info("{}>>:validateAuthentication:Token:[{}]", HEADER, token);
            }
            if (token == null) {
                if (isLogEnabled) {
                    logger.info("{}<<:validateAuthentication:Token:[{}]", HEADER, token);
                }
                return null;
            }
            token = token.replace(TOKENPREFIX, "").trim();
            if (isLogEnabled) {
                logger.info("{}>>:Token:{}", HEADER, token);
            }
            if (token != null && !token.isEmpty()) {
                Claims claims = null;
                try {
                    claims = Jwts.parser()
                            .setSigningKey(SECRET)
                            .parseClaimsJws(token).getBody();
                } catch (Exception e) {
                    logger.error("{}Excep:validateAuthentication:Error:[{}]", HEADER, ExceptionUtils.getRootCauseMessage(e));
                    return null;
                }
                //  System.out.println(":::::::::::::::::::::::::::::claims:::::::::::::::::::::   " + claims);
                if (claims != null && claims.containsKey("username")) {
                    String username = claims.get("username").toString();
                    String hash = claims.get("hash").toString().trim();
                    String existkey = String.format("%s:%s", username, hash)
                                .toUpperCase();
                    JwtUserDto user = (JwtUserDto) jwtRedisKeystore.getValue(HEADER, existkey, JwtUserDto.class);
                    if (user != null) {
                        if (isLogEnabled) {
                            logger.info("{}Key:[{}]:ExpirationTime: [{}]", HEADER, existkey, configProp.getExpiration());
                        }
                        jwtRedisKeystore.setValue(existkey, user, TimeUnit.MINUTES, configProp.getExpiration(), true);
                        AuthenticationTokenImpl auth = new AuthenticationTokenImpl(username, Collections.emptyList());
                        auth.setDetails(new JwtUserDto(username, (Integer) claims.get("userid")));
                        auth.authenticate();
                        if (isLogEnabled) {
                            logger.info("{}<<:Token:{}", HEADER + username, (token != null));
                        }
                        return auth;
                    } else {
                        if (isLogEnabled) {
                            logger.info("{}<<:validateAuthentication:UnableToFind:From:Token:[{}]", HEADER, (token != null));
                        }
                        return new UsernamePasswordAuthenticationToken(null, null);
                    }
                } else {
                    if (isLogEnabled) {
                        logger.info("{}<<:validateAuthentication:UnableToFind:From:Token:[{}]", HEADER, (token != null));
                    }
                    return new UsernamePasswordAuthenticationToken(null, null);
                }
            }
            if (isLogEnabled) {
                logger.info("{}<<:validateAuthentication:Token:[{}]", HEADER, (token != null));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:validateAuthentication:Error:[{}]", HEADER, ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }
}
