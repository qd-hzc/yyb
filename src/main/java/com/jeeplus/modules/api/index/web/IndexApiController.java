package com.jeeplus.modules.api.index.web;


import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.music.entity.YybMusic;
import com.jeeplus.modules.usage.entity.YybUsage;
import com.jeeplus.modules.usage.service.YybUsageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "/api/member")
public class IndexApiController extends BaseController {


    @Autowired
    YybUsageService yybUsageService;

    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/index")
    @ApiOperation(notes = "index", httpMethod = "GET", value = "首页")
    public Result index(){
        Map<String, Object> resultAll = new HashMap<>();

        List<YybUsage> usageList = yybUsageService.findAllList(new YybUsage());
        resultAll.put("usageAll", usageList);

//        List<YybMusic> musicList = yybUsageService.getIndexMusic();

        return ResultUtil.success();
    }

}
