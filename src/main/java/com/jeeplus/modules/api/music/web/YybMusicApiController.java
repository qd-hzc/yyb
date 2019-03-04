/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.music.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.music.entity.YybMusicVo;
import com.jeeplus.modules.api.music.service.YybMusicApiService;
import com.jeeplus.modules.api.musician.service.YybMusicianApiService;
import com.jeeplus.modules.music.entity.YybMusic;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 音乐Controller
 * @author LWB
 * @version 2019-02-19
 */
@Controller
@RequestMapping(value = "/api/music")
public class YybMusicApiController extends BaseController {

	@Autowired
	private YybMusicApiService yybMusicService;
	@Autowired
	private YybMusicianApiService yybMusicianApiService;



	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getIndexMusicList")
	@ApiOperation(notes = "getIndexMusicList", httpMethod = "GET", value = "首页音乐列表")
	public Result getIndexMusicList(){
		List<YybMusic> list = yybMusicService.getIndexMusicList();
		return ResultUtil.success(list);
	}


	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/searchMusic")
	@ApiOperation(notes = "searchMusic", httpMethod = "POST", value = "音乐搜索")
	@ApiImplicitParams({@ApiImplicitParam(name = "详见代码", value = "详见代码", required = true, paramType = "query",dataType = "RequestBody")})

	public Result searchMusic(@RequestBody YybMusicVo yybMusicVo){

		PageHelper.startPage(yybMusicVo.getStartPage() == null ? 1 : yybMusicVo.getStartPage(), yybMusicVo.getPageSize() == null ? 10 : yybMusicVo.getPageSize());

		List<YybMusic> list = yybMusicService.searchMusic(yybMusicVo);

		PageInfo<YybMusic> pageInfo = new PageInfo<>(list);

		return ResultUtil.success(pageInfo);

	}


	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getAllMusician")
	@ApiOperation(notes = "getAllMusician", httpMethod = "GET", value = "获取所有音乐人列表")
	public Result getAllMusician(){
		return ResultUtil.success(yybMusicianApiService.getAllMusician());
	}

}