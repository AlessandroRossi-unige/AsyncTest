package com.example.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class BackTask extends AsyncTask<String, Integer, String> {

    private final String TAG = "BackTask";
    private String storeDir = "";
    private ProgressBar pbDownload = null;

    private DownloadCompleted downloadCompleted = null;

    public BackTask(String _dir, ProgressBar _pb, DownloadCompleted _dc) {

        this.storeDir = _dir;
        this.pbDownload = _pb;
        this.downloadCompleted = _dc;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.pbDownload.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url = null;

        try{
            url = new URL(strings[0]);
            File f = new File(this.storeDir);

            if(f.exists()) {
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                InputStream is = con.getInputStream();
                String fullpath = this.storeDir + "/airplane.png";
                FileOutputStream fos = new FileOutputStream(fullpath);

                int filesize = con.getContentLength();
                int _total = 0, _count = 0;

                byte data[] = new byte[1024];

                while ((_count = is.read(data)) != -1 ){
                    _total += _count;
                    fos.write(data);
                }

                is.close();
                fos.flush();
                fos.close();

                return fullpath;

            }
            else {
                Log.e(TAG, "Cannot find" + this.storeDir + " folder");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.pbDownload.setVisibility(View.INVISIBLE);
        Bitmap _image = BitmapFactory.decodeFile(s);
        this.downloadCompleted.onDownloadCompleted(_image);
    }

}
