package com.coderstory.Purify.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.coderstory.Purify.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import static com.coderstory.Purify.utils.root.SuHelper.canRunRootCommands;

public class MyAppIntro extends AppIntro {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    protected SharedPreferences.Editor getEditor() {
        getPrefs();
        if (editor == null) {
            editor = prefs.edit();
        }
        return editor;

    }

      protected SharedPreferences getPrefs() {
        if (prefs == null) {

            prefs = getApplicationContext().getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE);
        }
        return prefs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getPrefs().getBoolean("showGuide", true)) {
            int color=ContextCompat.getColor( MyAppIntro.this, R.color.colorPrimary);
            addSlide(AppIntroFragment.newInstance("欢迎使用!", "欢迎使用本软件！\n", R.drawable.ic_slide1,color));
            addSlide(AppIntroFragment.newInstance("存储权限", "软件在发生异常的时候需要保存相关日志到本地. \n", R.drawable.ic_slide2,color));
            addSlide(AppIntroFragment.newInstance("ROOT权限", "软件的功能不是需要Xposed模块就是需要ROOT,不支持的话就无法生效. \n", R.drawable.ic_slide3,color));
            addSlide(AppIntroFragment.newInstance("使用须知!", "请勿未禁止软件自启,否则功能可能失效\n", R.drawable.ic_slide4, color));
            addSlide(AppIntroFragment.newInstance("设置完毕!", "祝您玩的愉快! ", R.drawable.ic_slide5, color));
            askForPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
            askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

            //禁用skip
            showSkipButton(false);
            //允许滑动
            setNextPageSwipeLock(false);
            setSwipeLock(false);
            setDoneText("完成");

            setBarColor(color);
            setSeparatorColor(color);

        }else{
            SplashActivity();
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        getEditor().putBoolean("showGuide",false);
        getEditor().apply();
        loadMainActivity();
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }
    int count=0;
    @Override
    public void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        if (count == 4) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    canRunRootCommands();
                }
            }.start();
        }
        count++;
    }

    public void getStarted(View v) {
        loadMainActivity();
    }


    void SplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        this.finish();
    }

    void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
