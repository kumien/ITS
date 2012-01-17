/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

import java.util.Random;

/**
 *
 * @author Krystian
 */
public class Individual {
    private static String TAG = "ITS_INDIVIDUAL";
    private static float LEARN_RATIO = 0.2f;
    
    private boolean []mTime;
    private boolean []mCars;
    private boolean []mCollisionCars;
    private float mRate;
    
    public Individual(int time, int cars, int collisionCars) {
        mTime = Individual.initTime(time);
        mCars = Individual.initCars(cars);
        mCollisionCars = Individual.initCollisionCars(collisionCars);
        rate();
    }
    
    public Individual(boolean []indyvidual) {
        mTime =  new boolean[8];
        mCars =  new boolean[8];
        mCollisionCars =  new boolean[8];
        System.arraycopy(indyvidual, 0, mCollisionCars, 0, 8);
        System.arraycopy(indyvidual, 8, mCars, 0, 8);
        System.arraycopy(indyvidual, 16, mTime, 0, 8);
        rate();
    }
    
    public boolean[] toGen() {
        boolean []gen = new boolean[24];
        System.arraycopy(mCollisionCars, 0, gen, 0, 8);
        System.arraycopy(mCars, 0, gen, 8, 8);
        System.arraycopy(mTime, 0, gen, 16, 8);
        return gen;
    }
    
    public int getTime() {
        int time = 0;
        int mask = 0x40;
        for(int i=7; i>=0; i--) {
            if(mTime[i]) {
                time |= mask;
            }
            mask >>= 1;
        }
        return time;
    }
    
    public int getCars() {
        int cars = 0;
        int mask = 0x40;
        for(int i=7; i>=0; i--) {
            if(mCars[i]) {
                cars |= mask;
            }
            mask >>= 1;
        }
        return cars;
    }
    
    public int getCollisionCars() {
        int cars = 0;
        int mask = 0x40;
        for(int i=7; i>=0; i--) {
            if(mCollisionCars[i]) {
                cars |= mask;
            }
            mask >>= 1;
        }
        return cars;
    }
    
    public float getRate() {
        return mRate;
    }
    
    private void rate() {
        float rate = (float)(getCars() - getCollisionCars())/(float)Genetic.maxCarsInPeriodOfTime(getTime());
        if(rate < 0) {
            rate = 0;
        } else if(rate>1) {
            rate = 1/rate;
        }
        mRate = rate;
    }
    
    public void mutate(Random random) {
        int mutatePosition = random.nextInt(8) + 16;
        if(mutatePosition<7) {
            mCollisionCars[mutatePosition] = !mCollisionCars[mutatePosition];
        } else if(mutatePosition<14) {
            mCars[mutatePosition-8] = !mCars[mutatePosition-8];
        } else {
            mTime[mutatePosition-16] = !mTime[mutatePosition-16];
        }
    }
    
    public void improve(int cars, int collisionCars) {
    	int time = Genetic.optimalTimeForCars(cars);
    	int newTime = (int) Math.ceil((float)(getTime() - time)*LEARN_RATIO);
    	int newCars = (int) Math.ceil((getCars() + cars)*LEARN_RATIO);
    	int newCollisionCars = (int) Math.ceil((getCollisionCars() + collisionCars)*LEARN_RATIO);
    	mTime = initTime(newTime);
    	mCars = initCars(newCars);
    	mCollisionCars = initCollisionCars(newCollisionCars);
    	rate();
    }
    
    
    public static boolean[] initTime(int time) {
        if(time>240) {
            time = 240;
        } else if(time<7) {
            time = 7;
        }
        boolean []booleanTime = new boolean[8];
        int mask = 0x40;
        int counter = 7;
        while(mask > 0) {
            if((mask&time) == 0) {
                booleanTime[counter] = false;
            } else {
                booleanTime[counter] = true;
            }
            counter--;
            mask >>= 1;
        }
        return booleanTime;
    }
    
    public static boolean[] initCars(int cars) {
        if(cars>255) {
            cars = 255;
        }
        boolean []booleanCars = new boolean[8];
        int mask = 0x40;
        int counter = 7;
        while(mask > 0) {
            if((mask&cars) == 0) {
                booleanCars[counter] = false;
            } else {
                booleanCars[counter] = true;
            }
            counter--;
            mask >>= 1;
        }
        return booleanCars;
    }
    
    public static boolean[] initCollisionCars(int cars) {
        if(cars>255) {
            cars = 255;
        }
        boolean []booleanCars = new boolean[8];
        int mask = 0x40;
        int counter = 7;
        while(mask > 0) {
            if((mask&cars) == 0) {
                booleanCars[counter] = false;
            } else {
                booleanCars[counter] = true;
            }
            counter--;
            mask >>= 1;
        }
        return booleanCars;
    }
}
