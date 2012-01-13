/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

/**
 *
 * @author Krystian
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
*
* @author krystian
*/
public class Badge extends View {
    private Context mContext;
    private int mNumber;
    private float mWidth;
    private float mHeight;
    private float mRadius;
    private float mFontHeight;
    private float mDensity;

    public Badge(Context context, float width) {
        super(context);
        mContext = context;
        mDensity = context.getResources().getDisplayMetrics().density;
        mWidth = width*mDensity;
        mHeight = mWidth;
        mRadius = mWidth/2;
        mFontHeight = mWidth/2;
    }

    public Badge(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mDensity = context.getResources().getDisplayMetrics().density;
        mWidth = 25*mDensity;
        mHeight = mWidth;
        mRadius = mWidth/2;
        mFontHeight = mWidth/2;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        Paint myPaint = new Paint();
        myPaint.setStrokeWidth(2);
        myPaint.setColor(0xFFFF0000);
        myPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth/2, mHeight/2, mRadius, myPaint);
        myPaint.setColor(0xFFFFFFFF);
        myPaint.setTextAlign(Paint.Align.CENTER);

        myPaint.setTypeface(Typeface.DEFAULT_BOLD);
        myPaint.setTextSize(mFontHeight);
        float size = myPaint.getTextSize();
        canvas.drawText(Integer.toString(mNumber), mWidth/2, mHeight/2+(mFontHeight/2)*0.7f, myPaint);
    }

    public void setBadge(int number) {
        mNumber = number;
        invalidate();
    }

    public int getBadge() {
        return mNumber;
    }

    public void increase() {
        mNumber++;
        invalidate();
    }

}
