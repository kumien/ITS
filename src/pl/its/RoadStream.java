/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

/**
 *
 * @author Krystian
 */
public class RoadStream {
    private int mType;
    private int mCarsNumber;
    
    public RoadStream(int type) {
        mType = type;
        mCarsNumber = 0;
    }
    
    public synchronized void increaseCarsNumber(int number) {
        mCarsNumber += number;
    }
    
    public synchronized void decreaseCarsNumber(int number) {
        mCarsNumber -= number;
        if(mCarsNumber<0) {
            mCarsNumber = 0;
        }
    }
    
    public synchronized int getCarsNumber() {
        return mCarsNumber;
    }
    
    public int getType() {
        return mType;
    }
}
