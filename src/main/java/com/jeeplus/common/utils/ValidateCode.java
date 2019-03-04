package com.jeeplus.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenbin on 2017/12/13.
 */
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = 6486374913813791101L;

    private final static int nextActionTimeSecond = 60;

    private final static int frequentlySecond = 5;

    public static final  String SESSION_KEY_FOR_CODE_SMS = "SESSION_KEY_FOR_CODE_SMS";
    public static final  String SESSION_KEY_FOR_VALID = "SESSION_KEY_FOR_VALID";

    private String code;

    private LocalDateTime expireTime;

    private LocalDateTime currentTime;

    private String contactWay;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public ValidateCode(String contactWay, String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
        this.contactWay = contactWay;
        this.currentTime = LocalDateTime.now();
    }

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     * 判断是否过期
     *
     * @return
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }


    /**
     * 判断相同联系方式否重复操作
     *
     * @return
     */
    public boolean isRepeat(String mobile) {
        if (mobile.equals(this.getContactWay())) {
            return LocalDateTime.now().isBefore(currentTime.plusSeconds(nextActionTimeSecond));
        } else {
            return false;
        }
    }

    /**
     * 判断频繁操作
     *
     * @return
     */
    public boolean isFrequently() {
        return LocalDateTime.now().isBefore(currentTime.plusSeconds(frequentlySecond));
    }


    /**
     * 校验短信验证码公共方法
     *
     * @param session
     * @param code
     * @return
     */
    public static Map<String, Object> validateSmsPhoneCode(HttpSession session, String code, String phone) {
        //定义返回map
        Map<String, Object> resultMap = new HashMap<>();
        //定义最后返回的msg
        resultMap.put("pass", false);
        //从session中取出短信验证码信息
        ValidateCode validateCode = (ValidateCode) session.getAttribute(SESSION_KEY_FOR_CODE_SMS);
        //进行验证码校验操作
        if (validateCode == null) {
            resultMap.put("msg", "请先获取手机验证码");
            return resultMap;
        }
        if (validateCode.isExpired()) {
            //session过期，直接移除
            session.removeAttribute(SESSION_KEY_FOR_CODE_SMS);
            resultMap.put("msg", "验证码已过期，请重新发送");
            return resultMap;
        }
        if (!phone.equals(validateCode.getContactWay())) {
            resultMap.put("msg", "手机号验证失败");
            return resultMap;
        }

        if (!StringUtils.equals(validateCode.getCode(), code)) {
            resultMap.put("msg", "验证不通过");
            return resultMap;
        } else {
            //验证通过后，删除session保存的code，先不删除，为了以后可以在验证
            resultMap.put("pass", true);
            resultMap.put("msg", "验证通过");
            return resultMap;
        }
    }

}
