package com.viewanimation.circularBeam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Arun Abraham on 03/12/14.
 */
public class PhotoView extends View {

    float PLACE_HOLDER_CIRCLE_RADIUS = 100.0f;
    float PLACE_HOLDER_MARGIN = 5.0f;
    Context context;

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setBackgroundColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                float touchPosX = motionEvent.getX();
                float touchPosY = motionEvent.getY();
                int centerX = getWidth()/2;
                int centerY = getHeight()/2;

                boolean isInCircularRegion = (touchPosX - centerX) * (touchPosX - centerX)
                                             + (touchPosY - centerY) * (touchPosY - centerY)
                                             <= PLACE_HOLDER_CIRCLE_RADIUS * PLACE_HOLDER_CIRCLE_RADIUS;

                if(isInCircularRegion)
                    Toast.makeText(context,"Within the circle",Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        drawPlaceHolderForImage(canvas);
        drawImageInPlaceHolder(canvas);
    }

    private void drawPlaceHolderForImage(Canvas canvas) {

        Paint circularFramePaint = new Paint();
        circularFramePaint.setColor(Color.RED);
        circularFramePaint.setAntiAlias(true);

        float centerOfViewX = getWidth()/2;
        float centerOfViewY = getHeight()/2;

        canvas.drawCircle(centerOfViewX, centerOfViewY, PLACE_HOLDER_CIRCLE_RADIUS + PLACE_HOLDER_MARGIN , circularFramePaint);

    }

    private void drawImageInPlaceHolder(Canvas canvas) {

        Bitmap imageBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog_image);
        int width = imageBm.getWidth();
        int height = imageBm.getHeight();

        Paint imagePaint = new Paint();
        imagePaint.setAntiAlias(true);

        Bitmap scaledBitmap = getResizedBitmap(imageBm, (int)PLACE_HOLDER_CIRCLE_RADIUS*2, (int)PLACE_HOLDER_CIRCLE_RADIUS*2 );

        canvas.drawBitmap(getCroppedBitmap(scaledBitmap), getWidth()/2 - PLACE_HOLDER_CIRCLE_RADIUS, getHeight()/2 - PLACE_HOLDER_CIRCLE_RADIUS, imagePaint);

    }

    private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(output.getWidth() / 2, output.getHeight() / 2,
                PLACE_HOLDER_CIRCLE_RADIUS, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
