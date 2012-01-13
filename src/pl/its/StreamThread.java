/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

import android.util.Log;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author Krystian
 */
public class StreamThread extends Thread {

//    private static int TRAM_KRZYKI[][] = new int[][]{
//        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}}; 
    
    private boolean mWork;
    RoadStream mStream;
    long mTimestamp;
    Random mRandom;

    public StreamThread(RoadStream stream, long timestamp, Random random) {
        mStream = stream;
        mTimestamp = timestamp;
        mRandom = random;
        mWork = true;
    }

    @Override
    public void run() {
        try {
            while (mWork) {
                switch (mStream.getType()) {
                    case ITS.MSG_KRZYKI_SRODMIESCIE:
                        increaseKrzykiSrodmiescie();
                        break;
                    case ITS.MSG_KUTNOWSKA:
                        increaseKutnowska();
                        break;
                    case ITS.MSG_SRODMIESCIE_KUTNOWSKA:
                        increaseSrodmiescieKutnowska();
                        break;
                    case ITS.MSG_SRODMIESCIE_KRZYKI:
                        increaseSrodmiescieKrzyki();
                        break;
                    case ITS.MSG_TRAMWAJ_KRZYKI:
                        break;
                    case ITS.MSG_TRAMWAJ_SRODMIESCIE:
                        break;
                }
                mTimestamp += 1000;
                sleep(ITS.SPEED);
            }
        } catch (InterruptedException ex) {
        }
    }
    
    public synchronized void turnOff() {
        mWork = false;
    }

    public void increaseKrzykiSrodmiescie() {
        int hour = getHour();
        int random = mRandom.nextInt(61);
        if (hour < 6) {
            if (random < 10) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 8) {
            if (random < 17) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 10) {
            if (random < 36) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 15) {
            if (random < 23) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 18) {
            if (random < 30) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 21) {
            if (random < 23) {
                mStream.increaseCarsNumber(1);
            }
        } else {
            if (random < 12) {
                mStream.increaseCarsNumber(1);
            }
        }
    }

    public void increaseSrodmiescieKutnowska() {
        int hour = getHour();
        if (hour < 6) {
            int random = mRandom.nextInt(300);
            if (random < 2) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 8) {
            int random = mRandom.nextInt(61);
            if (random < 2) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 10) {
            int random = mRandom.nextInt(61);
            if (random < 5) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 15) {
            int random = mRandom.nextInt(101);
            if (random < 3) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 18) {
            int random = mRandom.nextInt(61);
            if (random < 5) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 21) {
            int random = mRandom.nextInt(61);
            if (random < 3) {
                mStream.increaseCarsNumber(1);
            }
        } else {
            int random = mRandom.nextInt(125);
            if (random < 1) {
                mStream.increaseCarsNumber(1);
            }
        }
    }

    public void increaseKutnowska() {
        int hour = getHour();
        if (hour < 6) {
            int random = mRandom.nextInt(360);
            if (random < 1) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 8) {
            int random = mRandom.nextInt(101);
            if (random < 2) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 10) {
            int random = mRandom.nextInt(101);
            if (random < 3) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 15) {
            int random = mRandom.nextInt(61);
            if (random < 1) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 18) {
            int random = mRandom.nextInt(111);
            if (random < 3) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 21) {
            int random = mRandom.nextInt(61);
            if (random < 1) {
                mStream.increaseCarsNumber(1);
            }
        } else {
            int random = mRandom.nextInt(361);
            if (random < 1) {
                mStream.increaseCarsNumber(1);
            }
        }
    }

    public void increaseSrodmiescieKrzyki() {
        int hour = getHour();
        int random = mRandom.nextInt(61);
        if (hour < 6) {
            if (random < 8) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 8) {
            if (random < 16) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 10) {
            if (random < 36) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 15) {
            if (random < 18) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 18) {
            if (random < 30) {
                mStream.increaseCarsNumber(1);
            }
        } else if (hour < 21) {
            if (random < 25) {
                mStream.increaseCarsNumber(1);
            }
        } else {
            if (random < 11) {
                mStream.increaseCarsNumber(1);
            }
        }
    }

    private int getHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTimestamp);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("ITS_hour", "Hour: " + hour);
        return hour;
    }
}
