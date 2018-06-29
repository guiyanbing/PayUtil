package com.gyb.payutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gyb.payutil.manager.PayManager;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,PayManager.YxgPayListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_weixin=findViewById(R.id.btn_weixin);
        Button btn_alipay=findViewById(R.id.btn_alipay);
        btn_weixin.setOnClickListener(this);
        btn_alipay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_weixin:
                //微信支付
                PayManager.getInstace(MainActivity.this).toWxPay("","", "", "", "", "", this);
                break;
            case R.id.btn_alipay:
                //支付宝支付
                //参数一，后台返回的支付宝需要的一串字符串
                PayManager.getInstace(MainActivity.this).toAliPay("", this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPaySuccess(Map<String, String> rawResult) {

    }

    @Override
    public void onPayError(int error_code, String message) {

    }

    @Override
    public void onPayCancel() {

    }
}
