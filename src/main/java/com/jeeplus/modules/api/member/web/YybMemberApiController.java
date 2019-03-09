package com.jeeplus.modules.api.member.web;


import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.*;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.member.entity.ApiMember;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.api.member.service.YybMemberApiService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.SystemAuthorizingRealm;
import com.jeeplus.modules.sys.utils.UserUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.jeeplus.common.utils.ValidateCode.SESSION_KEY_FOR_CODE_SMS;
import static com.jeeplus.modules.sys.interceptor.LogInterceptor.LOGIN_MEMBER;

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
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    @ApiOperation(notes = "sendSms", httpMethod = "POST", value = "短信发送")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型，1:注册， 2：重置密码 3:手机号登陆", required = true, paramType = "query",dataType = "string")})
    public Result sendSms(HttpSession httpSession, String phone, @RequestParam String type) {

        if (!("1".equals(type) || "2".equals(type) || "3".equals(type))){
            return ResultUtil.error("类型有误");
        }

        if ("2".equals(type) || "3".equals(type)) {
            YybMember yybMember = yybMemberApiService.getByLoginName(phone);
            if (yybMember == null) {
                return ResultUtil.error("获取用户失败");
            }

        }
        if ("1".equals(type)) {
            YybMember yybMember = yybMemberApiService.getByLoginName(phone);
            if (yybMember != null) {
                return ResultUtil.error("用户已存在");
            }

        }
        //验证码位数  是否含有字母  是否含有数字
        String code= RandomStringUtils.random(6, false, true);

        Boolean smsMsg = SmsUtils.sendSms(phone, code, "3", "1");
        if (smsMsg == false) {
            return ResultUtil.error("短信发送失败");
        }
        ValidateCode validateCode =  new ValidateCode(phone, code,180);
        httpSession.setAttribute(SESSION_KEY_FOR_CODE_SMS, validateCode);

        return ResultUtil.success();
    }



    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/validSms", method = RequestMethod.POST)
    @ApiOperation(notes = "validSms", httpMethod = "POST", value = "重置密码下一步之前短信校验")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",dataType = "string")})
    public Result validSms(HttpSession httpSession, @RequestParam String phone, @RequestParam  String code) {
        Map<String,Object> smsMap = ValidateCode.validateSmsPhoneCode(httpSession, code, phone);
        if (!true == (Boolean) smsMap.get("pass")) {
            return ResultUtil.error(smsMap.get("msg").toString());
        }

        YybMember yybMember = yybMemberApiService.getByLoginName(phone);
        if (yybMember == null) {
            return ResultUtil.error("获取用户失败");
        }

        String validCode = UUID.randomUUID().toString();

        httpSession.setAttribute(phone, validCode);
        return ResultUtil.success(validCode);
    }

    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/register",  method = RequestMethod.POST)
    @ApiOperation(notes = "register", httpMethod = "POST", value = "用户注册")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query",dataType = "string")})
    public Result register(HttpSession httpSession, @RequestParam String phone, @RequestParam String code, @RequestParam String password) {
        Map<String,Object> smsMap = ValidateCode.validateSmsPhoneCode(httpSession, code, phone);
        if (!true == (Boolean) smsMap.get("pass")) {
            return ResultUtil.error(smsMap.get("msg").toString());
        }

        YybMember getyybMember = yybMemberApiService.getByLoginName(phone);
        if (getyybMember != null) {
            return ResultUtil.error("该用户已存在");
        }


        YybMember yybMember = new YybMember();
        yybMember.setPhone(phone);
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

    public Result login(@RequestParam String loginName, @RequestParam String password) throws Exception{
        YybMember yybMember = yybMemberApiService.getByLoginName(loginName);
        if (yybMember == null) {
            return ResultUtil.error("获取用户失败");
        }

        if (!MD5Util.MD5(password).equals(yybMember.getPassword())) {
            return ResultUtil.error("用户或密码错误");
        }

        ApiMember apiMember = new ApiMember();

        apiMember.setId(yybMember.getId());
        apiMember.setPhone(yybMember.getPhone());
        //给用户jwt加密生成token
        String token = JWT.sign(apiMember, 12L * 60L * 60L * 1000L);
        yybMember.setToken(token);
        return ResultUtil.success(yybMember);
    }



    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    @ApiOperation(notes = "resetPass", httpMethod = "POST", value = "重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "validCode", value = "校验码", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query",dataType = "string")})
    public Result resetPass(HttpSession session, @RequestParam String validCode, @RequestParam String phone, @RequestParam String password) {
        YybMember yybMember = yybMemberApiService.getByLoginName(phone);
        if (yybMember == null) {
            return ResultUtil.error("获取用户失败");
        }

        if (session.getAttribute(phone) == null) {
            return ResultUtil.error("请先获取验证码");
        }
        if (!validCode.equals(session.getAttribute(phone).toString())) {
            return ResultUtil.error("请先获取验证码");
        }

        YybMember resetYybMember = new YybMember();
        resetYybMember.setId(yybMember.getId());
        resetYybMember.setPassword(MD5Util.MD5(password));
        yybMemberApiService.updatePass(resetYybMember);
        return ResultUtil.success();
    }


    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/mobileLogin", method = RequestMethod.POST)
    @ApiOperation(notes = "mobileLogin", httpMethod = "POST", value = "手机号登陆")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",dataType = "string")})
    public Result mobileLogin(HttpSession httpSession, @RequestParam String phone, @RequestParam String code) throws Exception{

        Map<String, Object> smsMap = ValidateCode.validateSmsPhoneCode(httpSession, code, phone);
        if (!true == (Boolean) smsMap.get("pass")) {
            return ResultUtil.error(smsMap.get("msg").toString());
        }


        YybMember yybMember = yybMemberApiService.getByLoginName(phone);
        if (yybMember == null) {
            return ResultUtil.error("获取用户失败");
        }

        yybMember.setPassword(null);
        ApiMember apiMember = new ApiMember();

        apiMember.setId(yybMember.getId());
        apiMember.setPhone(yybMember.getPhone());

        //给用户jwt加密生成token
        String token = JWT.sign(apiMember, 12L * 60L * 60L * 1000L);
        yybMember.setToken(token);
        return ResultUtil.success(yybMember);

    }



    @ResponseBody
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ApiOperation(notes = "getToken", httpMethod = "get", value = "获取token信息")
    public Result getToken(HttpServletRequest request) {
        YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
        return ResultUtil.success(yybMember);

    }


    @ResponseBody
    @RequestMapping(value = "/bindTags", method = RequestMethod.POST)
    @ApiOperation(notes = "bindTags", httpMethod = "POST", value = "绑定tags， 英文逗号分隔id: 1,2,3,4")
    @ApiImplicitParams({@ApiImplicitParam(name = "tags", value = "tags， 英文逗号分隔id: 1,2,3,4", required = true, paramType = "form",dataType = "string")})
    public Result bindTag(HttpServletRequest request, @RequestParam String tags){

        YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
        yybMember = yybMemberApiService.get(yybMember.getId());
        if (StringUtils.isNotBlank(yybMember.getTags())) {
            return ResultUtil.error("已经绑定");
        }

        Map<String,Object> param = new HashMap<>();
        param.put("tags",tags);
        param.put("memberId",yybMember.getId());
        yybMemberApiService.bindTags(param);
        return ResultUtil.success();
    }



    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(notes = "upload", httpMethod = "POST", value = "文件上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "file", required = true, paramType = "form",dataType = "file")})
    public Result webupload(HttpServletRequest request, MultipartFile file) throws IllegalStateException, IOException {
        String uploadPath = request.getParameter("uploadPath");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) UserUtils.getPrincipal();
        String fileUrl = Global.getAttachmentUrl()+uploadPath+"/"+year+"/"+month+"/";
        String fileDir = Global.getAttachmentDir()+uploadPath+"/"+year+"/"+month+"/";
        String url = "";
        // 判断文件是否为空
        if (!file.isEmpty()) {
            // 文件保存路径
            // 转存文件
            FileUtils.createDirectory(fileDir);
            String name = file.getOriginalFilename();
            String filePath = fileDir +  name;
            File newFile = FileUtils.getAvailableFile(filePath,0);
            file.transferTo(newFile);

            url = fileUrl+ newFile.getName();
        }
        return ResultUtil.success(url);
    }



}
