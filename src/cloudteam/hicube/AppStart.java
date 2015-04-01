package cloudteam.hicube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import cloudteam.hicube.activity.BaseActivity;
//import cloudteam.hicube.activity.LoginActivity;

public class AppStart extends BaseActivity {


    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
       View localView = View.inflate(this, R.layout.activity_splash, null);
        setContentView(localView);
        setDefaultLaunch(localView);
    }

    private void redirectTo() {
       // startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setDefaultLaunch(View paramView) {
        specialLaunch(paramView);
    }

    private void specialLaunch(View paramView) {
        AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.3F, 1.0F);
        localAlphaAnimation.setDuration(2000L);
        paramView.startAnimation(localAlphaAnimation);
        localAlphaAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation paramAnimation) {
                AppStart.this.redirectTo();
            }

            public void onAnimationRepeat(Animation paramAnimation) {
            }

            public void onAnimationStart(Animation paramAnimation) {
            }
        });
    }


    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
}
