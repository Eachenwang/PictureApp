package com.eth.pictureapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_STORAGE = 3;

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
    }
}
