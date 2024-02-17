package com.akp.aonestar.GoodLuckModule;

import com.akp.aonestar.R;

public class Wheel extends Thread {

    interface WheelListener {
        void newImage(int img);
    }

    private static int[] imgs = {R.drawable.num_0, R.drawable.num_1, R.drawable.num_2, R.drawable.num_3,
            R.drawable.num_4, R.drawable.num_5, R.drawable.num_6, R.drawable.num_7,R.drawable.num_8, R.drawable.num_9};
    public int currentIndex;
    private WheelListener wheelListener;
    private long frameDuration;
    private long startIn;
    private boolean isStarted;

    public Wheel(WheelListener wheelListener, long frameDuration, long startIn) {
        this.wheelListener = wheelListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        currentIndex = 0;
        isStarted = true;
    }

    public void nextImg() {
        currentIndex++;

        if (currentIndex == imgs.length) {
            currentIndex = 0;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }

        while(isStarted) {
            try {
                Thread.sleep(frameDuration);
            } catch (InterruptedException e) {
            }

            nextImg();

            if (wheelListener != null) {
                wheelListener.newImage(imgs[currentIndex]);
            }
        }
    }

    public void stopWheel() {
        isStarted = false;
    }
}