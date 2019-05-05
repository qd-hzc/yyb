/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.text.mapper.web;

import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.text.mapper.service.YybTextApiService;
import com.jeeplus.modules.yybtext.entity.YybText;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文案Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "/api/text")
public class YybTextApiController extends BaseController {

	@Autowired
	private YybTextApiService yybTextApiService;


	/**
	 * 文案
	 */
	@IgnoreAuth
	@ResponseBody
	@RequestMapping(value = "/getByKey", method = RequestMethod.GET)
	@ApiOperation(notes = "getByKey", httpMethod = "get", value = "获取文本根据键")
	@ApiImplicitParams({@ApiImplicitParam(name = "key", value = "", required = false, paramType = "query",dataType = "string")})
	public Result list(@RequestParam(required = true) String key) {
		String value;
		YybText yybText = yybTextApiService.getByKey(key);

		if (yybText != null && StringUtils.isNotBlank(yybText.getTextValue())) {
			value = yybText.getTextValue();
		} else {
			value = "后台尚未配置；";
		}
		return ResultUtil.success(value);
	}
	

}