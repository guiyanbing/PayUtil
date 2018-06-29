package com.gyb.payutil.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by GuiYanBing on 2018/6/29 14:41
 * E-Mail Address：guiyanbing@zhiyihealth.com.cn
 */

public class BaseApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
