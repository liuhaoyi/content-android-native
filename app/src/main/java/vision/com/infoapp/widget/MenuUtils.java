package vision.com.infoapp.widget;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

public class MenuUtils {
	public static void selectMenuTrace(Activity p_activity, View p_viewFocus,
                                       int p_menu_count, int p_select_menu_index,
                                       int p_old_select_index) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		p_activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int _screen_width = displayMetrics.widthPixels;		
		int _menu_width = _screen_width / p_menu_count;
		
		Animation _animation = new TranslateAnimation(
				_menu_width * p_old_select_index,
				_menu_width * p_select_menu_index,
				2,
				2);
		_animation.setFillAfter(true);
		_animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// viewFocus.layout(viewFocus.getLeft(),viewFocus.getTop() ,
				// viewFocus.getLeft()+screen_width/6, viewFocus.getTop()+28);
			}
		});
		_animation.setDuration(500);
		_animation.setInterpolator(AnimationUtils.loadInterpolator(p_activity,
				android.R.anim.accelerate_decelerate_interpolator));
		p_viewFocus.setAnimation(_animation);
		_animation.startNow();
	}
}
