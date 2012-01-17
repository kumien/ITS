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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Krystian
 */
public class Genetic implements Runnable {

	private final static String TAG = "ITS_GENETIC";
	private final int POPULATION_SIZE = 1000;
	private final int PARENTS_SIZE = 100;
	private Random mRandom;
	private List<Individual> mPopulation;
	private List<Individual> mParents;
	private boolean mFirstRun = false;
	private int mPopulationCounter;
	

	public Genetic(Random random) {
		mRandom = random;
		mPopulationCounter = 0;
		initParents();
	}

	private void initPopulation() {
		mPopulation = new ArrayList<Individual>();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			mPopulation.add(new Individual(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
		}
	}

	private void initParents() {
		mParents = new ArrayList<Individual>();
	}

	private void sortPopulationByRate() {
		Collections.sort(mPopulation, new IndividualComparator());
	}

	private void chooseParents() {
		mParents.clear();
		sortPopulationByRate();
		float rateSum = 0;
		for (int i = 0; i < mPopulation.size(); i++) {
			rateSum += mPopulation.get(i).getRate();
		}
		for (int i = 0; i < PARENTS_SIZE; i++) {
			float randomPart = mRandom.nextFloat() * rateSum;
			float tempSum = 0;
			for (int j = 0; j < mPopulation.size(); j++) {
				tempSum += mPopulation.get(j).getRate();
				if (randomPart < tempSum) {
					mParents.add(mPopulation.get(j));
					break;
				}
			}
		}
	}

	private void crossoverParents() {
		mPopulation.clear();
		do {
			if (mRandom.nextFloat() < 0.01) {
				int parentPointer = mRandom.nextInt(mParents.size());
				Individual mutationChooser = new Individual(mParents.get(parentPointer).toGen());
				mutationChooser.mutate(mRandom);
				mPopulation.add(mutationChooser);
			} else {
				Individual ind1 = mParents.get(mRandom.nextInt(mParents.size()));
				Individual ind2 = mParents.get(mRandom.nextInt(mParents.size()));
				crossoverIndividuals(ind1.toGen(), ind2.toGen());
			}
		} while (mPopulation.size() < POPULATION_SIZE);
	}

	private void printAverageRate(int generation) {
		float averageParents = 0;
		for (int i = 0; i < mParents.size(); i++) {
			averageParents += mParents.get(i).getRate();
			Log.i(TAG, "Parent: " + mParents.get(i).getTime());
		}
		averageParents /= mParents.size();
		
		float averagePopulation = 0;
		for (int i = 0; i < mPopulation.size(); i++) {
			averagePopulation += mPopulation.get(i).getRate();
		}
		averagePopulation /= mPopulation.size();
		
		String pathParents =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS";
		File fileParents = new File(pathParents);
//		if(!fileParents.exists()) {
			fileParents.mkdirs();
//		}
		fileParents = new File(pathParents + "/parents.txt");

		String pathPopulation =  Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ITS";
		File filePopulation = new File(pathPopulation);
//		if(!filePopulation.exists()) {
			filePopulation.mkdirs();
//		}
		filePopulation = new File(pathPopulation + "/population.txt");
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileParents, true));
			out.write(generation + "\t" + averageParents);
			out.newLine();
			out.close();
			out = new BufferedWriter(new FileWriter(filePopulation, true));
			out.write(generation + "\t" + averagePopulation);
			out.newLine();
			out.close();
		} catch(Exception e) {
			Log.e(TAG, "Exception: " + e.getLocalizedMessage());
		}
	}

	private void crossoverIndividuals(boolean[] indGen1, boolean[] indGen2) {
		int crossoverPointer = mRandom.nextInt(24);
//		int crossoverPointer = mRandom.nextInt(16);
		boolean[] tempGen = new boolean[24];
		System.arraycopy(indGen1, 0, tempGen, 0, crossoverPointer);
		System.arraycopy(indGen2, 0, indGen1, 0, crossoverPointer);
		System.arraycopy(tempGen, 0, indGen2, 0, crossoverPointer);
		mPopulation.add(new Individual(indGen1));
		if (mPopulation.size() < POPULATION_SIZE) {
			mPopulation.add(new Individual(indGen2));
		}
	}

	private void printPopulation() {
		for (int i = 0; i < mPopulation.size(); i++) {
			Log.i(TAG, "Individual " + i + " has rate " + mPopulation.get(i).getRate());
		}
	}

	public static int maxCarsInPeriodOfTime(int time) {
		int cars;
		if (time < 3) {
			cars = time;
		} else if (time < 8) {
			cars = 3 * (time - 2) + 3;
		} else {
			double number = Math.ceil((time - 5) / 2);
			int carsNumber = (int) (number + 21);
			cars = carsNumber;
		}
		return cars;
	}

	public static int optimalTimeForCars(int cars) {
		int tempCars = 0;
		int time = 0;
		do {
			if (time < 3) {
				tempCars++;
				time++;
			} else if (time < 6) {
				tempCars += 3;
				time++;
			} else {
//				double number = Math.ceil((time - 5) / 2);
				tempCars += 2;
				time += 3;
			}
		} while (tempCars < cars);

		return cars;
	}

	public int getBestIndividualTime(int cars, int collisionCars) {
		int rating = 500000;
		float rate = 0;
		int index = -1;
		for (int i = 0; i < mPopulation.size(); i++) {
			int tempRating = 0;
			if (mPopulation.get(i).getCars() > cars) {
				tempRating += mPopulation.get(i).getCars() - cars;
			} else {
				tempRating += cars - mPopulation.get(i).getCars();
			}
			if (mPopulation.get(i).getCollisionCars() > collisionCars) {
				tempRating += mPopulation.get(i).getCollisionCars() - collisionCars;
			} else {
				tempRating += collisionCars - mPopulation.get(i).getCollisionCars();
			}
			if (tempRating < rating || (tempRating< rating-15) && mPopulation.get(i).getRate()>rate) {
				index = i;
				rating = tempRating;
				rate = mPopulation.get(i).getRate();
			}
		}
		int time = mPopulation.get(index).getTime();
//		mPopulation.get(index).improve(cars, collisionCars);
		return time;
	}

	public synchronized void nextPopulation() {
//		if (mPopulationCounter > 1000) {
//			chooseParents();
//			crossoverParents();
//			mPopulationCounter = 0;
//		}
	}

	public void run() {
		if (!mFirstRun) {
			initPopulation();
			for (int i = 0; i < 50; i++) {
				Log.i(TAG, "Generation " + i);
				chooseParents();
				printAverageRate(i);
				crossoverParents();
			}
			printPopulation();
		}
	}
}
