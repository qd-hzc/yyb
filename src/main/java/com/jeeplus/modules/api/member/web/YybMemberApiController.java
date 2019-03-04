package com.jeeplus.modules.api.member.web;


import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.common.json.TokenEntity;
import com.jeeplus.common.utils.*;
import com.jeeplus.common.utils.text.TextValidator;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.api.member.service.YybMemberApiService;
import com.jeeplus.modules.sys.service.SystemService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 会员Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "/api/member")
public class YybMemberApiController extends BaseController {

    @Autowired
    private YybMemberApiService yybMemberApiService;

    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/sendSms")
    @ApiOperation(notes = "sendSms", httpMethod = "POST", value = "短信发送")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型，1：重置密码", required = false, paramType = "query",dataType = "string")})
    public Result sendSms(HttpSession httpSession, String phone, @RequestParam(required = false) String type) {

        if ("1".equals(type)) {
            YybMember yybMember = yybMemberApiService.getByLoginName(phone);
            if (yybMember == null) {
                return ResultUtil.error("获取用户失败");
            }
        }
        //验证码位数  是否含有字母  是否含有数字
        String code= RandomStringUtils.random(6, false, true);

        Boolean smsMsg = SmsUtils.sendSms(phone, code, "3", "1");
        if (smsMsg == false) {
            return ResultUtil.error("短信发送失败");
        }
        ValidateCode validateCode =  new ValidateCode(phone, code,180);
        httpSession.setAttribute(ValidateCode.SESSION_KEY_FOR_CODE_SMS, validateCode);
        return ResultUtil.success();
    }



    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/validSms")
    @ApiOperation(notes = "validSms", httpMethod = "POST", value = "短信校验")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",dataType = "string")})
    public Result validSms(HttpSession httpSession, @RequestParam String phone, @RequestParam  String code) {
        Map<String,Object> smsMap = ValidateCode.validateSmsPhoneCode(httpSession, code, phone);
        if (!true == (Boolean) smsMap.get("pass")) {
            return ResultUtil.error(smsMap.get("msg").toString());
        }else {
            return ResultUtil.success();
        }
    }

    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/register",  method = RequestMethod.POST)
    @ApiOperation(notes = "register", httpMethod = "POST", value = "用户注册")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "name", value = "账号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query",dataType = "string")})
    public Result register(HttpSession httpSession, @RequestParam String phone, @RequestParam String code, @RequestParam String name, @RequestParam String password) {
        Map<String,Object> smsMap = ValidateCode.validateSmsPhoneCode(httpSession, code, phone);
        if (!true == (Boolean) smsMap.get("pass")) {
            return ResultUtil.error(smsMap.get("msg").toString());
        }

        if (TextValidator.isMobileExact(name)){
            return ResultUtil.error("注册用户名不能为手机号");

        }

        YybMember yybMember = new YybMember();
        yybMember.setPhone(phone);
        yybMember.setName(name);
        yybMember.setPassword(SystemService.entryptPassword(password));
        yybMember.setDelFlag("0");
        yybMemberApiService.save(yybMember);
        return ResultUtil.success();
    }

    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(notes = "login", httpMethod = "POST", value = "用户登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "loginName", value = "用户名", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query",dataType = "string")})

    public Result login(@RequestParam String loginName, @RequestParam String password) {
        YybMember yybMember = yybMemberApiService.getByLoginName(loginName);
        if (yybMember == null) {
            return ResultUtil.error("获取用户失败");
        }

        if (!SystemService.entryptPassword (password).equals(yybMember.getPassword())) {
            return ResultUtil.error("用户或密码错误");
        }

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setExpireTime(new Date(System.currentTimeMillis() + 12 * 3600 * 1000));
        tokenEntity.setMember(yybMember);
        //缓存 key为memberId
        CacheUtils.put(yybMember.getId(), tokenEntity);
        yybMember.setToken(yybMember.getId());
        return ResultUtil.success(yybMember);
    }




    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    @ApiOperation(notes = "resetPass", httpMethod = "POST", value = "重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query",dataType = "string")})
    public Result resetPass(@RequestParam String phone, @RequestParam String password) {
        YybMember yybMember = yybMemberApiService.getByLoginName(phone);
        if (yybMember == null) {
            return ResultUtil.error("获取用户失败");
        }

        YybMember resetYybMember = new YybMember();
        resetYybMember.setId(yybMember.getId());
        resetYybMember.setPassword(SystemService.entryptPassword(password));
        yybMemberApiService.updatePass(resetYybMember);
        return ResultUtil.success();
    }


    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/mobileLogin", method = RequestMethod.POST)
    @ApiOperation(notes = "mobileLogin", httpMethod = "POST", value = "重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",dataType = "string")})
    public Result mobileLogin(HttpSession httpSession, @RequestParam String phone, @RequestParam String code) {

        Map<String, Object> smsMap = ValidateCode.validateSmsPhoneCode(httpSession, code, phone);
        if (!true == (Boolean) smsMap.get("pass")) {
            return ResultUtil.error(smsMap.get("msg").toString());
        }


        YybMember yybMember = yybMemberApiService.getByLoginName(phone);
        if (yybMember == null) {
            return ResultUtil.error("获取用户失败");
        }

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setExpireTime(new Date(System.currentTimeMillis() + 12 * 3600 * 1000));
        tokenEntity.setMember(yybMember);
        //缓存 key为memberId
        CacheUtils.put(yybMember.getId(), tokenEntity);
        yybMember.setToken(yybMember.getId());
        return ResultUtil.success(yybMember);

    }
}
