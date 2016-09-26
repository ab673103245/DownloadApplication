package qianfeng.downloadapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = ((Button) findViewById(R.id.btn));
        pb = ((ProgressBar) findViewById(R.id.pb));


    }

    public void download(View view) { // 点击就下载图片，并显示进度条
            new MyAsyncTask().execute("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv");

    }

                                                    // 进度条的setProgress() , 里面只能传入一个Integer型
    private class MyAsyncTask extends AsyncTask<String, Integer, byte[]> {

        @Override
        protected void onPreExecute() {
           // 一开始显示进度条
            pb.setVisibility(View.VISIBLE);
        }
        @Override
        protected byte[] doInBackground(String... params) {

            http(params[0]);  // 感觉这里的byte数组没什么用啊,都传递不了
            return new byte[0];
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
           // 执行下载完成后，进度条就不见了
            pb.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
           if(values.length > 0)
           {
               pb.setProgress(values[0]);
           }
        }

        private byte[] http(String s)
        {
            HttpURLConnection httpURLConnection = null;
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                URL url = new URL(s);
                fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"111.mp4"));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                if(httpURLConnection.getResponseCode()==200)
                {
                    // 连接成功要干嘛？
                    is = httpURLConnection.getInputStream();
                    long totalByte = httpURLConnection.getContentLength();
                    int current = 0;
                    int len = 0;
                    byte[] data = new byte[1024];
                    while((len = is.read(data))!=-1)
                    {
                        fos.write(data,0,len);
                        fos.flush();
                        current += len;
                        // 来一个方法//调用该方法会触发onProgressUpdate
                        publishProgress((int)(current*100/totalByte));
                    }
                }
            } catch (MalformedURLException e) {


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }




}
