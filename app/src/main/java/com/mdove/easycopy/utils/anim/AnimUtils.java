package com.mdove.easycopy.utils.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * @author wangwei on 2017/11/30.
 *         wangwei@jiandaola.com
 */
public class AnimUtils {

    /**
     * Rotate point P around center point C.
     *
     * @param pX             x coordinate of point P
     * @param pY             y coordinate of point P
     * @param cX             x coordinate of point C
     * @param cY             y coordinate of point C
     * @param angleInDegrees rotation angle in degrees
     * @return new x coordinate
     */
    public static float rotateX(float pX, float pY, float cX, float cY, float angleInDegrees) {
        double angle = Math.toRadians(angleInDegrees);
        return (float) (Math.cos(angle) * (pX - cX) - Math.sin(angle) * (pY - cY) + cX);
    }

    /**
     * Rotate point P around center point C.
     *
     * @param pX             x coordinate of point P
     * @param pY             y coordinate of point P
     * @param cX             x coordinate of point C
     * @param cY             y coordinate of point C
     * @param angleInDegrees rotation angle in degrees
     * @return new y coordinate
     */
    public static float rotateY(float pX, float pY, float cX, float cY, float angleInDegrees) {
        double angle = Math.toRadians(angleInDegrees);
        return (float) (Math.sin(angle) * (pX - cX) + Math.cos(angle) * (pY - cY) + cY);
    }

    public static void flipAnimatorXViewShow(final View oldView, final View newView, final long time) {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationX", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationX", -90, 0);
        animator2.setInterpolator(new OvershootInterpolator(2.0f));

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(time).start();
                newView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(time).start();
    }

}
