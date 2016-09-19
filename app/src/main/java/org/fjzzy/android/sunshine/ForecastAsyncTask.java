package org.fjzzy.android.sunshine;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pengguiyang on 16/9/19.
 */
public class ForecastAsyncTask extends AsyncTask<Void,Void,Void>{
    @Override
    protected Void doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;


        try{
            String urlStr = "http://api.openweathermap.org/data/2.5/forecast/daily?id=1785018&mode=json&units=metric&cnt=7&lang=zh_cn&APPID=56fdc694f42bc8b85dea556d9ae54fb4";
            URL url = new URL(urlStr);

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            

            //输入流
            InputStream inputStream = urlConnection.getInputStream();

            if(inputStream == null){
                Log.v("ForecastAsyncTask","输入流错误" );
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            //缓冲区
            StringBuffer buffer = new StringBuffer();
            String line = null;

            //读数据
            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0){
                Log.v("ForecastAsyncTask","读数据错误,没有数据" );
                return null;
            }else{
                forecastJsonStr = buffer.toString();
                Log.v("ForecastAsyncTask","Forecast Json String:" + forecastJsonStr );

            }





        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
            Log.v("ForecastAsyncTask","IO异常",e );
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        return null;
    }
}
