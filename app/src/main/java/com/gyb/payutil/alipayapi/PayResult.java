package com.gyb.payutil.alipayapi;

import java.util.Map;

/**
 * Created by GuiYanBing on 2018/6/29 13:43
 * E-Mail Address：guiyanbing@zhiyihealth.com.cn
 * describe：描述 支付宝 支付结果
 */

public class PayResult {

    private String resultStatus;
    private String result;
    private String memo;

    public PayResult(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }

        for (String key : rawResult.keySet()) {
            if (key.equals("resultStatus")) {
                resultStatus = rawResult.get(key);
            } else if (key.equals("result")) {
                result = rawResult.get(key);
            } else if (key.equals("memo")) {
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
}
