/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.playhistory.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.music.service.YybMusicApiService;
import com.jeeplus.modules.api.playhistory.service.YybPlayHistoryApiService;
import com.jeeplus.modules.member.entity.YybMember;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.playhistory.entity.YybPlayHistory;
import com.jeeplus.modules.playhistory.service.YybPlayHistoryService;

import static com.jeeplus.modules.sys.interceptor.LogInterceptor.LOGIN_MEMBER;

/**
 * 播放历史Controller
 * @author lwb
 * @version 2019-03-06
 */
@Controller
@RequestMapping(value = "/api/playhistory")
public class YybPlayHistoryApiController extends BaseController {

	@Autowired
	private YybPlayHistoryApiService yybPlayHistoryService;
	@Autowired
	private YybMusicApiService yybMusicService;


	/**
	 * 个人浏览列表页面
	 */
	@ResponseBody
	@RequestMapping(value = "/list")
	@ApiOperation(notes = "list", httpMethod = "GET", value = "个人浏览列表页面")
	@ApiImplicitParams({@ApiImplicitParam(name = "startPage", value = "", required = false, paramType = "query",dataType = "string"),
			@ApiImplicitParam(name = "pageSize", value = "", required = false, paramType = "query",dataType = "string")})

	public Result list(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") String startPage,
					   @RequestParam(required = false, defaultValue = "10") String pageSize) {

		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();
		Map<String, Object> param = new HashMap<>();
		param.put("memberId", memebrId);

		PageHelper.startPage(Integer.parseInt(startPage),Integer.parseInt(pageSize));

		List<YybPlayHistory> list = yybPlayHistoryService.memberPlayHistoryList(param);

		PageInfo<YybPlayHistory> pageInfo = new PageInfo<>(list);

		return ResultUtil.success(pageInfo);
	}

	/**


	 /**
	 * 保存个人浏览
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(notes = "save", httpMethod = "post", value = "保存")
	@ApiImplicitParams({@ApiImplicitParam(name = "musicId", value = "", required = true, paramType = "query",dataType = "string")})
	public Result save(HttpServletRequest request, @RequestParam String musicId) throws Exception{

		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();

		Map<String, Object> param = new HashMap<>();
		param.put("memberId", memebrId);
		param.put("musicId", musicId);


		YybPlayHistory yybPlayHistory = yybPlayHistoryService.getByCondition(param);

		if (yybPlayHistory != null && StringUtils.isNotBlank(yybPlayHistory.getId())){
			yybPlayHistoryService.save(yybPlayHistory);
		} else {
			//保存
			YybPlayHistory yybPlayHistorySave = new YybPlayHistory();
			yybPlayHistorySave.setMemberId(memebrId);
			yybPlayHistorySave.setMusicId(musicId);
			yybPlayHistoryService.save(yybPlayHistorySave);
			//音乐收藏数量+1
		}
		yybMusicService.updateAddPlayHistoryCount(musicId);

		return ResultUtil.success();
	}




}