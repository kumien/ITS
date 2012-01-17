package pl.its;

import java.util.Random;


public class LeaveThread extends Thread {

	public static int YELLOW = 3;
	public static int RED_AND_YELLOW = 2;
	int mTimeout;
	int mDelay;
	int mType;
	int mTime;
	RoadStream mStream1;
	RoadStream mStream2;
	Random mRandom;
	private boolean mFinished;
	private boolean mUsed;
	

	public LeaveThread(int timeout, int middleRed, RoadStream stream, Random random) {
		mTimeout = timeout;
		mDelay = middleRed;
		mStream1 = stream;
		mRandom = random;
		mFinished = false;
		mUsed = false;
		stream.increaseSumGreenLight(timeout);
	}

	public LeaveThread(int timeout, int middleRed,
			RoadStream stream1, RoadStream stream2, Random random) {
		mTimeout = timeout;
		mDelay = middleRed;
		mStream1 = stream1;
		mStream2 = stream2;
		mRandom = random;
		mFinished = false;
		mUsed = false;
		stream1.increaseSumGreenLight(timeout);
		stream2.increaseSumGreenLight(timeout);
	}

	public void run() {
		try {
			mTime = 0;
			mStream1.setEfficiencyTime(mTimeout);
			mStream1.increaseCycleCounter();
			if(mStream2!=null) {
				mStream2.setEfficiencyTime(mTimeout);
				mStream2.increaseCycleCounter();
			}
			if ((mDelay - RED_AND_YELLOW) < 0) {
				sleep((RED_AND_YELLOW) * ITS.SPEED);
			} else {
				sleep((mDelay) * ITS.SPEED);
			}
			do {
				if (mTime < 3) {
					mStream1.decreaseCarsNumber(1);
					if (mStream2 != null) {
						mStream2.decreaseCarsNumber(1);
					}
				} else if (mTime < 8) {
					mStream1.decreaseCarsNumber(2 + mRandom.nextInt(2));
					if (mStream2 != null) {
						mStream2.decreaseCarsNumber(2 + mRandom.nextInt(2));
					}
				} else if (mRandom.nextInt(4) < 3) {
					mStream1.decreaseCarsNumber(1);
					if (mStream2 != null) {
						mStream2.decreaseCarsNumber(1);
					}
				}
				sleep(ITS.SPEED);
				mTimeout--;
				mTime++;
			} while (mTimeout > 0);
			mStream1.increaseEfficiency();
			if(mStream2!=null) {
				mStream2.increaseEfficiency();
			}
			sleep((YELLOW) * ITS.SPEED);
			mFinished = true;
		} catch (InterruptedException ex) {
		}
	}
	
	public synchronized boolean isFinished() {
		return mFinished;
	}
	
	public synchronized boolean isUsed() {
		return mUsed;
	}
	
	public synchronized void use() {
		mUsed = true;
	}
	
	public synchronized int toEnd() {
		return mTimeout;
	}
}
