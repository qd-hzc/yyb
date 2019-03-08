package com.jeeplus.modules.api.order.alipay;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092500589722";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCKHOvZfQwaON8O9yyqdYGTolRcQsKGGA3x+DPPJtNJZXhgJzhT3bQjRdjt8BC9alyCLDOfYTrUilXfevkYkm1y+ly+wtUr9NMSKigMpxqlCCD71e5zBf++oyzj3l7iWcyhib/RX/8PrCP5X/XI9DKAEJxiI4CeHJk0VhAvVECbVg9auGSRaI2Y5Vg0SaPskUNg1pulFzBEu+kZnU6rH8lekhgsoOyQ4z7VxswUM+aqMvE+rUcSgW9D45sUkAKvkLHNJmEOtLhT22Ijj3WgsQx6CIoACfzgWSNXCintdFhoagZKre86wVGBOQryVMYRsPqkA2hvcrq1/kbjBN6gACg5AgMBAAECggEARhAR3QzAfPTowxDEydw+VYR9wyfiwl+lg2Bj9+w8NvmQrUZ4HqNTZKlkOKAV/eDc+PQvY+TREUzz7yDVCJAPkcpMDkGMHnZeNNLJ6lFDBQEwqlCGXDdo226vsRD3lV/0nOXiNva5z1uGDFWPu9eVXr4OdwKHlq3vUktufnQBKkspHkOYwB1SQkz6xAaV0LFz0l2uQlIwlD9OQQoviOoqyiKCXAhT4CUTDdlAHc086N+8/PX8xxMUX1UgNR8izTVktxwD+OuIXZW2aUXeS4tYDLopV80IMMKJl1dOOBhbcK/bOTULMcpAfRNQVC7C/mTGHB3k/b8ahSsuYlQ3llnqmQKBgQDEVsbS7xyuewsyBFEkZ6AqtxlOBFviO2VphUUDV/qcsVYPy7zPD8Y+JMkP+s0jEkxfpClJe8uvK86Tm7gN8sJjVZtdfkBEjtPtr7/Hyp6t3Uj0mDlE8hpQhLy4gg/bbScuG4laJNwk+gARmj98TuvLZf3HLjaUPaOqGgSX7U0WnwKBgQC0FL1qXYSSoEC1EGesqczqvZgnHI/6CiO8JXlyXyEEyZeJrPaocVIUFrVGwYBOx+YH42XemFP+QxlkGm3ZIy+XUngAZmZ9Vw38E19ChAV/fdKmEamxrgFD/+Vi4t0vL7B2//BTbWtumdPkBLpi3rFgNWHihjZfgsw1d85JivCKJwKBgQCSKuA+LNE3fFbrV5do+NSeuE1YbZpSJFHtas/kG11xxfwCKnVX2bCGlzAwe1C46Wh+U2KBWG5c7MoPzgAKMk5RUWwnAFSsNYDFFPA7tzbGzVUA80q/b1Xz173/xqQzgWGHy+xHdeWGia//aswUqVSVykpSPK9ZUUJE4rXEvV+ElQKBgBJfI0vcqo/C5ZcTS2BkGlHAcetbkJyzuB2TvSYb6mRwpJMZIDjjBrqh7nB/gTWIU0Fuw4/H/pmsAsUhpR9H5a0Mbs2rpL0YyoZy+37vtRfioEDtAGDuHZbwXliOSH5t4RTi24PHX/RyoRlcGh/IFFR6XbgcdSOdVnQF+vepwwx1AoGAGkRgKKl9cYeuEDcg4zWiAiV5CdEYAP+98sVx8M6TzmdHtG2qjYSXEswwB8f0ctBPvtd/xhj/2JhkrbUHOmY2FJIXCgegBlrSS0+IcjDqgWi3KHByxyef0hZuuiauqYHwlc0sMceYSHmfPdmPU35hzNArS+C3vOTq5k2Bd520gko=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArzGlwGmHugPMzCh92Nm3m9gt/vf5hmmKq0XU4kRwkTdPwdJwublhiX15ds5Oe6bqodwRpLK5EBSknaz42mSrxvRHnFgJnkTCV57GFBZV3E3Oe6MMzREOBraMh1DfVH450ezsD19Cc3vm23DeWL/hQ7OXK01MBu1xjLMVMgTEGV0ycRi2kr4p0lwqJQ/yl6kdsZGQtwxQkteg1e1lRvl1xUt8EJh06j+eblI1YAAoIh7WztN5H4yUG9sdf1l4r2vp9Df6td8cvU8w6WoUc5oebQm56F/+g9WZDMW/XACEh3ggWxSjJEUsvZJBy/+NhzRR9in1Ol1IopMiL4U6yCVMywIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝日志
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}