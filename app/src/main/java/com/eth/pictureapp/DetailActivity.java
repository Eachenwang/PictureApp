package com.eth.pictureapp;

import android.accessibilityservice.GestureDescription;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    GestureDetector detector;
    private int position;
    private ImageView image;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detector = new GestureDetector(this, this);

        position = getIntent().getIntExtra("POSITION", 0);
        image = findViewById(R.id.imageView);
        CursorLoader loader = new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        cursor = loader.loadInBackground();
        cursor.moveToPosition(position);
        updateImage();
    }

    private void updateImage() {
        String imagePath= cursor.getString(cursor.getColumnIndex(
                MediaStore.Images.Media.DATA));
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        image.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float v, float v1) {
        float distance = motionEvent2.getX()-motionEvent1.getX();
        if (distance > 100) {
            //往右 往前一張
            if (!cursor.moveToPrevious()) {
                cursor.moveToLast();
            }
            updateImage();
        } else if (distance < 100) {
            if (!cursor.moveToNext()) {
                cursor.moveToFirst();
            }
            updateImage();
        }

        return false;
    }
}



