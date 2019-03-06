/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.like.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.like.service.YybLikeApiService;
import com.jeeplus.modules.api.music.service.YybMusicApiService;
import com.jeeplus.modules.like.entity.YybLike;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.music.service.YybMusicService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeeplus.modules.sys.interceptor.LogInterceptor.LOGIN_MEMBER;

/**
 * 个人喜欢Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "/api/like")
public class YybLikeApiController extends BaseController {

	@Autowired
	private YybLikeApiService yybLikeService;
	@Autowired
	private YybMusicApiService yybMusicService;
	

	/**
	 * 个人喜欢列表页面
	 */
	@ResponseBody
	@RequestMapping(value = "/memberLikeList")
	@ApiOperation(notes = "memberLikeList", httpMethod = "POST", value = "个人喜欢列表页面")
	@ApiImplicitParams({@ApiImplicitParam(name = "startPage", value = "", required = false, paramType = "query",dataType = "string"),
			@ApiImplicitParam(name = "pageSize", value = "", required = false, paramType = "query",dataType = "string")})

	public Result list(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") String startPage,
					   @RequestParam(required = false, defaultValue = "10") String pageSize) {

		YybMember yybMember = (YybMember)request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();
		Map<String, Object> param = new HashMap<>();
		param.put("memberId", memebrId);

		PageHelper.startPage(Integer.parseInt(startPage),Integer.parseInt(pageSize));

		List<YybLike> list = yybLikeService.memberLikeList(param);



		PageInfo<YybLike> pageInfo = new PageInfo<>(list);

		return ResultUtil.success(pageInfo);
	}
	
		/**


	/**
	 * 保存个人喜欢
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(notes = "save", httpMethod = "post", value = "保存")
	@ApiImplicitParams({@ApiImplicitParam(name = "musicId", value = "", required = true, paramType = "query",dataType = "string")})
	public Result save(HttpServletRequest request, @RequestParam String musicId) throws Exception{

		YybMember yybMember = (YybMember)request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();

		Map<String, Object> param = new HashMap<>();
		param.put("memberId", memebrId);
		param.put("musicId", musicId);


		YybLike yybLike = yybLikeService.getByCondition(param);

		if (yybLike != null && StringUtils.isNotBlank(yybLike.getId())){
			//更新
//			yybLikeService.save(yybLike);
		} else {
			//保存
			YybLike yybLikeSave = new YybLike();
			yybLikeSave.setMemberId(memebrId);
			yybLikeSave.setMusicId(musicId);
			yybLikeService.save(yybLikeSave);
			//音乐喜欢数量+1
			yybMusicService.updateAddLikeCount(musicId);
		}

		return ResultUtil.success();
	}


	/**
	 * 取消个人喜欢
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ApiOperation(notes = "cancel", httpMethod = "post", value = "取消")
	@ApiImplicitParams({@ApiImplicitParam(name = "musicId", value = "", required = true, paramType = "query",dataType = "string")})
	public Result cancel(HttpServletRequest request, @RequestParam String musicId) throws Exception{

		YybMember yybMember = (YybMember)request.getAttribute(LOGIN_MEMBER);
		String memebrId = yybMember.getId();

		Map<String, Object> param = new HashMap<>();
		param.put("memberId", memebrId);
		param.put("musicId", musicId);


		YybLike yybLike = yybLikeService.getByCondition(param);

		if (yybLike != null && StringUtils.isNotBlank(yybLike.getId())){
			//删除
			yybLikeService.delete(yybLike);
		} else { ;
			//音乐喜欢数量-1
			yybMusicService.updateReduceLikeCount(musicId);
		}

		return ResultUtil.success();
	}

}