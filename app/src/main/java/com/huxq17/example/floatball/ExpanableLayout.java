package com.huxq17.example.floatball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by huxq17 on 2016/5/25.
 */
public class ExpanableLayout extends RelativeLayout {
    private Paint abovePaint;
    private int roundWidth = 8;
    private int roundHeight = 8;
    private Paint belowPaint;
    public static int LEFT = 0;
    public static int RIGHT = 1;
    private int ORITATION = LEFT;

    public ExpanableLayout(Context context) {
        super(context);
        init(context, null);
    }

    public void setOritation(int oritation) {
        ORITATION = oritation;
    }

    private void init(Context context, AttributeSet attrs) {
        roundWidth = DensityUtil.dip2px(getContext(), roundWidth);
        roundHeight = DensityUtil.dip2px(getContext(), roundHeight);

        abovePaint = new Paint();
        abovePaint.setAntiAlias(true);
        abovePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        belowPaint = new Paint();
        belowPaint.setAntiAlias(true);
//        belowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), belowPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);

        if (ORITATION == LEFT) {
            drawLeft(canvas);
        } else if (ORITATION == RIGHT) {
            drawRight(canvas);
        }
        canvas.restore();
    }

    public void setOffset(int offset) {
        this.offset = offset;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private int offset = 0;
    private Path path = new Path();

    private void drawLeft(Canvas canvas) {
        int height = getHeight();
        path.reset();
        path.moveTo(0, height);
        path.lineTo(0, 0);
        path.lineTo(getWidth() - height / 2 - offset, 0);
        path.arcTo(new RectF(
                        getWidth() - offset, 0, getWidth() - offset + height, height),
                -90,
                -180);
        path.close();
        canvas.drawPath(path, abovePaint);
    }

    private void drawRight(Canvas canvas) {
        int height = getHeight();
        path.reset();
        path.moveTo(getWidth(), getHeight());
        path.lineTo(getWidth(), 0);
        path.lineTo(offset - height / 2, 0);
        path.arcTo(new RectF(offset - height, 0, offset, height), -90, 180);
        path.close();
        canvas.drawPath(path, abovePaint);
    }
}
