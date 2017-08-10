package in.habel.animator;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by habel on 24/4/17.
 */

final class ViewHelper {

    static void clear(View v) {
        v.setAlpha(1);
        v.setScaleX(1);
        v.setScaleY(1);
        v.setTranslationX(0);
        v.setTranslationY(0);
        v.setRotation(0);
        v.setRotationX(0);
        v.setRotationY(0);
        v.setPivotX(v.getMeasuredWidth() / 2);
        v.setPivotY(v.getMeasuredHeight() / 2);
        ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
    }
}