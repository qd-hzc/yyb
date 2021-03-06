/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.music.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.music.entity.YybMusicVo;
import com.jeeplus.modules.api.music.service.YybMusicApiService;
import com.jeeplus.modules.api.musician.service.YybMusicianApiService;
import com.jeeplus.modules.api.right.service.YybRightApiService;
import com.jeeplus.modules.api.tagcatetory.service.YybTagCategoryApiService;
import com.jeeplus.modules.api.usage.service.YybUsageApiService;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.music.entity.YybMusic;
import com.jeeplus.modules.musician.entity.YybMusician;
import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;
import com.jeeplus.modules.tagcatetory.service.YybTagCategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeeplus.modules.sys.interceptor.LogInterceptor.LOGIN_MEMBER;

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
	private YybTagCategoryApiService tagCategoryService;
	@Autowired
	private YybUsageApiService yybUsageApiService;
	@Autowired
	private YybRightApiService yybRightApiService;


	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/searchMusic", method = RequestMethod.POST)
	@ApiOperation(notes = "searchMusic", httpMethod = "POST", value = "音乐搜索")
	@ApiImplicitParams({@ApiImplicitParam(name = "参数详见代码", value = "参数详见代码", required = true, paramType = "query",dataType = "RequestBody")})

	public Result searchMusic(HttpServletRequest request, @RequestBody YybMusicVo yybMusicVo){

		//是否喜欢
		Object o = request.getAttribute(LOGIN_MEMBER);
		if (o != null) {
			YybMember yybMember = (YybMember) o;
			if (yybMember != null && yybMember.getId() != null) {
				yybMusicVo.setMemberId(yybMember.getId());
			}
		}

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
	@RequestMapping(value = "/getAllMusician", method = RequestMethod.GET)
	@ApiOperation(notes = "getAllMusician", httpMethod = "GET", value = "获取所有音乐人列表")
	public Result getAllMusician(){
		return ResultUtil.success(yybMusicianApiService.getAllMusician(new HashMap<>()));
	}


	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getAllTag", method = RequestMethod.GET)
	@ApiOperation(notes = "getAllTag", httpMethod = "GET", value = "获取所有标签, 子父")
	public Result getAllTag(){

		List<YybTagCategory> yybTagCategories = tagCategoryService.findAll(new YybTagCategory());
		List<YybTagCategory> result = new ArrayList<>();
		for (YybTagCategory yybTagCategory : yybTagCategories) {
			if ("0".equals(yybTagCategory.getParent_Id())) {
				result.add(yybTagCategory);
			}
		}


		for (YybTagCategory yybTagCategory : result) {
			List<YybTagCategory> child = new ArrayList<>();
			for (YybTagCategory yybTagCategoryAll : yybTagCategories) {
				if (yybTagCategory.getId().equals(yybTagCategoryAll.getParent_Id())) {
					child.add(yybTagCategoryAll);
				}
			}
			yybTagCategory.setList(child);
			if (!CollectionUtils.isEmpty(child)) {
				yybTagCategory.setHasChildren(true);
			}
		}

		return ResultUtil.success(result);
	}

	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getTagData")
	public Map<String, Object> getTagData(@RequestParam(required = false) String name, @RequestParam(required = false) String parentName) {

		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("parentName", parentName);

		List<YybTagCategory> list = tagCategoryService.getTagData(map);
		Page page = new Page();
		page.setList(list);
		return getBootstrapData(page);
	}



	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getAllRight", method = RequestMethod.GET)
	@ApiOperation(notes = "getAllRight", httpMethod = "GET", value = "获取所有权利")
	public Result getAllRight(){
		return ResultUtil.success(yybRightApiService.getAll());
	}


	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getAllUsage", method = RequestMethod.GET)
	@ApiOperation(notes = "getAllUsage", httpMethod = "GET", value = "获取所有用途")
	public Result getAllUsage(){
		return ResultUtil.success(yybUsageApiService.getAll());
	}


	@IgnoreAuth
	@RequestMapping(value = "/down", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(notes = "down", httpMethod = "get", value = "文件上传")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query",dataType = "string")})
	public void downMusic(HttpServletRequest request, HttpServletResponse response, String id) {
		YybMusic yybMusic = yybMusicService.get(id);
		if (yybMusic !=null) {
			FileUtils.downFile(Global.getConfig("download.basedir")+ yybMusic.getUrl(), yybMusic.getTitle()+".mp3", response);
		}else {
			FileUtils.downFile("文件未找到", "文件未找到", response);
		}
	}


}