/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.common.utils.JWT;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.member.entity.ApiMember;
import com.jeeplus.modules.api.member.service.YybMemberApiService;
import com.jeeplus.modules.member.entity.YybMember;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.core.service.BaseService;
import com.jeeplus.modules.sys.utils.LogUtils;

/**
 * 日志拦截器
 * @author jeeplus
 * @version 2017-8-19
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor {

	public static final String LOGIN_MEMBER = "LOGIN_MEMBER";

	@Autowired
	private YybMemberApiService yybMemberApiService;

	private static final ThreadLocal<Long> startTimeThreadLocal =
			new NamedThreadLocal<Long>("ThreadLocal StartTime");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		if (logger.isDebugEnabled()){
			long beginTime = System.currentTimeMillis();//1、开始时间  
	        startTimeThreadLocal.set(beginTime);		//线程绑定变量（该数据只有当前请求的线程可见）  
	        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
	        	.format(beginTime), request.getRequestURI());
		}

		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.setHeader("Access-Control-Allow-Credentials","true"); //是否支持cookie跨域

		if (!request.getRequestURI().contains("/api/")) {
			return true;
		}
		IgnoreAuth annotation;
		if(handler instanceof HandlerMethod) {
			annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
		}else{
			return true;
		}


		//从header中获取token
		String token = request.getHeader("token");
		//如果header中不存在token，则从参数中获取token
		if(StringUtils.isBlank(token)){
			token = request.getParameter("token");
		}

		//如果有@IgnoreAuth注解，则不验证token
		if(annotation != null){

			//如果传了token， 取出用户信息
			if (StringUtils.isNotBlank(token)) {
				ApiMember apiMember = JWT.unsign(token, ApiMember.class);
				if (apiMember != null) {
					//校验数据库中的用户信息
					YybMember yybMember1 = new YybMember();
					BeanUtils.copyProperties(yybMember1, apiMember);
					//设置userId到request里，后续根据userId，获取用户信息
					request.setAttribute(LOGIN_MEMBER, yybMember1);
				}
			}

			return true;
		}


		//token为空
		if(StringUtils.isBlank(token)){
			returnResponse(response, "token不能为空");
		}

		ApiMember apiMember = JWT.unsign(token, ApiMember.class);

		if (apiMember == null) {
			returnResponse(response, "token失效", "1111");
		}

		String memberId = request.getParameter("memberId");
		if (StringUtils.isNotBlank(memberId)) {
			if (!memberId.equals(apiMember.getId())) {
				returnResponse(response, "登陆信息有误");
			}
		}

		//校验数据库中的用户信息
		YybMember yybMember1 = yybMemberApiService.get(apiMember.getId());
		if (yybMember1 == null) {
			returnResponse(response, "登陆信息有误");
		}


		//设置userId到request里，后续根据userId，获取用户信息
		request.setAttribute(LOGIN_MEMBER, yybMember1);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null){
			logger.info("ViewName: " + modelAndView.getViewName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {

		// 保存日志
		LogUtils.saveLog(request, handler, ex, null);
		
		// 打印JVM信息。
		if (logger.isDebugEnabled()){
			long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
	        logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
	        		new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), DateUtils.formatDateTime(endTime - beginTime),
					request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024, 
					(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024); 
		}
		
	}


	public void returnResponse(HttpServletResponse response, String msg, String code){

		Result result = ResultUtil.error(code,msg);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(JSON.toJSONString(result));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}


	public void returnResponse(HttpServletResponse response, String msg){

		Result result = ResultUtil.error(msg);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(JSON.toJSONString(result));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
