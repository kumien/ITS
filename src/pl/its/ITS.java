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

	public static final boolean LOG = false;
	public static final long SPEED = 10;
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
	private static int MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI = 9; // Do
																		// wyliczenia
	private static int MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KUTNOWSKA = 7; // Do
																			// wyliczenia
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message message) {
			int type = (Integer) message.obj;
			switch (message.what) {
			case MSG_KRZYKI_SRODMIESCIE:
				// if(type == Light.MSG_YELLOW) {
				// mTramwajThread = new LightThread(mHandler, MSG_TRAMWAJ,
				// TIME_TRAMWAJ, MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ);
				// mTramwajThread.start();
				// }
				mKrzykiSrodmiescie1.setLight(type);
				mKrzykiSrodmiescie2.setLight(type);
				break;
			case MSG_SRODMIESCIE_KRZYKI:
				if (type == Light.MSG_YELLOW) {
					mTramwajThread = new LightThread(mHandler, MSG_TRAMWAJ, TIME_TRAMWAJ,
							MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieTramwajStream, mRandom);
					mTramwajThread.start();
				}
				mSrodmiescieKrzyki1.setLight(type);
				mSrodmiescieKrzyki2.setLight(type);
				break;
			case MSG_SRODMIESCIE_KUTNOWSKA:
				if (type == Light.MSG_YELLOW) {
					mKrzykiSrodmiescieThread = new LightThread(mHandler, MSG_KRZYKI_SRODMIESCIE, TIME_KRZYKI_SRODMIESCIE,
							MIEDZYCZERWONE_SRODMIESCIE_KUTNOWSKA_Z_KRZYKI, mKrzykiSrodmiescie1Stream, mKrzykiSrodmiescie2Stream,
							mRandom);
					mKrzykiSrodmiescieThread.start();
				}
				mSrodmiescieKutnowska.setLight(type);
				break;
			case MSG_KUTNOWSKA:
				break;
			case MSG_TRAMWAJ:
				if (type == Light.MSG_YELLOW) {
					mSrodmiescieKrzykiThread = new LightThread(mHandler, MSG_SRODMIESCIE_KRZYKI, TIME_SRODMIESCIE_KRZYKI,
							MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI, mSrodmiescieKrzyki1Stream, mSrodmiescieKrzyki2Stream,
							mRandom);
					mSrodmiescieKutnowskaThread = new LightThread(mHandler, MSG_SRODMIESCIE_KUTNOWSKA,
							TIME_SRODMIESCIE_KUTNOWSKA, MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI + TIME_SRODMIESCIE_KRZYKI
									- TIME_SRODMIESCIE_KUTNOWSKA, mSrodmiescieKutnowskaStream, mRandom);
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
	Button mGenetic;
	LightThread mKrzykiSrodmiescieThread;
	LightThread mKutnowskaThread;
	LightThread mSrodmiescieKrzykiThread;
	LightThread mSrodmiescieKutnowskaThread;
	LightThread mTramwajThread;
	LeaveThread mKrzykiSrodmiescieLeaveThread;
	LeaveThread mKutnowskaLeaveThread;
	LeaveThread mSrodmiescieKrzykiLeaveThread;
	LeaveThread mSrodmiescieKutnowskaLeaveThread;
	LeaveThread mTramwajLeaveThread;
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
	
	Genetic mGeneticAlgorithm;

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

		mKrzykiSrodmiescie1Stream = new RoadStream(MSG_KRZYKI_SRODMIESCIE, 0);
		mKrzykiSrodmiescie2Stream = new RoadStream(MSG_KRZYKI_SRODMIESCIE, 1);
		mKutnowskaStream = new RoadStream(MSG_KUTNOWSKA, 0);
		mSrodmiescieKutnowskaStream = new RoadStream(MSG_SRODMIESCIE_KUTNOWSKA, 0);
		mSrodmiescieKrzyki1Stream = new RoadStream(MSG_SRODMIESCIE_KRZYKI, 0);
		mSrodmiescieKrzyki2Stream = new RoadStream(MSG_SRODMIESCIE_KRZYKI, 1);
		mKrzykiTramwajStream = new RoadStream(MSG_TRAMWAJ_KRZYKI, 0);
		mSrodmiescieTramwajStream = new RoadStream(MSG_TRAMWAJ_SRODMIESCIE, 0);

		mGeneticAlgorithm = new Genetic(new Random());
		new Thread(mGeneticAlgorithm).start();
		
		mStart = (Button) findViewById(R.id.start_stop);
		mStart.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				if (mTramwajThread == null) {
					Log.d("ITS", "Start");
					Calendar calendar = Calendar.getInstance();
					mRandom = new Random(calendar.getTimeInMillis());
					mTramwajThread = new LightThread(mHandler, MSG_TRAMWAJ, TIME_TRAMWAJ,
							MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mKrzykiTramwajStream, mRandom);
					mTramwajThread.start();
					mKrzykiSrodmiescie1StreamThread = new StreamThread(mKrzykiSrodmiescie1Stream, calendar.getTimeInMillis(),
							mRandom);
					mKrzykiSrodmiescie1StreamThread.start();
					mKrzykiSrodmiescie2StreamThread = new StreamThread(mKrzykiSrodmiescie2Stream, calendar.getTimeInMillis(),
							mRandom);
					mKrzykiSrodmiescie2StreamThread.start();
					mKutnowskaStreamThread = new StreamThread(mKutnowskaStream, calendar.getTimeInMillis(), mRandom);
					mKutnowskaStreamThread.start();
					mSrodmiescieKutnowskaStreamThread = new StreamThread(mSrodmiescieKutnowskaStream, calendar.getTimeInMillis(),
							mRandom);
					mSrodmiescieKutnowskaStreamThread.start();
					mSrodmiescieKrzyki1StreamThread = new StreamThread(mSrodmiescieKrzyki1Stream, calendar.getTimeInMillis(),
							mRandom);
					mSrodmiescieKrzyki1StreamThread.start();
					mSrodmiescieKrzyki2StreamThread = new StreamThread(mSrodmiescieKrzyki2Stream, calendar.getTimeInMillis(),
							mRandom);
					mSrodmiescieKrzyki2StreamThread.start();
					mKrzykiTramwajStreamThread = new StreamThread(mKrzykiTramwajStream, calendar.getTimeInMillis(), mRandom);
					mKrzykiTramwajStreamThread.start();
					mSrodmiescieTramwajStreamThread = new StreamThread(mSrodmiescieTramwajStream, calendar.getTimeInMillis(),
							mRandom);
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

		mStart = (Button) findViewById(R.id.start_stop);
		mStart.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				initStreams();
				mTramwajLeaveThread = new LeaveThread(TIME_TRAMWAJ, MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI,
						mSrodmiescieTramwajStream, mRandom);
				Log.i(TAG, "Start tramwaj");
				mTramwajLeaveThread.start();

				mRefreshTimer = new Timer();
				mRefreshTimer.schedule(new TimerTask() {

					@Override
					public void run() {

						// Aktywacja swiatla dla tramwaju
						if (mSrodmiescieKrzykiLeaveThread != null && mSrodmiescieKutnowskaLeaveThread != null) {
							if (mSrodmiescieKrzykiLeaveThread.isFinished() && mSrodmiescieKutnowskaLeaveThread.isFinished()) {
								if (mTramwajLeaveThread != null) {
									if (mTramwajLeaveThread.isFinished() && mTramwajLeaveThread.isUsed()) {
										mTramwajLeaveThread = new LeaveThread(TIME_TRAMWAJ,
												MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI, mSrodmiescieTramwajStream, mRandom);
										if (LOG) {
											Log.i(TAG, "Start tramwaj");
										}
										mTramwajLeaveThread.start();
									}
								} else {
									mTramwajLeaveThread = new LeaveThread(TIME_TRAMWAJ,
											MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI, mSrodmiescieTramwajStream, mRandom);
									if (LOG) {
										Log.i(TAG, "Start tramwaj (null state)");
									}
									mTramwajLeaveThread.start();
								}
							}
						}

						// Aktywacja swiatla ze srodmiescia na krzyki
						if (mTramwajLeaveThread != null) {
							if (mTramwajLeaveThread.isFinished()) {
								if (mSrodmiescieKrzykiLeaveThread != null) {
									if (mSrodmiescieKrzykiLeaveThread.isFinished()
											&& mSrodmiescieKutnowskaLeaveThread.isFinished()) {
										mSrodmiescieKrzykiLeaveThread = new LeaveThread(TIME_SRODMIESCIE_KRZYKI,
												MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKrzyki1Stream,
												mSrodmiescieKrzyki2Stream, mRandom);
										if (LOG) {
											Log.i(TAG, "Start Srodmiescie (Krzyki)");
										}
										mSrodmiescieKrzykiLeaveThread.start();
										mTramwajLeaveThread.use();
									}
								} else if (mSrodmiescieKutnowskaLeaveThread == null) {
									mSrodmiescieKrzykiLeaveThread = new LeaveThread(TIME_SRODMIESCIE_KRZYKI,
											MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKrzyki1Stream,
											mSrodmiescieKrzyki2Stream, mRandom);
									if (LOG) {
										Log.i(TAG, "Start Srodmiescie (Krzyki) with null");
									}
									mSrodmiescieKrzykiLeaveThread.start();
									mTramwajLeaveThread.use();
								}
							}
						}

						// Aktywacja swiatla ze srodmiescia w kutnowska
						if (mSrodmiescieKrzykiLeaveThread != null) {
							if (!mSrodmiescieKrzykiLeaveThread.isFinished()) {
								if (mSrodmiescieKrzykiLeaveThread.toEnd() <= (TIME_SRODMIESCIE_KUTNOWSKA + RED_AND_YELLOW)) {
									if (mSrodmiescieKutnowskaLeaveThread != null) {
										if (mSrodmiescieKutnowskaLeaveThread.isFinished()) {
											mSrodmiescieKutnowskaLeaveThread = new LeaveThread(TIME_SRODMIESCIE_KUTNOWSKA,
													MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKutnowskaStream,
													mRandom);
											if (LOG) {
												Log.i(TAG, "Start Srodmiescie (Kutnowska)");
											}
											mSrodmiescieKutnowskaLeaveThread.start();
										}
									} else {
										mSrodmiescieKutnowskaLeaveThread = new LeaveThread(TIME_SRODMIESCIE_KUTNOWSKA,
												MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKutnowskaStream, mRandom);
										if (LOG) {
											Log.i(TAG, "Start Srodmiescie (Kutnowska)");
										}
										mSrodmiescieKutnowskaLeaveThread.start();
									}
								}
							}
						}

						// Aktywacja swiatla z krzykow do srodmiescia
						if (mSrodmiescieKutnowskaLeaveThread != null) {
							if (mSrodmiescieKutnowskaLeaveThread.isFinished()) {
								if (mKrzykiSrodmiescieLeaveThread != null) {
									if (mKrzykiSrodmiescieLeaveThread.isFinished() && !mSrodmiescieKutnowskaLeaveThread.isUsed()) {
										mKrzykiSrodmiescieLeaveThread = new LeaveThread(TIME_KRZYKI_SRODMIESCIE,
												MIEDZYCZERWONE_KRZYKI_Z_SRODMIESCIE_KUTNOWSKA, mKrzykiSrodmiescie1Stream,
												mKrzykiSrodmiescie2Stream, mRandom);
										if (LOG) {
											Log.i(TAG, "Start Krzyki (Srodmiescie)");
										}
										mKrzykiSrodmiescieLeaveThread.start();
										mSrodmiescieKutnowskaLeaveThread.use();
									}
								} else {
									mKrzykiSrodmiescieLeaveThread = new LeaveThread(TIME_KRZYKI_SRODMIESCIE,
											MIEDZYCZERWONE_KRZYKI_Z_SRODMIESCIE_KUTNOWSKA, mKrzykiSrodmiescie1Stream,
											mKrzykiSrodmiescie2Stream, mRandom);
									if (LOG) {
										Log.i(TAG, "Start Krzyki (Srodmiescie)");
									}
									mKrzykiSrodmiescieLeaveThread.start();
									mSrodmiescieKutnowskaLeaveThread.use();
								}
							}
						}
					}
				}, 0, SPEED);
			}
		});
		
		mGenetic = (Button) findViewById(R.id.start_genetic);
		mGenetic.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				initStreams();
				mTramwajLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
						mKrzykiTramwajStream.getCarsNumber()*10 + mSrodmiescieTramwajStream.getCarsNumber()*10,
						mSrodmiescieKrzyki1Stream.getCarsNumber() + mSrodmiescieKrzyki2Stream.getCarsNumber()
						+ mSrodmiescieKutnowskaStream.getCarsNumber()),
						MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI,
						mSrodmiescieTramwajStream, mRandom);
				Log.i(TAG, "Start tramwaj");
				mTramwajLeaveThread.start();

				mRefreshTimer = new Timer();
				mRefreshTimer.schedule(new TimerTask() {

					@Override
					public void run() {

						// Aktywacja swiatla dla tramwaju
						if (mSrodmiescieKrzykiLeaveThread != null && mSrodmiescieKutnowskaLeaveThread != null) {
							if (mSrodmiescieKrzykiLeaveThread.isFinished() && mSrodmiescieKutnowskaLeaveThread.isFinished()) {
								if (mTramwajLeaveThread != null) {
									mGeneticAlgorithm.nextPopulation();
									if (mTramwajLeaveThread.isFinished() && mTramwajLeaveThread.isUsed()) {
										mTramwajLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
												mKrzykiTramwajStream.getCarsNumber()*10 + mSrodmiescieTramwajStream.getCarsNumber()*10,
												mSrodmiescieKrzyki1Stream.getCarsNumber() + mSrodmiescieKrzyki2Stream.getCarsNumber()
												+ mSrodmiescieKutnowskaStream.getCarsNumber()),
												MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI,
												mSrodmiescieTramwajStream, mRandom);
										if (LOG) {
											Log.i(TAG, "Start tramwaj");
										}
										mTramwajLeaveThread.start();
									}
								} else {
									mTramwajLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
											mKrzykiTramwajStream.getCarsNumber()*10 + mSrodmiescieTramwajStream.getCarsNumber()*10,
											mSrodmiescieKrzyki1Stream.getCarsNumber() + mSrodmiescieKrzyki2Stream.getCarsNumber()
											+ mSrodmiescieKutnowskaStream.getCarsNumber()),
											MIEDZYCZERWONE_TRAMWAJ_Z_SRODMIESCIE_KRZYKI,
											mSrodmiescieTramwajStream, mRandom);
									if (LOG) {
										Log.i(TAG, "Start tramwaj (null state)");
									}
									mTramwajLeaveThread.start();
								}
							}
						}

						// Aktywacja swiatla ze srodmiescia na krzyki
						if (mTramwajLeaveThread != null) {
							if (mTramwajLeaveThread.isFinished()) {
								if (mSrodmiescieKrzykiLeaveThread != null) {
									if (mSrodmiescieKrzykiLeaveThread.isFinished() && mSrodmiescieKutnowskaLeaveThread.isFinished()) {
										mSrodmiescieKrzykiLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
												mSrodmiescieKrzyki1Stream.getCarsNumber() + mSrodmiescieKrzyki2Stream.getCarsNumber(),
												mKrzykiTramwajStream.getCarsNumber()*10 + mSrodmiescieTramwajStream.getCarsNumber()*10),
												MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKrzyki1Stream,
												mSrodmiescieKrzyki2Stream, mRandom);
										if (LOG) {
											Log.i(TAG, "Start Srodmiescie (Krzyki)");
										}
										mSrodmiescieKrzykiLeaveThread.start();
										mTramwajLeaveThread.use();
									}
								} else if (mSrodmiescieKutnowskaLeaveThread == null) {
									mSrodmiescieKrzykiLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
											mSrodmiescieKrzyki1Stream.getCarsNumber() + mSrodmiescieKrzyki2Stream.getCarsNumber(),
											mKrzykiTramwajStream.getCarsNumber()*10 + mSrodmiescieTramwajStream.getCarsNumber()*10),
											MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKrzyki1Stream,
											mSrodmiescieKrzyki2Stream, mRandom);
									if (LOG) {
										Log.i(TAG, "Start Srodmiescie (Krzyki) with null");
									}
									mSrodmiescieKrzykiLeaveThread.start();
									mTramwajLeaveThread.use();
								}
							}
						}

						// Aktywacja swiatla ze srodmiescia w kutnowska
						if (mSrodmiescieKrzykiLeaveThread != null) {
							if (!mSrodmiescieKrzykiLeaveThread.isFinished()) {
									if (mSrodmiescieKutnowskaLeaveThread != null) {
										if (mSrodmiescieKutnowskaLeaveThread.isFinished() && mSrodmiescieKutnowskaLeaveThread.isUsed()) {
											mSrodmiescieKutnowskaLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
													mSrodmiescieKutnowskaStream.getCarsNumber(), mKrzykiTramwajStream.getCarsNumber()*10
													+ mSrodmiescieTramwajStream.getCarsNumber()*10
													+ mKrzykiSrodmiescie1Stream.getCarsNumber()
													+ mKrzykiSrodmiescie2Stream.getCarsNumber()),
													MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKutnowskaStream,
													mRandom);
											if (LOG) {
												Log.i(TAG, "Start Srodmiescie (Kutnowska)");
											}
											mSrodmiescieKutnowskaLeaveThread.start();
										}
									} else {
										mSrodmiescieKutnowskaLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
												mSrodmiescieKutnowskaStream.getCarsNumber(), mKrzykiTramwajStream.getCarsNumber()*10
												+ mSrodmiescieTramwajStream.getCarsNumber()*10
												+ mKrzykiSrodmiescie1Stream.getCarsNumber()
												+ mKrzykiSrodmiescie2Stream.getCarsNumber()),
												MIEDZYCZERWONE_SRODMIESCIE_KRZYKI_Z_TRAMWAJ, mSrodmiescieKutnowskaStream,
												mRandom);
										if (LOG) {
											Log.i(TAG, "Start Srodmiescie (Kutnowska)");
										}
										mSrodmiescieKutnowskaLeaveThread.start();
									}
							}
						}
						
						// Aktywacja swiatla z kutnowska
						if (mSrodmiescieKrzykiLeaveThread != null) {
							if (!mSrodmiescieKrzykiLeaveThread.isFinished()) {
									if (mKutnowskaLeaveThread != null) {
										if (mKutnowskaLeaveThread.isFinished() && mKutnowskaLeaveThread.isUsed()) {
											mKutnowskaLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
													mKutnowskaStream.getCarsNumber(), mKrzykiSrodmiescie1Stream.getCarsNumber()
													+ mKrzykiSrodmiescie2Stream.getCarsNumber()),
													MIEDZYCZERWONE_KUTNOWSKA_Z_KRZYKI, mKutnowskaStream,
													mRandom);
											if (LOG) {
												Log.i(TAG, "Start Kutnowska");
											}
											mKutnowskaLeaveThread.start();
										}
									} else {
										mKutnowskaLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
												mKutnowskaStream.getCarsNumber(), mKrzykiSrodmiescie1Stream.getCarsNumber()
												+ mKrzykiSrodmiescie2Stream.getCarsNumber()),
												MIEDZYCZERWONE_KUTNOWSKA_Z_KRZYKI, mKutnowskaStream,
												mRandom);
										if (LOG) {
											Log.i(TAG, "Start Kutnowska");
										}
										mKutnowskaLeaveThread.start();
									}
							}
						}

						// Aktywacja swiatla z krzykow do srodmiescia
						if (mSrodmiescieKutnowskaLeaveThread != null && mKutnowskaLeaveThread!=null) {
							if (mSrodmiescieKutnowskaLeaveThread.isFinished()) {
								if (mKrzykiSrodmiescieLeaveThread != null) {
									if (mKrzykiSrodmiescieLeaveThread.isFinished() && !mSrodmiescieKutnowskaLeaveThread.isUsed()
											 && !mKutnowskaLeaveThread.isUsed()) {
										mKrzykiSrodmiescieLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
												mKrzykiSrodmiescie1Stream.getCarsNumber() + mKrzykiSrodmiescie2Stream.getCarsNumber(),
												mKutnowskaStream.getCarsNumber() + mSrodmiescieKutnowskaStream.getCarsNumber()),
												MIEDZYCZERWONE_KRZYKI_Z_SRODMIESCIE_KUTNOWSKA, mKrzykiSrodmiescie1Stream,
												mKrzykiSrodmiescie2Stream, mRandom);
										if (LOG) {
											Log.i(TAG, "Start Krzyki (Srodmiescie)");
										}
										mKrzykiSrodmiescieLeaveThread.start();
										mSrodmiescieKutnowskaLeaveThread.use();
										mKutnowskaLeaveThread.use();
									}
								} else {
									mKrzykiSrodmiescieLeaveThread = new LeaveThread(mGeneticAlgorithm.getBestIndividualTime(
											mKrzykiSrodmiescie1Stream.getCarsNumber() + mKrzykiSrodmiescie2Stream.getCarsNumber(),
											mKutnowskaStream.getCarsNumber() + mSrodmiescieKutnowskaStream.getCarsNumber()),
											MIEDZYCZERWONE_KRZYKI_Z_SRODMIESCIE_KUTNOWSKA, mKrzykiSrodmiescie1Stream,
											mKrzykiSrodmiescie2Stream, mRandom);
									if (LOG) {
										Log.i(TAG, "Start Krzyki (Srodmiescie)");
									}
									mKrzykiSrodmiescieLeaveThread.start();
									mSrodmiescieKutnowskaLeaveThread.use();
									mKutnowskaLeaveThread.use();
								}
							}
						}
					}
				}, 0, SPEED);
			}
		});

	}

	public void initStreams() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		mRandom = new Random(calendar.getTimeInMillis());
		mKrzykiSrodmiescie1StreamThread = new StreamThread(mKrzykiSrodmiescie1Stream, calendar.getTimeInMillis(), mRandom);
		mKrzykiSrodmiescie2StreamThread = new StreamThread(mKrzykiSrodmiescie2Stream, calendar.getTimeInMillis(), mRandom);
		mKutnowskaStreamThread = new StreamThread(mKutnowskaStream, calendar.getTimeInMillis(), mRandom);
		mSrodmiescieKutnowskaStreamThread = new StreamThread(mSrodmiescieKutnowskaStream, calendar.getTimeInMillis(), mRandom);
		mSrodmiescieKrzyki1StreamThread = new StreamThread(mSrodmiescieKrzyki1Stream, calendar.getTimeInMillis(), mRandom);
		mSrodmiescieKrzyki2StreamThread = new StreamThread(mSrodmiescieKrzyki2Stream, calendar.getTimeInMillis(), mRandom);
		mKrzykiTramwajStreamThread = new StreamThread(mKrzykiTramwajStream, calendar.getTimeInMillis(), mRandom);
		mSrodmiescieTramwajStreamThread = new StreamThread(mSrodmiescieTramwajStream, calendar.getTimeInMillis(), mRandom);

		mKrzykiSrodmiescie1StreamThread.start();
		mKrzykiSrodmiescie2StreamThread.start();
		mKutnowskaStreamThread.start();
		mSrodmiescieKutnowskaStreamThread.start();
		mSrodmiescieKrzyki1StreamThread.start();
		mSrodmiescieKrzyki2StreamThread.start();
		mKrzykiTramwajStreamThread.start();
		mSrodmiescieTramwajStreamThread.start();
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
