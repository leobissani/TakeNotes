package com.bissani.takenotes;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loading = (ProgressBar) findViewById(R.id.loadingBar);

        loading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_light),
                PorterDuff.Mode.MULTIPLY);

        //loading.setProgressTintList(ColorStateList.valueOf(Color.BLACK));

        Handler handle = new Handler();

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, 2000);
    }

    private void startApp() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
