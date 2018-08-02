package com.eth.pictureapp;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
                    AdapterView.OnItemClickListener {

    private static final int REQUEST_READ_STORAGE = 3;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //將檢查有無權限結果存入permission
        int permission = ActivityCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);
        //如果未取得權限則向使用者獲取權限
        if(permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    //給onRequestPermissionsResult()的requestCode
                    REQUEST_READ_STORAGE);
        }else {
            //反之則有權限則存取
            readThumbnails();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //依據requestCode切換執行程式碼
        switch (requestCode){
            case REQUEST_READ_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //取得讀取外部資料權限則存取
                    readThumbnails();
                }else {
                    //反之使用者拒絕存取權限則對話框告知
                    new AlertDialog.Builder(this)
                            .setMessage("必須允許讀取外部資料權限才能顯示圖檔")
                            .setPositiveButton("OK", null)
                            .show();
                }
                return;
        }
    }

    private void readThumbnails() {
        GridView grid = findViewById(R.id.grid);
        String[] from = {MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.DISPLAY_NAME};
        int[] to = {R.id.thumb_image, R.id.thumb_text};
        //參數 Cursor 先給 null 值 之後在 用CursorLoader處理
        adapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.thumb_item,
                null,
                from,
                to,
                0
        );
        grid.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        grid.setOnItemClickListener(this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //用android.net.Uri儲存查詢的資料位置
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //產生並回傳資料讀取器物件 並將uri傳遞給他
        return new CursorLoader(this,uri,null,null,null,null);
    }

    //資料讀取器向內容提供者查詢完畢會自動執行此方法
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //替換cursor
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }
}
