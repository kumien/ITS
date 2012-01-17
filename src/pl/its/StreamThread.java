/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

/**
 * 
 * @author Krystian
 */
public class StreamThread extends Thread {
	private static final String TAG = "ITS_STREAM";

	private boolean mWork;
	RoadStream mStream;
	long mTimestamp;
	Random mRandom;
	private int mHour;
	private int mDay;

	public StreamThread(RoadStream stream, long timestamp, Random random) {
		mStream = stream;
		mTimestamp = timestamp;
		mRandom = random;
		mWork = true;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		mHour = getHour();
		Log.d(TAG, "Day number: " + mDay); 
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
		if(mHour!=hour) {
			mHour = hour;
			printData();
			mStream.clearLeaveCars();
			mStream.clearIncomingCars();
			mStream.clearCycleCounter();
			mStream.clearEfficiency();
			mStream.clearSumGreenLight();
		}
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(day!=mDay) {
			turnOff();
		}
		return hour;
	}

	private void printData() {
		String pathIncoming =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS/incoming";
		File fileIncoming = new File(pathIncoming);
		if(!fileIncoming.exists()) {
			fileIncoming.mkdirs();
		}
		fileIncoming = new File(pathIncoming + "/" + mStream.getName());
		
		String pathLeave =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS/leave";
		File fileLeave = new File(pathLeave);
		if(!fileLeave.exists()) {
			fileLeave.mkdirs();
		}
		fileLeave = new File(pathLeave + "/" + mStream.getName());
		
		String pathCycleCounter =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS/cycleCounter";
		File fileCycle = new File(pathCycleCounter);
		if(!fileCycle.exists()) {
			fileCycle.mkdirs();
		}
		fileCycle = new File(pathCycleCounter + "/" + mStream.getName());
		
		String pathAverageGreenTime =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS/averageGreenTime";
		File fileAverageGreenTime = new File(pathAverageGreenTime);
		if(!fileAverageGreenTime.exists()) {
			fileAverageGreenTime.mkdirs();
		}
		fileAverageGreenTime = new File(pathAverageGreenTime + "/" + mStream.getName());
		
		String pathQueueCars =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS/queueCars";
		File fileQueueCars = new File(pathQueueCars);
		if(!fileQueueCars.exists()) {
			fileQueueCars.mkdirs();
		}
		fileQueueCars = new File(pathQueueCars + "/" + mStream.getName());
		
		String pathEfficiency =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS/efficiency";
		File fileEfficiency = new File(pathEfficiency);
		if(!fileEfficiency.exists()) {
			fileEfficiency.mkdirs();
		}
		fileEfficiency = new File(pathEfficiency + "/" + mStream.getName());
		
		try {
			String logLeave = mHour + "\t" + mStream.getLeaveCars();
			String logIncoming = mHour + "\t" + mStream.getIncomingCars();
			String logCycleCounter = mHour + "\t" + mStream.getCycleCounter();
			String logAverageGreenTime = mHour + "\t" + (int)Math.ceil((float)mStream.getCSumGreenLight()/(float)mStream.getCycleCounter());
			String logQueueCars = mHour + "\t" + mStream.getCarsNumber();
			String logEfficiency = mHour + "\t" + mStream.getEfficiency();
			Log.i(TAG, "Leave: " + logLeave);
			BufferedWriter out = new BufferedWriter(new FileWriter(fileLeave, true));
			out.write(logLeave);
			out.newLine();
			out.close();
			out = new BufferedWriter(new FileWriter(fileIncoming, true));
			out.write(logIncoming);
			out.newLine();
			out.close();
			out = new BufferedWriter(new FileWriter(fileCycle, true));
			out.write(logCycleCounter);
			out.newLine();
			out.close();
			out = new BufferedWriter(new FileWriter(fileAverageGreenTime, true));
			out.write(logAverageGreenTime);
			out.newLine();
			out.close();
			out = new BufferedWriter(new FileWriter(fileQueueCars, true));
			out.write(logQueueCars);
			out.newLine();
			out.close();
			out = new BufferedWriter(new FileWriter(fileEfficiency, true));
			out.write(logEfficiency);
			out.newLine();
			out.close();
		} catch (IOException e) {
			Log.e(TAG, "Save data exception: " + e.getLocalizedMessage());
		}
	}
}
