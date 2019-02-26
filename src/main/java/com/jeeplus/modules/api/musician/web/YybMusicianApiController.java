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
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

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
	private YybMusicianService yybMusicianService;
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
        YybMusician yybMusician = yybMusicianService.get(id);

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
		yybMusician.setCompanyName("音乐人"+yybMusician.getName());
		yybMusicianService.save(yybMusician);

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
        yybMusicianService.updateStatus(yybMusician);
        j.setMsg("拒绝音乐人成功");
        return j;
    }



	/**
	 * 保存音乐人
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Result save(HttpServletRequest request, YybMusician yybMusician) throws Exception{

		YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
		yybMusician.setType(2);
		yybMusician.setStatus(1);

		yybMusician.setMemberId(yybMember.getId());
		yybMusicianService.save(yybMusician);//保存
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