package kr.pe.burt.android.lib.faimageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

import kr.pe.burt.android.lib.androidchannel.Timer;

/**
 * Created by burt on 15. 10. 22..
 */
public class FAImageView extends ImageView {

    private final static int DEFAULT_INTERVAL = 1000;       // 1s

    Timer timer;
    int interval = DEFAULT_INTERVAL;

    ArrayList<Drawable> drawableList;
    int currentFrameIndex = 0;
    boolean loop = false;
    int animationRepeatCount = 1;
    boolean restoreFirstFrameWhenFinishAnimation = true;


    public FAImageView(Context context) {
        this(context, null);
    }

    public FAImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FAImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * set inteval in milli seconds
     * @param interval
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    @SuppressWarnings("deprecation")
    public void addImageFrame(int resId) {

        if(drawableList == null) {
            drawableList = new ArrayList<>();
        }
        drawableList.add(getContext().getResources().getDrawable(resId));

    }

    public void startAnimation() {

        if(drawableList == null || drawableList.size() == 0) {
            throw new IllegalStateException("You shoud add frame at least one frame");
        }


        currentFrameIndex = 0;
        setImageDrawable(drawableList.get(currentFrameIndex));


        if(timer == null) {
            timer = new Timer(interval, new Timer.OnTimer() {
                @Override
                public void onTime(Timer timer) {
                    currentFrameIndex++;
                    if(currentFrameIndex == drawableList.size()) {
                        if(loop) {
                            currentFrameIndex = 0;
                        } else {
                            animationRepeatCount--;

                            if(animationRepeatCount == 0) {
                                if (restoreFirstFrameWhenFinishAnimation) {
                                    currentFrameIndex = 0;
                                } else {
                                    currentFrameIndex = drawableList.size() - 1;
                                }
                                stopAnimaion();
                            } else {
                                currentFrameIndex = 0;
                            }
                        }
                    }
                    setImageDrawable(drawableList.get(currentFrameIndex));
                }
            });
            timer.stop();
        }
        if(timer.isAlive() == false) {
            timer.start();
        }
    }

    public void stopAnimaion() {
        if(timer != null && timer.isAlive()) {
            timer.stop();
        }
        timer = null;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setRestoreFirstFrameWhenFinishAnimation(boolean restore) {
        this.restoreFirstFrameWhenFinishAnimation = restore;
    }

    public void setAnimationRepeatCount(int animationRepeatCount) {
        this.animationRepeatCount = animationRepeatCount;
    }

    public void reset() {
        stopAnimaion();
        drawableList.clear();
        drawableList = null;
    }
}
