/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.shopcart.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.music.service.YybMusicApiService;
import com.jeeplus.modules.api.right.service.YybRightApiService;
import com.jeeplus.modules.api.shopcart.entity.YybShopcartVo;
import com.jeeplus.modules.api.usage.service.YybUsageApiService;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.music.entity.YybMusic;
import com.jeeplus.modules.right.entity.YybRight;
import com.jeeplus.modules.usage.entity.YybUsage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;
import com.jeeplus.modules.api.shopcart.service.YybShopcartApiService;

import static com.jeeplus.modules.sys.interceptor.LogInterceptor.LOGIN_MEMBER;

/**
 * 购物车Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "/api/shopcart")
public class YybShopcartController extends BaseController {

	@Autowired
	private YybShopcartApiService yybShopcartService;
	@Autowired
	private YybRightApiService yybRightApiService;
	@Autowired
	private YybUsageApiService yybUsageApiService;
	@Autowired
	private YybMusicApiService yybMusicApiService;

	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ApiOperation(notes = "/get", httpMethod = "get", value = "获取购物车详情")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query",dataType = "String")})
	public Result get(HttpServletRequest request, @RequestParam(required=true) String id) {
		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();

		YybShopcart entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybShopcartService.get(id);
		}
		if (entity == null || !memebrId.equals(entity.getMemberId())) {
			return ResultUtil.error("获取购物车异常");
		}

		return ResultUtil.success(entity);
	}


	@ResponseBody
	@RequestMapping(value = "/isHave", method = RequestMethod.GET)
	@ApiOperation(notes = "/isHave", httpMethod = "get", value = "判断购物车是否已经拥有该商品，返回 true 或 false")
	@ApiImplicitParams({@ApiImplicitParam(name = "musicId", value = "musicId", required = true, paramType = "query",dataType = "String")})
	public Result isHave(HttpServletRequest request, @RequestParam String musicId) {
		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();

		Map<String,Object> param = new HashMap<>();
		param.put("memberId", memebrId);
		param.put("musicId", musicId);

		int count  = yybShopcartService.getCountByCodition(param);

		return ResultUtil.success(count > 0);
	}

	/**
	 * 个人购物车列表页面
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation(notes = "list", httpMethod = "get", value = "个人购物车页面")
	@ApiImplicitParams({@ApiImplicitParam(name = "startPage", value = "", required = false, paramType = "query",dataType = "string"),
			@ApiImplicitParam(name = "pageSize", value = "", required = false, paramType = "query",dataType = "string")})

	public Result list(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") String startPage,
					   @RequestParam(required = false, defaultValue = "10") String pageSize) {

		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();
		Map<String, Object> param = new HashMap<>();
		param.put("memberId", memebrId);

		PageHelper.startPage(Integer.parseInt(startPage),Integer.parseInt(pageSize));

		List<YybShopcart> list = yybShopcartService.shopcartList(param);

		PageInfo<YybShopcart> pageInfo = new PageInfo<>(list);

		return ResultUtil.success(pageInfo);
	}

	/**
	 * 保存购物车
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(notes = "/save", httpMethod = "post", value = "保存购物车")
	@ApiImplicitParams({@ApiImplicitParam(name = "yybShopcartVo", value = "yybShopcartVo", required = true, paramType = "body",dataType = "body")})
	public Result save(HttpServletRequest request, @RequestBody YybShopcartVo yybShopcartVo,
					   BindingResult bindingResult) throws Exception{

		List<String> rightSelectName = new ArrayList<>();
		List<String> usageSelectName = new ArrayList<>();
		Result result = this.validShopcartParam(yybShopcartVo, bindingResult, rightSelectName, usageSelectName);
		if (!"0000".equals(result.getCode())) {
			return result;
		}


		YybShopcart yybShopcart = new YybShopcart();
		yybShopcart.setMusicId(yybShopcartVo.getMusicId());
		yybShopcart.setMusicPrice(yybShopcartVo.getMusicPrice().doubleValue());
		yybShopcart.setRightSelect(StringUtils.join(yybShopcartVo.getRightSelectList().toArray(), ","));
		yybShopcart.setRightSelectName(StringUtils.join(rightSelectName.toArray(), "/"));
		yybShopcart.setUsageSelect(StringUtils.join(yybShopcartVo.getUsageSelectList().toArray(), ","));
		yybShopcart.setUsageSelectName(StringUtils.join(usageSelectName.toArray(), "/"));
		yybShopcart.setMusicTotal(yybShopcartVo.getMusicTotal().doubleValue());

		YybMusic yybMusic = yybMusicApiService.get(yybShopcartVo.getMusicId());
		yybShopcart.setMusicTitle(yybMusic.getTitle());
		yybShopcart.setMusicianName(yybMusic.getYybMusician().getName());

		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
		yybShopcart.setMemberId(yybMember.getId());

		yybShopcartService.save(yybShopcart);

		return ResultUtil.success();
	}

	
	/**
	 * 批量删除购物车
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll", method = RequestMethod.POST)
	@ApiOperation(notes = "/deleteAll", httpMethod = "post", value = "删除多个")
	@ApiImplicitParams({@ApiImplicitParam(name = "删除", value = "deleteAll", required = true, paramType = "body",dataType = "String")})
	public Result deleteAll(HttpServletRequest request, @RequestBody List<String> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return ResultUtil.error("ids不能为空");
		}
		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);


		List<YybShopcart> yybShopcartList = yybShopcartService.getListByIds(ids);
		for (YybShopcart yybShopcart : yybShopcartList) {
			if (!yybMember.getId().equals(yybShopcart.getMemberId())) {
				return ResultUtil.error("数据异常");
			}
		}

		for(String id : ids){
			yybShopcartService.delete(yybShopcartService.get(id));
		}
		return ResultUtil.success();
	}




	/**
	 * 参数校验
	 * @param yybShopcartVo
	 * @param bindingResult
	 * @return
	 */
	public Result validShopcartParam(YybShopcartVo yybShopcartVo, BindingResult bindingResult, List<String> rightSelectName, List<String> usageListName){
		if (yybShopcartVo == null) {
			return ResultUtil.error("参数不能为空");
		}
		//校验参数,出错抛出异常
		if (bindingResult.hasErrors()) {
			logger.error("参数错误.(" + bindingResult.getFieldError().getDefaultMessage() + ")");
			return ResultUtil.error("参数错误.(" + bindingResult.getFieldError().getDefaultMessage() + ")");
		}


		if (BigDecimal.ZERO.compareTo(yybShopcartVo.getMusicPrice()) == 0 ||
				BigDecimal.ZERO.compareTo(yybShopcartVo.getMusicTotal()) == 0) {
			return ResultUtil.error("音乐价格或者总额不能为0");
		}


		YybMusic yybMusic = yybMusicApiService.get(yybShopcartVo.getMusicId());
		if (yybMusic == null || StringUtils.isBlank(yybMusic.getId())) {
			return ResultUtil.error("获取音乐失败");
		}

		BigDecimal musicPrice = yybShopcartVo.getMusicPrice();
		if (musicPrice.compareTo(BigDecimal.valueOf(yybMusic.getPrice())) != 0) {
			return ResultUtil.error("音乐价格异常");
		}

		List<YybRight> rightList = yybRightApiService.getListByIds(yybShopcartVo.getRightSelectList());
		List<YybUsage> usageList = yybUsageApiService.getListByIds(yybShopcartVo.getUsageSelectList());
		if (yybShopcartVo.getRightSelectList().size() != rightList.size() ||
				yybShopcartVo.getUsageSelectList().size() != usageList.size()) {
			return ResultUtil.error("获取权利或用途异常");
		}

		BigDecimal rightTotal = BigDecimal.ZERO;
		for (YybRight right : rightList) {
			rightSelectName.add(right.getName());
			rightTotal = rightTotal.add(BigDecimal.valueOf(right.getRate()));
		}
		BigDecimal usageTotal = BigDecimal.ZERO;
		for (YybUsage yybUsage : usageList) {
			usageListName.add(yybUsage.getName());
			usageTotal = usageTotal.add(BigDecimal.valueOf(yybUsage.getRate()));
		}

		if (BigDecimal.ZERO.compareTo(rightTotal) == 0 || BigDecimal.ZERO.compareTo(usageTotal) == 0) {
			return ResultUtil.error("选择权利或用途比率异常");
		}

		BigDecimal musicTotal = (rightTotal.add(usageTotal)).multiply(musicPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

		if (musicTotal.compareTo(yybShopcartVo.getMusicTotal()) != 0) {
			return ResultUtil.error("总额不符");
		}

		return ResultUtil.success();
	}



}