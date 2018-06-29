package com.gyb.payutil.wxapi;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.gyb.payutil.manager.PayManager.YxgPayListener;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by GuiYanBing on 2018/6/29 13:48
 * E-Mail Address：guiyanbing@zhiyihealth.com.cn
 */
public class WeiXinPay {

    private static WeiXinPay mWeiXinPay;
    private Activity mContext;
    private IWXAPI mIWXAPI;
    private YxgPayListener mYxgPayListener;

    //未安装微信或微信版本过低
    public static final int WEIXIN_VERSION_LOW = 0x001;
    //支付参数异常
    public static final int PAY_PARAMETERS_ERROE = 0x002;
    //支付失败
    public static final int PAY_ERROR= 0x003;

    private WeiXinPay(Activity context) {
        mContext = context;
    }

    public static WeiXinPay getInstance(Activity context){
        if (mWeiXinPay == null) {
            synchronized(WeiXinPay.class){
                if (mWeiXinPay == null) {
                    mWeiXinPay = new WeiXinPay(context);
                }
            }
        }
        return mWeiXinPay;
    }

    /**
     * 初始化微信支付接口
     * @param appId
     */
    public void init(String appId){
        mIWXAPI= WXAPIFactory.createWXAPI(mContext, null);
        mIWXAPI.registerApp(appId);
    }

    /**
     * 获取微信接口
     * @return
     */
    public IWXAPI getWXApi() {
        return mIWXAPI;
    }

    /**
     * 调起支付
     * @param appId
     * @param partnerId
     * @param prepayId
     * @param nonceStr
     * @param timeStamp
     * @param sign
     */
    public void startWXPay(String appId,String partnerId,String prepayId,
                           String nonceStr,String timeStamp,String sign,YxgPayListener listener){
        mYxgPayListener = listener;
        init(appId);

        if (!checkWx()) {
            if(listener != null) {
                listener.onPayError(WEIXIN_VERSION_LOW,"未安装微信或者微信版本过低");
            }
            return;
        }

        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId= prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr=nonceStr;
        request.timeStamp= timeStamp;
        request.sign= sign;
        mIWXAPI.sendReq(request);
    }


    /**
     * 响应支付回调
     * @param error_code
     * @param message
     */
    public void onResp(int error_code,String message) {
        if(mYxgPayListener == null) {
            return;
        }
        if(error_code == 0) {
            //支付成功
            mYxgPayListener.onPaySuccess(null);
        } else if(error_code == -1) {
            //支付异常
            mYxgPayListener.onPayError(PAY_ERROR,message);
        } else if(error_code == -2) {
            //支付取消
            mYxgPayListener.onPayCancel();
        }

        mYxgPayListener = null;
    }



    //检测微信客户端是否支持微信支付
    private boolean checkWx() {
        return isWeixinAvilible() && mIWXAPI.isWXAppInstalled() && mIWXAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
    /**
     * 判断微信是否安装
     * @return
     */
    private  boolean isWeixinAvilible() {
        return appIsAvilible("com.tencent.mm");
    }

    /**
     * 判断app是否安装
     * @param packageName
     * @return
     */
    private  boolean appIsAvilible(String packageName) {
        final PackageManager packageManager = mContext.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
