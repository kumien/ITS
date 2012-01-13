package pl.its;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ITS extends Activity {

    public static final long SPEED = 100;
    public static final String TAG = "ITS";
    public static final int MSG_KRZYKI_SRODMIESCIE = 0;
    public static final int MSG_SRODMIESCIE_KRZYKI = 1;
    public static final int MSG_SRODMIESCIE_KUTNOWSKA = 2;
    public static final int MSG_KUTNOWSKA = 3;
    public static final int MSG_TRAMWAJ = 4;
    public static final int MSG_TRAMWAJ_KRZYKI = 5;
    public static final int MSG_TRAMWAJ_SRODMIESCIE = 6;
    public static int YELLOW = 3;
    public static int RED_AND_YELLOW = 2;
    private static int TIME_KRZYKI_SRODMIESCIE = 65;
    private static int TIME_SRODMIESCIE_KRZYKI = 60;
    private static int TIME_SRODMIESCIE_KUTNOWSKA = 10;
    private static int TIME_TRAMWAJ = 25;
    private static int MIEDZYCZERWONE_KRZYKI_Z_KUTNOWSKA = 4;
    private static int MIEDZYCZERWONE_KRZYKI_Z_SRODMIESCIE_KUTNOWSKA = 3;
    private static int MIEDZYCZERWONE_SRODMIESCIE_KUTNOWSKA_Z_KRZYKI = 12;
    private static int MIEDZYCZERWONE_SRODMIESCIE_KUTNOWSKA_Z_TRAMWAJ = 7;
    private static int MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ = 9;
    private static int MIEDZYCZERWONE_KUTNOWSKA_Z_KRZYKI = 5;
    private static int MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI = 9;       //Do wyliczenia
    private static int MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KUTNOWSKA = 7;    //Do wyliczenia
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            int type = (Integer) message.obj;
            switch (message.what) {
                case MSG_KRZYKI_SRODMIESCIE:
//                    if(type == Light.MSG_YELLOW) {
//                        mTramwajThread = new LightThread(mHandler, MSG_TRAMWAJ, TIME_TRAMWAJ, MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ);
//                        mTramwajThread.start();
//                    }
                    mKrzykiSrodmiescie1.setLight(type);
                    mKrzykiSrodmiescie2.setLight(type);
                    break;
                case MSG_SRODMIESCIE_KRZYKI:
                    if (type == Light.MSG_YELLOW) {
                        mTramwajThread = new LightThread(
                                mHandler,
                                MSG_TRAMWAJ,
                                TIME_TRAMWAJ,
                                MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ,
                                mSrodmiescieTramwajStream, mRandom);
                        mTramwajThread.start();
                    }
                    mSrodmiescieKrzyki1.setLight(type);
                    mSrodmiescieKrzyki2.setLight(type);
                    break;
                case MSG_SRODMIESCIE_KUTNOWSKA:
                    if (type == Light.MSG_YELLOW) {
                        mKrzykiSrodmiescieThread = new LightThread(
                                mHandler,
                                MSG_KRZYKI_SRODMIESCIE,
                                TIME_KRZYKI_SRODMIESCIE,
                                MIEDZYCZERWONE_SRODMIESCIE_KUTNOWSKA_Z_KRZYKI,
                                mKrzykiSrodmiescie1Stream,
                                mKrzykiSrodmiescie2Stream,
                                mRandom);
                        mKrzykiSrodmiescieThread.start();
                    }
                    mSrodmiescieKutnowska.setLight(type);
                    break;
                case MSG_KUTNOWSKA:
                    break;
                case MSG_TRAMWAJ:
                    if (type == Light.MSG_YELLOW) {
                        mSrodmiescieKrzykiThread = new LightThread(
                                mHandler,
                                MSG_SRODMIESCIE_KRZYKI,
                                TIME_SRODMIESCIE_KRZYKI,
                                MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI,
                                mSrodmiescieKrzyki1Stream,
                                mSrodmiescieKrzyki2Stream,
                                mRandom);
                        mSrodmiescieKutnowskaThread = new LightThread(
                                mHandler,
                                MSG_SRODMIESCIE_KUTNOWSKA,
                                TIME_SRODMIESCIE_KUTNOWSKA,
                                MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI + TIME_SRODMIESCIE_KRZYKI - TIME_SRODMIESCIE_KUTNOWSKA,
                                mSrodmiescieKutnowskaStream,
                                mRandom);
                        mSrodmiescieKrzykiThread.start();
                        mSrodmiescieKutnowskaThread.start();
                    }
                    mKrzykiTramwaj.setLight(type);
                    mSrodmiescieTramwaj.setLight(type);
                    break;
            }
        }
    };
    Light mKrzykiSrodmiescie1;
    Light mKrzykiSrodmiescie2;
    Light mKutnowska;
    Light mSrodmiescieKutnowska;
    Light mSrodmiescieKrzyki1;
    Light mSrodmiescieKrzyki2;
    Light mKrzykiTramwaj;
    Light mSrodmiescieTramwaj;
    Badge mKrzykiSrodmiescie1Badge;
    Badge mKrzykiSrodmiescie2Badge;
    Badge mKutnowskaBadge;
    Badge mSrodmiescieKutnowskaBadge;
    Badge mSrodmiescieKrzyki1Badge;
    Badge mSrodmiescieKrzyki2Badge;
    Badge mKrzykiTramwajBadge;
    Badge mSrodmiescieTramwajBadge;
    Button mStart;
    LightThread mKrzykiSrodmiescieThread;
    LightThread mKutnowskaThread;
    LightThread mSrodmiescieKrzykiThread;
    LightThread mSrodmiescieKutnowskaThread;
    LightThread mTramwajThread;
    RoadStream mKrzykiSrodmiescie1Stream;
    RoadStream mKrzykiSrodmiescie2Stream;
    RoadStream mKutnowskaStream;
    RoadStream mSrodmiescieKutnowskaStream;
    RoadStream mSrodmiescieKrzyki1Stream;
    RoadStream mSrodmiescieKrzyki2Stream;
    RoadStream mKrzykiTramwajStream;
    RoadStream mSrodmiescieTramwajStream;
    StreamThread mKrzykiSrodmiescie1StreamThread;
    StreamThread mKrzykiSrodmiescie2StreamThread;
    StreamThread mKutnowskaStreamThread;
    StreamThread mSrodmiescieKutnowskaStreamThread;
    StreamThread mSrodmiescieKrzyki1StreamThread;
    StreamThread mSrodmiescieKrzyki2StreamThread;
    StreamThread mKrzykiTramwajStreamThread;
    StreamThread mSrodmiescieTramwajStreamThread;
    PowerManager mPowerManager;
    PowerManager.WakeLock mWakeLock;
    Random mRandom;
    Timer mRefreshTimer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mKrzykiSrodmiescie1 = (Light) findViewById(R.id.krzyki_srodmiescie_1);
        mKrzykiSrodmiescie2 = (Light) findViewById(R.id.krzyki_srodmiescie_2);
        mKutnowska = (Light) findViewById(R.id.kutnowska);
        mSrodmiescieKutnowska = (Light) findViewById(R.id.srodmiescie_kutnowska);
        mSrodmiescieKrzyki1 = (Light) findViewById(R.id.srodmiescie_krzyki_1);
        mSrodmiescieKrzyki2 = (Light) findViewById(R.id.srodmiescie_krzyki_2);
        mKrzykiTramwaj = (Light) findViewById(R.id.krzyki_tramwaj);
        mSrodmiescieTramwaj = (Light) findViewById(R.id.srodmiescie_tramwaj);

        mKrzykiSrodmiescie1Badge = (Badge) findViewById(R.id.badge_krzyki_srodmiescie_1);
        mKrzykiSrodmiescie2Badge = (Badge) findViewById(R.id.badge_krzyki_srodmiescie_2);
        mKutnowskaBadge = (Badge) findViewById(R.id.badge_kutnowska);
        mSrodmiescieKutnowskaBadge = (Badge) findViewById(R.id.badge_srodmiescie_kutnowska);
        mSrodmiescieKrzyki1Badge = (Badge) findViewById(R.id.badge_srodmiescie_krzyki_1);
        mSrodmiescieKrzyki2Badge = (Badge) findViewById(R.id.badge_srodmiescie_krzyki_2);
        mKrzykiTramwajBadge = (Badge) findViewById(R.id.badge_krzyki_tramwaj);
        mSrodmiescieTramwajBadge = (Badge) findViewById(R.id.badge_srodmiescie_tramwaj);

        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        mWakeLock.acquire();


        mKrzykiSrodmiescie1Stream = new RoadStream(MSG_KRZYKI_SRODMIESCIE);
        mKrzykiSrodmiescie2Stream = new RoadStream(MSG_KRZYKI_SRODMIESCIE);
        mKutnowskaStream = new RoadStream(MSG_KUTNOWSKA);
        mSrodmiescieKutnowskaStream = new RoadStream(MSG_SRODMIESCIE_KUTNOWSKA);
        mSrodmiescieKrzyki1Stream = new RoadStream(MSG_SRODMIESCIE_KRZYKI);
        mSrodmiescieKrzyki2Stream = new RoadStream(MSG_SRODMIESCIE_KRZYKI);
        mKrzykiTramwajStream = new RoadStream(MSG_TRAMWAJ_KRZYKI);
        mSrodmiescieTramwajStream = new RoadStream(MSG_TRAMWAJ_SRODMIESCIE);

        mStart = (Button) findViewById(R.id.start_stop);
        mStart.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                if (mTramwajThread == null) {
                    Log.d("ITS", "Start");
                    Calendar calendar = Calendar.getInstance();
                    mRandom = new Random(calendar.getTimeInMillis());
                    mTramwajThread = new LightThread(
                            mHandler,
                            MSG_TRAMWAJ,
                            TIME_TRAMWAJ,
                            MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ,
                            mKrzykiTramwajStream,
                            mRandom);
                    mTramwajThread.start();
                    mKrzykiSrodmiescie1StreamThread = new StreamThread(mKrzykiSrodmiescie1Stream, calendar.getTimeInMillis(), mRandom);
                    mKrzykiSrodmiescie1StreamThread.start();
                    mKrzykiSrodmiescie2StreamThread = new StreamThread(mKrzykiSrodmiescie2Stream, calendar.getTimeInMillis(), mRandom);
                    mKrzykiSrodmiescie2StreamThread.start();
                    mKutnowskaStreamThread = new StreamThread(mKutnowskaStream, calendar.getTimeInMillis(), mRandom);
                    mKutnowskaStreamThread.start();
                    mSrodmiescieKutnowskaStreamThread = new StreamThread(mSrodmiescieKutnowskaStream, calendar.getTimeInMillis(), mRandom);
                    mSrodmiescieKutnowskaStreamThread.start();
                    mSrodmiescieKrzyki1StreamThread = new StreamThread(mSrodmiescieKrzyki1Stream, calendar.getTimeInMillis(), mRandom);
                    mSrodmiescieKrzyki1StreamThread.start();
                    mSrodmiescieKrzyki2StreamThread = new StreamThread(mSrodmiescieKrzyki2Stream, calendar.getTimeInMillis(), mRandom);
                    mSrodmiescieKrzyki2StreamThread.start();
                    mKrzykiTramwajStreamThread = new StreamThread(mKrzykiTramwajStream, calendar.getTimeInMillis(), mRandom);
                    mKrzykiTramwajStreamThread.start();
                    mSrodmiescieTramwajStreamThread = new StreamThread(mSrodmiescieTramwajStream, calendar.getTimeInMillis(), mRandom);
                    mSrodmiescieTramwajStreamThread.start();
                    mRefreshTimer = new Timer();
                    mRefreshTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Log.d("ITS", "Refresh badge");
                                    mKrzykiSrodmiescie1Badge.setBadge(mKrzykiSrodmiescie1Stream.getCarsNumber());
                                    mKrzykiSrodmiescie2Badge.setBadge(mKrzykiSrodmiescie2Stream.getCarsNumber());
                                    mKutnowskaBadge.setBadge(mKutnowskaStream.getCarsNumber());
                                    mSrodmiescieKutnowskaBadge.setBadge(mSrodmiescieKutnowskaStream.getCarsNumber());
                                    mSrodmiescieKrzyki1Badge.setBadge(mSrodmiescieKrzyki1Stream.getCarsNumber());
                                    mSrodmiescieKrzyki2Badge.setBadge(mSrodmiescieKrzyki2Stream.getCarsNumber());
                                    mKrzykiTramwajBadge.setBadge(mKrzykiTramwajStream.getCarsNumber());
                                    mSrodmiescieTramwajBadge.setBadge(mSrodmiescieTramwajStream.getCarsNumber());

                                }
                            });
                        }
                    }, 0, SPEED);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
        if (mRefreshTimer != null) {
            mKrzykiSrodmiescie1StreamThread.turnOff();
            mKrzykiSrodmiescie2StreamThread.turnOff();
            mKutnowskaStreamThread.turnOff();
            mSrodmiescieKutnowskaStreamThread.turnOff();
            mSrodmiescieKrzyki1StreamThread.turnOff();
            mSrodmiescieKrzyki2StreamThread.turnOff();
            mKrzykiTramwajStreamThread.turnOff();
            mSrodmiescieTramwajStreamThread.turnOff();
            mRefreshTimer.cancel();
        }
    }
}
