package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;

public class MainActivity extends AppCompatActivity implements DownloadCompleted {

    EditText ettURL = null;
    Button bttOK = null;
    ImageView ivImage = null;
    ProgressBar pbDownload = null;

    DownloadCompleted downloadCompleted = null;
    BackTask backtask = null;

    final String TAG = "MainActivity";
    final String MAFI_DOWNLOAD = "MAFI_DOWNLOAD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ettURL = findViewById(R.id.etURL);
        bttOK = findViewById(R.id.bttOK);
        ivImage = findViewById(R.id.ivImage);
        pbDownload = findViewById(R.id.pbDownload);
        downloadCompleted = this;

        bttOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String directory = createDirectory(MAFI_DOWNLOAD);
                backtask = new BackTask(directory, pbDownload, downloadCompleted);

                String _insertedURL = ettURL.getText().toString();
                backtask.execute(_insertedURL);
            }
        });

    }

    private String createDirectory(String _dirName) {

        String storeDir = Environment.getExternalStorageDirectory() + "/" + _dirName;
        File f = new File(storeDir);

        if(! f.exists()){
            if(!f.mkdir()){
                Log.e(TAG, "Cannot create dir");
                return null;
            }
            else return storeDir;
        }
        else return storeDir;
    }

    @Override
    public void onDownloadCompleted(Bitmap image) {

        ivImage.setImageBitmap(image);
    }



}