package com.gyb.payutil.manager;

import android.app.Activity;
import android.text.TextUtils;

import com.gyb.payutil.alipayapi.Alipay;
import com.gyb.payutil.wxapi.WeiXinPay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by GuiYanBing on 2018/6/29 13:45
 * E-Mail Address：guiyanbing@zhiyihealth.com.cn
 */

public class PayManager {

    private static PayManager mPay;
    private Activity mContext;

    private PayManager(Activity context){
        mContext = context;
    }

    public static PayManager getInstace(Activity mContext){
        if (mPay == null) {
            synchronized (PayManager.class) {
                if (mPay == null) {
                    mPay = new PayManager(mContext);
                }
            }
        }
        return mPay;
    }
    public interface YxgPayListener {
        //支付成功
        void onPaySuccess(Map<String, String> rawResult);

        //支付失败
        void onPayError(int error_code, String message);

        //支付取消
        void onPayCancel();
    }


    public enum PayMode {
        WXPAY, ALIPAY,
    }


    public void toPay(PayMode payMode, String payParameters, YxgPayListener listener) {
        if (payMode.name().equalsIgnoreCase(PayMode.WXPAY.name())) {
            toWxPay(payParameters, listener);
        } else if (payMode.name().equalsIgnoreCase(PayMode.ALIPAY.name())) {
            toAliPay(payParameters, listener);
        }
    }


    public void toWxPay(String payParameters, YxgPayListener listener) {
        if (payParameters != null) {
            JSONObject param = null;
            try {
                param = new JSONObject(payParameters);
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            if (TextUtils.isEmpty(param.optString("appId")) || TextUtils.isEmpty(param.optString("partnerId"))
                    || TextUtils.isEmpty(param.optString("prepayId")) || TextUtils.isEmpty(param.optString("nonceStr"))
                    || TextUtils.isEmpty(param.optString("timeStamp")) || TextUtils.isEmpty(param.optString("sign"))) {
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            WeiXinPay.getInstance(mContext).startWXPay(param.optString("appId"),
                    param.optString("partnerId"), param.optString("prepayId"),
                    param.optString("nonceStr"), param.optString("timeStamp"),
                    param.optString("sign"), listener);

        } else {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public void toWxPay(String appId, String partnerId, String prepayId,
                        String nonceStr, String timeStamp, String sign, YxgPayListener listener) {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(partnerId)
                || TextUtils.isEmpty(prepayId) || TextUtils.isEmpty(nonceStr)
                || TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(sign)) {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
            return;
        }
        WeiXinPay.getInstance(mContext).startWXPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, listener);
    }


    public void toAliPay(String payParameters, YxgPayListener listener) {
        if (payParameters != null) {
            if (listener != null) {
                Alipay.getInstance(mContext).startAliPay(payParameters, listener);
            }
        } else {
            if (listener != null) {
                listener.onPayError(Alipay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }



    public void getWeixinPayParams(){

    }

}

