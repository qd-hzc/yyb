package com.jeeplus.modules.api.order.web;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.web.Result;
import com.jeeplus.core.web.ResultUtil;
import com.jeeplus.modules.api.music.service.YybMusicApiService;
import com.jeeplus.modules.api.order.alipay.AlipayConfig;
import com.jeeplus.modules.api.order.alipay.AlipayUtil;
import com.jeeplus.modules.api.order.entity.OrderApi;
import com.jeeplus.modules.api.order.entity.OrderDeatilApi;
import com.jeeplus.modules.api.order.entity.YybOrderVo;
import com.jeeplus.modules.api.order.service.YybOrderApiService;
import com.jeeplus.modules.api.right.service.YybRightApiService;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;
import com.jeeplus.modules.api.shopcart.service.YybShopcartApiService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.jeeplus.modules.sys.interceptor.LogInterceptor.LOGIN_MEMBER;

@Controller
@RequestMapping(value = "/api/order")
public class OrderApiController extends BaseController {


    @Autowired
    private YybShopcartApiService yybShopcartApiService;
    @Autowired
    private YybMusicApiService  yybMusicApiService;
    @Autowired
    private YybUsageApiService yybUsageApiService;
    @Autowired
    private YybRightApiService yybRightApiService;
    @Autowired
    private YybOrderApiService yybOrderApiService;

    @ResponseBody
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ApiOperation(notes = "createOrder", httpMethod = "post", value = "下单")
    @ApiImplicitParams({@ApiImplicitParam(name = "YybOrderVo", value = "YybOrderVo", required = true, paramType = "body",dataType = "body")})
    public Result createOrder(HttpServletRequest request, @RequestBody @Valid YybOrderVo yybOrderVo,
                          BindingResult bindingResult){
        logger.info("toOrder:request:" + JSON.toJSONString(yybOrderVo));
        String orderNo = "";
        try {
            YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
            String memebrId = yybMember.getId();

            //校验参数, 返回购物车
            Result validParam = this.validParam(yybOrderVo, bindingResult, memebrId);

            List<YybShopcart> shopcartList = yybShopcartApiService.getListByIds(yybOrderVo.getShopcartIds());

            if (!"0000".equals(validParam.getCode())) {
                return validParam;
            }

            orderNo = yybOrderApiService.toOrder(yybOrderVo, shopcartList, memebrId);
        } catch (Exception e) {
            logger.error("生成订单失败:"+e.getMessage(), e);
            return ResultUtil.error("生成订单失败");
        }
        return ResultUtil.success(orderNo);
    }







    /**
     * 取消订单
     */
    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ApiOperation(notes = "cancel", httpMethod = "post", value = "取消")
    @ApiImplicitParams({@ApiImplicitParam(name = "orderId", value = "订单id", required = true, paramType = "form",dataType = "string")})
    public Result cancel(HttpServletRequest request, @RequestParam String orderId) throws Exception{

        YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
        String memebrId = yybMember.getId();

        OrderApi orderApi = yybOrderApiService.get(orderId);
        if (orderApi == null || orderApi.getStatus() != 1) {
            return ResultUtil.error("获取订单状态异常");
        }

        if (!memebrId.equals(orderApi.getMemberId())) {
            return ResultUtil.error("用户校验异常");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("status", 2);
        param.put("orderId", orderId);


        yybOrderApiService.updateStatus(param);

        return ResultUtil.success();
    }


    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(notes = "list", httpMethod = "GET", value = "列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "startPage", value = "", required = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "", required = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "status", value = "status:0:未支付 1:已取消 2:已支付", required = false, paramType = "query",dataType = "string")})

    public Result list(HttpServletRequest request, @RequestParam(required = false) Integer status ,
                       @RequestParam(required = false, defaultValue = "1") String startPage,
                       @RequestParam(required = false, defaultValue = "10") String pageSize){
        YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
        String memebrId = yybMember.getId();

        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        param.put("memberId", memebrId);

        List<OrderApi> list = yybOrderApiService.list(param);

        PageHelper.startPage(Integer.parseInt(startPage),Integer.parseInt(pageSize));

        PageInfo<OrderApi> pageInfo = new PageInfo<>(list);

        return ResultUtil.success(pageInfo);
    }



    /**
     * 支付宝支付
     */
    @ResponseBody
    @RequestMapping(value = "/alipay", method = RequestMethod.POST)
    @ApiOperation(notes = "alipay", httpMethod = "POST", value = "列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "orderId", value = "orderId", required = true, paramType = "query",dataType = "string")})
    public Result pay(HttpServletRequest request, @RequestParam String orderId) {

        logger.info("pay:request:" + (orderId));

        YybMember yybMember = (YybMember) request.getAttribute(LOGIN_MEMBER);
        String memberId = yybMember.getId();

        //商户订单号，商户网站订单系统中唯一订单号，必填
        OrderApi orderApi = yybOrderApiService.get(orderId);
        if (orderApi == null || orderApi.getStatus() != 1) {
            logger.error("支付：订单状态异常");
            return ResultUtil.error("订单状态异常");
        }

        if (!orderApi.getMemberId().equals(memberId)) {
            logger.error("支付：订单异常");
            return ResultUtil.error("订单异常");
        }

        List<OrderDeatilApi> deatilList = yybOrderApiService.getDetailListByOrderId(orderId);
        for (OrderDeatilApi orderDeatilApi : deatilList) {
            YybMusic yybMusic = yybMusicApiService.get(orderDeatilApi.getMusicId());
            if (yybMusic == null || yybMusic.getId() ==null || "1".equals(yybMusic.getDelFlag())) {
                logger.error("支付：音乐状态异常");
                return ResultUtil.error("音乐状态异常");
            }
        }

        //付款金额，必填
        String money = orderApi.getOrderAmount().toString();
        String orderNo = orderApi.getOrderNo();
        //订单名称，必填
        String name = new String("音乐邦版权购买");
        //商品描述，可空
        String info = new String("音乐邦版权购买");
        String result= "";
        try {
            result = AlipayUtil.pay(money, info, name, orderId, orderNo);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultUtil.success(result);
    }


    /**
     * 回调
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/notify_url",method= RequestMethod.POST)
    @ResponseBody
    public String notify(HttpServletRequest request,HttpServletResponse response) throws Exception {

        logger.info("支付宝回调参数：" + JSON.toJSONString(request.getParameterMap()));

        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
        logger.info("支付宝验签成功");


        //——请在这里编写您的程序（以下代码仅作参考）——
	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("orderNo").getBytes("ISO-8859-1"),"UTF-8");

            String orderId = new String(request.getParameter("orderId").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                Map<String, Object> param = new HashMap<>();
                param.put("orderId", orderId);
                param.put("tradeNo", trade_no);

                yybOrderApiService.updateTradeNo(param);


                param.put("status", 3);
                yybOrderApiService.updateStatus(param);
                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            return "success";

        }else {//验证失败
            return "fail";

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }
    }


    private Result validParam(YybOrderVo yybOrderVo, BindingResult bindingResult, String memberId) {
        //校验参数,出错抛出异常
        if (bindingResult.hasErrors()) {
            logger.error("参数错误.(" + bindingResult.getFieldError().getDefaultMessage() + ")");
            return ResultUtil.error("参数错误.(" + bindingResult.getFieldError().getDefaultMessage() + ")");
        }

        //个人
        if (1 == yybOrderVo.getMemberType()) {
            if (StringUtils.isEmpty(yybOrderVo.getIdCard()) || StringUtils.isEmpty(yybOrderVo.getIdCardAttach())
                    || null == yybOrderVo.getMemberSex() || (yybOrderVo.getMemberSex() != 1 && yybOrderVo.getMemberSex() != 2)){
                logger.error("下单：个人信息不全");
                return ResultUtil.error("个人信息不全");
            }
        } else {
            if (StringUtils.isEmpty(yybOrderVo.getOrgCode()) || StringUtils.isEmpty(yybOrderVo.getOrgCodeAttach())){
                logger.error("下单：公司信息不全");
                return ResultUtil.error("公司信息不全");
            }
        }

        //金额校验
        List<YybShopcart> yybShopcartList = yybShopcartApiService.getListByIds(yybOrderVo.getShopcartIds());

        //计算总额
        BigDecimal calOrderAmount = BigDecimal.ZERO;
        for (YybShopcart yybShopcart : yybShopcartList) {

            if (!memberId.equals(yybShopcart.getMemberId())) {
                logger.error("下单：我的购物车异常");

                return ResultUtil.error("我的购物车异常");
            }

            if (!StringUtils.isEmpty(yybShopcart.getOrderId())) {
                logger.error("下单：购物车已下单");

                return ResultUtil.error("购物车已下单");
            }

            YybMusic yybMusic = yybMusicApiService.get(yybShopcart.getMusicId());
            if (yybMusic == null || com.jeeplus.common.utils.StringUtils.isBlank(yybMusic.getId())) {
                logger.error("下单：获取音乐失败");

                return ResultUtil.error("获取音乐失败");
            }

            BigDecimal musicPrice = BigDecimal.valueOf(yybShopcart.getMusicPrice());
            if (musicPrice.compareTo(BigDecimal.valueOf(yybMusic.getPrice())) != 0) {
                logger.error("下单：音乐价格异常");

                return ResultUtil.error("音乐价格异常");
            }


            List<String> rightIds = Arrays.asList(yybShopcart.getRightSelect().split(","));
            List<String> usageIds = Arrays.asList(yybShopcart.getUsageSelect().split(","));
            List<YybRight> rightList = yybRightApiService.getListByIds(rightIds);
            List<YybUsage> usageList = yybUsageApiService.getListByIds(usageIds);
            if (rightIds.size() != rightList.size() ||
                    usageIds.size() != usageList.size()) {
                logger.error("下单：获取权利或用途异常");

                return ResultUtil.error("获取权利或用途异常");
            }

            BigDecimal rightTotal = BigDecimal.ZERO;
            for (YybRight right : rightList) {
                rightTotal = rightTotal.add(BigDecimal.valueOf(right.getRate()));
            }
            BigDecimal usageTotal = BigDecimal.ZERO;
            for (YybUsage yybUsage : usageList) {
                usageTotal = usageTotal.add(BigDecimal.valueOf(yybUsage.getRate()));
            }

            if (BigDecimal.ZERO.compareTo(rightTotal) == 0 || BigDecimal.ZERO.compareTo(usageTotal) == 0) {
                logger.error("下单：选择权利或用途比率异常");

                return ResultUtil.error("选择权利或用途比率异常");
            }

            BigDecimal musicTotal = (rightTotal.add(usageTotal)).multiply(musicPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
            calOrderAmount = calOrderAmount.add(musicTotal);
            if (musicTotal.compareTo(BigDecimal.valueOf(yybShopcart.getMusicTotal())) != 0) {
                logger.error("商品:["+yybShopcart.getMusicTitle()+"]金额发生变动, 请重新加入购物车");

                return ResultUtil.error("商品:["+yybShopcart.getMusicTitle()+"]金额发生变动, 请重新加入购物车");
            }

        }

        if (yybOrderVo.getOrderAmount().compareTo(calOrderAmount) != 0) {
            logger.error("下单：订单总额不符");

            return ResultUtil.error("订单总额不符");
        }

        return ResultUtil.success();
    }



}
