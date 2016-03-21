package com.first.fubao.oto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.first.fubao.oto.utils.NetUtil;
import com.first.fubao.oto.utils.UIUtils;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/1
 * @描述：欢迎页面
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (!NetUtil.isNetworkAvailable(this)){
            Toast.makeText(WelcomeActivity.this, "网络状况不佳", Toast.LENGTH_SHORT).show();
        }
        UIUtils.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }

        }, 1000);
    }
}
