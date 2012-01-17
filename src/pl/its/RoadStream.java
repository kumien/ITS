/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

import android.util.Log;

/**
 * 
 * @author Krystian
 */
public class RoadStream {
	private int mType;
	private int mTypeNumber;
	private int mCarsNumber;
	private int mIncomingCars;
	private int mLeaveCars;
	private int mCycleCounter;
	private int mSumGreenTime;
	private int mQuickLeaveCars;
	private int mTime;
	private float mSumEfficiency;

	public RoadStream(int type, int typeNumber) {
		mType = type;
		mTypeNumber = typeNumber;
		mCarsNumber = 0;
		mLeaveCars = 0;
		mIncomingCars = 0;
		mCycleCounter = 0;
		mSumGreenTime = 0;
		mQuickLeaveCars = 0;
		mTime = 0;
		mSumEfficiency = 0;
	}

	public synchronized void increaseCarsNumber(int number) {
		mCarsNumber += number;
		mIncomingCars += number;
	}

	public synchronized void decreaseCarsNumber(int number) {
		if ((mCarsNumber - number) < 0) {
			increaseLeaveCars(mCarsNumber);
			mCarsNumber = 0;

		} else {
			increaseLeaveCars(number);
			mCarsNumber -= number;
		}
	}

	public synchronized int getCarsNumber() {
		return mCarsNumber;
	}
	
	public synchronized int getIncomingCars() {
		return mIncomingCars;
	}
	
	public synchronized void clearIncomingCars() {
		mIncomingCars = 0;
	}

	public int getType() {
		return mType;
	}

	public synchronized int getLeaveCars() {
		return mLeaveCars;
	}

	public synchronized void increaseLeaveCars(int number) {
		mLeaveCars += number;
		mQuickLeaveCars += number;
	}
	
	public synchronized void clearLeaveCars() {
		mLeaveCars = 0;
	}

	public synchronized int getCycleCounter() {
		return mCycleCounter;
	}

	public synchronized void increaseCycleCounter() {
		mCycleCounter++;
	}
	
	public synchronized void clearCycleCounter() {
		mCycleCounter = 0;
	}

	public synchronized int getCSumGreenLight() {
		return mSumGreenTime;
	}

	public synchronized void increaseSumGreenLight(int time) {
		mSumGreenTime+=time;
	}
	
	public synchronized void clearSumGreenLight() {
		mSumGreenTime = 0;
	}

	public synchronized void increaseEfficiency() {
		mSumEfficiency += (float)mQuickLeaveCars/(float)mTime;
		mTime = 0;
		mQuickLeaveCars = 0;
	}

	public synchronized void setEfficiencyTime(int time) {
		mTime = time;
		mQuickLeaveCars = 0;
	}
	
	public synchronized void clearEfficiency() {
		mSumEfficiency = 0;
	}
	
	public synchronized float getEfficiency() {
		return mSumEfficiency/(float)mCycleCounter;
	}
	

	public String getName() {
		switch (mType) {
			case ITS.MSG_KRZYKI_SRODMIESCIE:
				return "krzyki_srodmiescie_" + mTypeNumber;
			case ITS.MSG_KUTNOWSKA:
				return "kutnowska_" + mTypeNumber;
			case ITS.MSG_SRODMIESCIE_KUTNOWSKA:
				return "srodmiescie_kutnowska_" + mTypeNumber;
			case ITS.MSG_SRODMIESCIE_KRZYKI:
				return "srodmiescie_krzyki_" + mTypeNumber;
			case ITS.MSG_TRAMWAJ_KRZYKI:
				return "tramwaj_krzyki_" + mTypeNumber;
			case ITS.MSG_TRAMWAJ_SRODMIESCIE:
				return "tramwaj_srodmiescie_" + mTypeNumber;
			default:
				return "no_stream";
		}
	}
}
