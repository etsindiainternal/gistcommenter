package com.ets.gistcommenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.Constants;

public class Home extends AppCompatActivity {

    @BindView(R.id.idgotoscaner)
    ImageView imgscanner;

    private MediaPlayer mediaPlayer = null;
    private Animation shake;
    private boolean voiceflag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash_screen);
        getSupportActionBar().hide();
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        ButterKnife.bind(this);

        imgscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceflag = false;//setting the voice flag false if user  proceed immediately with scan icon
                Intent i = new Intent(Home.this, QRScannerActivity.class);
                startActivity(i);
                finish();

            }
        });

        /*logic to play voice assitance only ones
        and continue shake animation every 3 seconds for icon*/
        final Handler objHandler = new Handler();
        final int delay = 3000; //milliseconds
        objHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgscanner.startAnimation(shake);
                if (voiceflag) {
                    playSound(Home.this);
                }
                voiceflag = false;
                objHandler.postDelayed(this, delay);
            }
        }, delay);


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    //play voice assistance method
    public void playSound(Context context) {
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(Constants.VOICE_FILENAME);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    private static final int TIME_DELAY = 3000;
    private static long back_pressed;

    @Override
    public void onBackPressed() {

        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            exitApp();
        } else {
            Constants.showToast(this, Constants.BACK_PRESS_MSG);
        }
        back_pressed = System.currentTimeMillis();
    }

    private void exitApp() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
