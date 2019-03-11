package com.jeeplus.modules.api.order.alipay;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092500589722";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDANxXDNh1CHjN13mSH9btPX9tMYtpdxWsDiwcz/nzmXuRnlRTfsWLb+xvol5kCqJge2MiT1Ywx75mIYJMcBOFaNJQsrYdmFLpvrtEmjyfIOTGQXBAFyq9VYUrU+mG4j8goS5FGEeKMg1l7t4LwOgXXrWKleePmXGEze1tGUFb9/oO1Yz6aq+S2oGYNF9ePGSe/lBQNXBCn2nXB4C3Bn6zw8qhJpmkEcFzI3q4ey1OxL32lZyjGND6AvbpTPdi2NiqrXc41kKWN6q6mf1+21bROg9rIPkUrwJGnSLTOwD4p0CMoF35GzpIoTZ3ZThiAZkA2J1UBGyWT5BPGnkDKA6PfAgMBAAECggEBALJ08iGRpkxymbh/R7rrLIKfLo2W4cJZvedGNuPRDMkdpNV+liixGmW+l8S0qvzK5qENB/wpMcZS+6qgbzR+W3NSiZeJbMIb0JRMLCaulnLQlvG+Z6nY6jxM3hsqc3vVEjJHJpN1m251rKiuUkUSucXqVkHoDu0bK6uOhcmcYYfiHxuAWFT1C8bTnJNe9uZYxXNlhKEr1HMIHAjrYawRaF+cE7f53ISomtl63BdpyvQVJP6PLYQSbgae2oXQmekZ6Zt3V4OXfbi4esDHlr15j/ma9DChQnxrfFfbVZEP5XQyOMlEdPIgKxSYkAkIg2bcXRVmsnUiakAjq8rrlIt1uzECgYEA4HljSrNTtLIBFRnTVt6gqUQ3v+9hmIQBQO32ifwRLZDilz92RSaT2ZPR6zikxhFAOXzT53eiXcv8VvaM5SKhxQqLqSiX7HWEVpqF8nhrNOoIhtHX1eCLI4K1t3qrfKcxoxbDpMvN2maOo3RryFd6dhvYBm5XtM3TCP47j4B+XFUCgYEA2zXgUHFj2OSXFtNtqGSJthxUXBmIk/9X3BcwxmaEQUmTqqqfzQiHC+/mMLFVBH/h920cs0UplfIRzW9O5XMQ6h6798i9traLZ4htFIgedNgP3Wjkhiy2yTfAqrpsOXHB6d4gmmsaT2lXwsqh0jYiEqWS4sgozEocM0WQpbq9M2MCgYBhZAkyVc+RbFGP6B6LaBfomJJfkeqUE2G/2/7FKzoAvQWyfXRA2FX5ZmsDVRYYYU+uIXyGxp8JdwFKcbn7iCULqmGhyNzxWKNMN8q5wokw8gE1DUJJpxx3EbTpoC1a7edZTP0hONNdWHW2cUJ6L4oQSFBXqfQfm9Vd53/L6QHp6QKBgChUJB2Ijt8DRwyq/xygynkXgymihO79ZhOWrKGDkMdWGvWmCFo3PGkkzIuexE5cnJzhlQsiKUFt1HqgBLrPzYfU+a3fX4+yy7jTb4X8eOX0Lg8E0i4hIcupFygRvgFffVZiSEtAcwmzDrNGhjjfPquBjKFuQ5s58zA3lyH2VgKTAoGBAJc7Fn1RKojD1v1++1JwNSh1qyzHXlNTVZetSMJ973ZfiyUPLnS1bnZYleFb/nSZut+eX7J5foH7q9DaOVx1kJNWOK3ZEiqjEqz/L/a705rvhGbwb9RLstIc+d0E5+9vaES9y8xhfaMP4PD5G4kwtlBjJr2zl7TW4J1IOZc2cKzK";

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

    // 支付宝网关
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