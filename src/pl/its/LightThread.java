/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

import android.os.Handler;
import android.os.Message;
import java.util.Random;

/**
 *
 * @author Krystian
 */
public class LightThread extends Thread {

    public static int YELLOW = 3;
    public static int RED_AND_YELLOW = 2;
    Handler mHandler;
    int mTimeout;
    int mDelay;
    int mType;
    int mTime;
    RoadStream mStream1;
    RoadStream mStream2;
    Random mRandom;

    public LightThread(Handler handler, int type, int timeout, int delay, RoadStream stream, Random random) {
        mHandler = handler;
        mTimeout = timeout;
        mDelay = delay;
        mType = type;
        mStream1 = stream;
        mRandom = random;
    }

    public LightThread(Handler handler, int type, int timeout, int delay, RoadStream stream1, RoadStream stream2, Random random) {
        mHandler = handler;
        mTimeout = timeout;
        mDelay = delay;
        mType = type;
        mStream1 = stream1;
        mStream2 = stream2;
        mRandom = random;
    }

    @Override
    public void run() {
        try {
            mTime = 0;
            sleep((mDelay - RED_AND_YELLOW) * ITS.SPEED);
            mHandler.sendMessage(Message.obtain(mHandler, mType, Light.MSG_RED_AND_YELLOW));
            sleep(RED_AND_YELLOW * ITS.SPEED);
            mHandler.sendMessage(Message.obtain(mHandler, mType, Light.MSG_GREEN));
            do {
                if (mTime < 3) {
                    mStream1.decreaseCarsNumber(1);
                    if (mStream2 != null) {
                        mStream2.decreaseCarsNumber(1);
                    }
                } else if (mTime < 6) {
                    mStream1.decreaseCarsNumber(1 + mRandom.nextInt(2));
                    if (mStream2 != null) {
                        mStream2.decreaseCarsNumber(1 + mRandom.nextInt(2));
                    }
                } else if (mRandom.nextInt(3) < 1) {
                    mStream1.decreaseCarsNumber(1);
                    if (mStream2 != null) {
                        mStream2.decreaseCarsNumber(1);
                    }
                }
                sleep(ITS.SPEED);
                mTimeout--;
                mTime++;
            } while (mTimeout > 0);
            mHandler.sendMessage(Message.obtain(mHandler, mType, Light.MSG_YELLOW));
            sleep(YELLOW * ITS.SPEED);
            mHandler.sendMessage(Message.obtain(mHandler, mType, Light.MSG_RED));
        } catch (InterruptedException ex) {
        }
    }
}
