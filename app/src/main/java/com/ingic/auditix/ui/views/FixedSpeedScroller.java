package com.ingic.auditix.ui.views;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {

    private int mDuration = 400;
    private int width = 1;
    private int pagemargin = 1;
    private float pagewidth = 1;
    private int velocity;
    private boolean isGivenDuration = false;

    public FixedSpeedScroller(Context context, int width, int pagemargin, float pagewidth, int velocity) {
        super(context);
        this.width = width;
        this.pagemargin = pagemargin;
        this.pagewidth = pagewidth;
        this.velocity = velocity;
    }

    public FixedSpeedScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
        isGivenDuration = true;
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    private int getduarion(int dx) {
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
        final float distance = halfWidth + halfWidth
                * distanceInfluenceForSnapDuration(distanceRatio);

        int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float pageWidth = width * pagewidth;
            final float pageDelta = (float) Math.abs(dx) / (pageWidth + pageWidth);
            duration = (int) ((pageDelta + 1) * 100);
        }
        duration = Math.min(duration, 600);
        return duration;
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        int dur = isGivenDuration ? mDuration : getduarion(dx);
        super.startScroll(startX, startY, dx, dy, dur);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        int dur = isGivenDuration ? mDuration : getduarion(dx);
        super.startScroll(startX, startY, dx, dy, dur);
    }
}
