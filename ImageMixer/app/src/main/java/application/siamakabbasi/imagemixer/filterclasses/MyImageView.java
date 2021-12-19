package application.siamakabbasi.imagemixer.filterclasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import application.siamakabbasi.imagemixer.commonclasses.DataManager;

public class MyImageView extends ImageView {

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawBitmap(DataManager.getInstance().getMainImageCustom(),null,(Rect)null,new Paint(Paint.ANTI_ALIAS_FLAG));
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        invalidate();
        Log.d("", "point: " + point);
        return true;
    }

    class Point {
        float x, y;
        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}