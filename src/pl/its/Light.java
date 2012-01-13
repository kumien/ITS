/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *
 * @author Krystian
 */
public class Light extends ImageView {

    public static final int MSG_RED = 0;
    public static final int MSG_RED_AND_YELLOW = 1;
    public static final int MSG_GREEN = 2;
    public static final int MSG_YELLOW = 3;

    public Light(Context context) {
        super(context);
    }

    public Light(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public Light(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public void setRed() {
        setImageResource(R.drawable.red);
    }

    public void setRedYellow() {
        setImageResource(R.drawable.red_and_yellow);
    }

    public void setYellow() {
        setImageResource(R.drawable.yellow);
    }

    public void setGreen() {
        setImageResource(R.drawable.green);
    }

    public void setLight(int type) {
        switch (type) {
            case MSG_GREEN:
                setGreen();
                break;
            case MSG_RED:
                setRed();
                break;
            case MSG_RED_AND_YELLOW:
                setRedYellow();
                break;
            case MSG_YELLOW:
                setYellow();
                break;
        }
        invalidate();
    }
}
