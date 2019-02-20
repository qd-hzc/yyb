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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
    public Result sendSms(HttpSession httpSession, String phone) {


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
    @RequestMapping(value = "/register",  method = RequestMethod.POST)
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
        yybMember.setPassword(MD5Util.MD5(password));
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

        if (!MD5Util.MD5(password).equals(yybMember.getPassword())) {
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
}
