/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.music.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.music.entity.YybMusicVo;
import com.jeeplus.modules.api.music.service.YybMusicApiService;
import com.jeeplus.modules.api.musician.service.YybMusicianApiService;
import com.jeeplus.modules.music.entity.YybMusic;
import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;
import com.jeeplus.modules.tagcatetory.service.YybTagCategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	@Autowired
	private YybTagCategoryService tagCategoryService;



	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/searchMusic")
	@ApiOperation(notes = "searchMusic", httpMethod = "POST", value = "音乐搜索")
	@ApiImplicitParams({@ApiImplicitParam(name = "参数详见代码", value = "参数详见代码", required = true, paramType = "query",dataType = "RequestBody")})

	public Result searchMusic(@RequestBody YybMusicVo yybMusicVo){


		String type = yybMusicVo.getType();
		String mode = yybMusicVo.getMode();
		if (StringUtils.isEmpty(type)) {
			type = "3";
		}
		if (StringUtils.isEmpty(mode)) {
			mode = "desc";
		}

		if (!("1".equals(type) || "2".equals(type) || "3".equals(type))){
			return ResultUtil.error("类型有误");
		}


		if (!("asc".equals(mode) || "desc".equals(mode))){
			return ResultUtil.error("排序方式有误");
		}


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


	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getAllTag")
	@ApiOperation(notes = "getAllTag", httpMethod = "GET", value = "获取所有标签, 子父")
	public Result getAllTag(){

		List<YybTagCategory> yybTagCategories = tagCategoryService.findAllList(new YybTagCategory());
		List<YybTagCategory> result = new ArrayList<>();
		for (YybTagCategory yybTagCategory : yybTagCategories) {
			if ("0".equals(yybTagCategory.getParentId())) {
				result.add(yybTagCategory);
			}
		}


		for (YybTagCategory yybTagCategory : result) {
			List<YybTagCategory> child = new ArrayList<>();
			for (YybTagCategory yybTagCategoryAll : yybTagCategories) {
				if (yybTagCategory.getId().equals(yybTagCategoryAll.getParentId())) {
					child.add(yybTagCategoryAll);
				}
			}
			yybTagCategory.setList(child);
		}

		return ResultUtil.success(result);
	}



}