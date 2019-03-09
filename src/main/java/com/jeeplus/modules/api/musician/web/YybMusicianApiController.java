/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.musician.web;

import com.jeeplus.common.annotation.IgnoreAuth;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.HanyuPinyinHelper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.musician.entity.YybMusicianVo;
import com.jeeplus.modules.api.musician.service.YybMusicianApiService;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.musician.entity.YybMusician;
import com.jeeplus.modules.musician.service.YybMusicianService;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.sysparam.service.SysParamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static com.jeeplus.modules.sys.interceptor.LogInterceptor.LOGIN_MEMBER;

/**
 * 音乐人Controller
 * @author lwb
 * @version 2019-02-23
 */
@Controller
@RequestMapping(value = "/api/musician/")
public class YybMusicianApiController extends BaseController {

	@Autowired
	private YybMusicianApiService yybMusicianApiService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SysParamService sysParamService;
	

    /**
     * 通过音乐人
     */
    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "pass")
    public AjaxJson pass(String id) {
        AjaxJson j = new AjaxJson();
        YybMusician yybMusician = yybMusicianApiService.get(id);

		Office parent = new Office();
		parent.setId(Global.getConfig("office.music"));
		User createUser = new User();
		createUser.setId("1");

		String officeId = UUID.randomUUID().toString().replace("-","");
		Office office = new Office();
		office.setArea(new Area("a9beb8c645ff448d89e71f96dc97bc09"));
		office.setParent(parent);
		office.setParentIds("0,1");
		office.setId(officeId);
		office.setCreateBy(createUser);
		office.setCreateDate(new Date());
		office.setUpdateBy(createUser);
		office.setUpdateDate(new Date());
		office.setName("音乐人"+yybMusician.getName());

		office.setType("1");
		office.setUseable("1");
		office.setGrade("1");
		office.setDelFlag("0");
		officeService.saveOffice(office);


		User user = new User();
		user.setCompany(new Office("1"));
		user.setOffice(new Office(officeId));

		user.setCreateBy(createUser);
		user.setUpdateBy(createUser);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		user.setDelFlag("0");

		user.setPassword(SystemService.entryptPassword("123456"));
		user.setPhoto(yybMusician.getHeadPhoto());
		user.setName(yybMusician.getName());
		HanyuPinyinHelper hanyuPinyinHelper = new HanyuPinyinHelper() ;
		String adminLoginName = hanyuPinyinHelper.toHanyuPinyin(yybMusician.getName());
		user.setLoginName(this.getLoginName(adminLoginName));

		user.setLoginFlag("1");
		//角色音乐id
		user.setRoleList(Arrays.asList(new Role(sysParamService.getValueByKey("role_music"))));
		systemService.saveUser(user);



		yybMusician.setId(id);
		yybMusician.setStatus(2);
		yybMusician.setCompanyId(officeId);
		yybMusician.setCompanyName("独立音乐人"+yybMusician.getName());
		yybMusicianApiService.save(yybMusician);

		j.setMsg("通过音乐人成功，请牢记此独立音乐人后台登陆账号："+adminLoginName+"  密码：123456");
        return j;
    }


    /**
     * 拒绝音乐人
     */
	@IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "noPass")
    public AjaxJson noPass(String id) {
        AjaxJson j = new AjaxJson();
        YybMusician yybMusician = new YybMusician();
        yybMusician.setId(id);
        yybMusician.setStatus(3);
		yybMusicianApiService.updateStatus(yybMusician);
        j.setMsg("拒绝音乐人成功");
        return j;
    }



	@ResponseBody
	@RequestMapping(value = "isHave", method = RequestMethod.GET)
	@ApiOperation(notes = "isHave", httpMethod = "get", value = "判断改用户是否申请过音乐人或通过音乐人")

	public Result isHave(HttpServletRequest request){
		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);

		//判断用户处于再申请， 通过的数量
		Map<String, Object> param = new HashMap<>();
		param.put("memberId", yybMember.getId());

		int count = yybMusicianApiService.getMemberApplyHis(param);

		return ResultUtil.success(count > 0 );
	}



	/**
	 * 保存独立音乐人
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ApiOperation(notes = "save", httpMethod = "post", value = "保存独立音乐人")
	@ApiImplicitParams({@ApiImplicitParam(name = "yybMusicianVo", value = "yybMusicianVo", required = true, paramType = "body",dataType = "body")})

	public Result save(HttpServletRequest request, @RequestBody @Valid YybMusicianVo yybMusicianVo, BindingResult bindingResult) throws Exception{

		//校验参数,出错抛出异常
		if (bindingResult.hasErrors()) {
			logger.error("参数错误.(" + bindingResult.getFieldError().getDefaultMessage() + ")");
			return ResultUtil.error("参数错误.(" + bindingResult.getFieldError().getDefaultMessage() + ")");
		}


		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);


		//判断用户处于再申请， 通过的数量
		Map<String, Object> param = new HashMap<>();
		param.put("memberId", yybMember.getId());

		int count = yybMusicianApiService.getMemberApplyHis(param);

		if (count > 0) {
			return ResultUtil.error("您已申请过独立音乐人");
		}

		YybMusician yybMusician = new YybMusician();

		BeanUtils.copyProperties(yybMusicianVo, yybMusician);
		yybMusician.setMemberId(yybMember.getId());
		yybMusician.setType(2);
		yybMusician.setStatus(1);
		yybMusician.setMemberId(yybMember.getId());

		yybMusicianApiService.save(yybMusician);

		return ResultUtil.success();
	}


	private String getLoginName(String loginName){
		if (systemService.getUserByLoginName(loginName) == null) {
			return loginName;
		}else{
			return getLoginName(loginName+"0");
		}
	}


}