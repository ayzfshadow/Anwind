package com.saki.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.ayzf.anwind.R;

@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {

    private float round;
    private Paint paint;

    public RoundImageView(Context context) {
        super(context);
        init();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView, defStyle, 0);
        round = a.getFloat(R.styleable.RoundImageView_roundName, 0.0f);
        a.recycle();
        init();
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (null == drawable)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = getImageMatrix();
        if (null != matrix)
            canvas.concat(matrix);
        drawable.draw(canvas);
        return bitmap;
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        Matrix matrix = getImageMatrix();
        if (null == drawable)
            return;
        if (0 == drawable.getIntrinsicWidth()
                || 0 == drawable.getIntrinsicHeight())
            return;
        if (null == matrix && 0 == getPaddingTop() && 0 == getPaddingLeft())
            drawable.draw(canvas);
        else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            if (0 < round) {
                if (round < 1) {
                    round = getWidth() * round;
                }
                Bitmap bitmap = drawable2Bitmap(drawable);
                paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP));
                canvas.drawRoundRect(new RectF(getPaddingLeft(),
                        getPaddingTop(), getWidth() - getPaddingRight(),
                        getHeight() - getPaddingBottom()), round, round, paint);
            } else {
                if (null != matrix) {
                    canvas.concat(matrix);
                }
                drawable.draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        }
    }

}
