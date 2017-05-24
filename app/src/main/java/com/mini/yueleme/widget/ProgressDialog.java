package com.mini.yueleme.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.mini.yueleme.R;

/**
 * 自定义进度对话框
 * Created by weiersyuan on 2016/7/24.
 */
public class ProgressDialog extends Dialog {

    private SpinKitView spinKitView;

    public ProgressDialog(Context context) {
        super(context,R.style.baseDialog);
        setContentView(R.layout.loading_dialog);
        initView();
        setProperty();
    }

    private void initView() {
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        FadingCircle doubleBounce = new FadingCircle();
        spinKitView.setIndeterminateDrawable(doubleBounce);
    }

    private void setProperty() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

}
