package com.eth.pictureapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {

    private int position;
    private ImageView image;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
}
