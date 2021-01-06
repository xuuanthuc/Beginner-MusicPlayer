package musicplayer.group3.dev.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.media.MediaManager;


public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_DISPLAY_LENGTH = 3000;
    Animation rotateAnimation;
    private ImageView rotateImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        MediaManager.getInstance(this).setArrItemSong(MediaManager.getInstance(this).getSongList(null, null));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        rotateImg = findViewById(R.id.fullscreen_content);
        rotateAnimation();

    }

    private void rotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        rotateImg.startAnimation(rotateAnimation);
    }


}
