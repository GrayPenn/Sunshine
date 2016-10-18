package org.fjzzy.android.sunshine;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter=null;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    //点击返回后刷新自动
    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);


        mForecastAdapter =new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview

        );

        ListView listviewFroecast=(ListView) rootView.findViewById(R.id.listview_forecast);
        listviewFroecast.setAdapter(mForecastAdapter);
        listviewFroecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String forecast = mForecastAdapter.getItem(i);
                //Toast.makeText(getActivity(),forecast,Toast.LENGTH_SHORT).show();  //打开一个Activity
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT,forecast);
                startActivity(intent);


            }
        });

        // Inflate the layout for this fragment

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            //处理代码
            updateWeather();
            return true;
        }
        if(id==R.id.action_setting){
            //打开一个Activity
            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //1785018 : 漳州
    //1790645 : 厦门
    //1792585 : 福鼎
    public void updateWeather(){
        ForecastAsyncTask forecastAsyncTask=new ForecastAsyncTask();
        //SharedPreferences 共享配置  PreferenceManager 配置管理器
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cityId = prefs.getString("city_name_text",getString(R.string.pref_default_city_name));
        forecastAsyncTask.execute(cityId);
    }

    public class ForecastAsyncTask extends AsyncTask<String,Void,String[]> {
        @Override
        protected String[] doInBackground(String... paramses) {
            HttpURLConnection urlConnection =null;
            BufferedReader reader =null;
            String forecastJsonStr=null;

            try {
                //String urlStr="http://api.openweathermap.org/data/2.5/forecast/daily?id=1785018&mode=json&units=metric&cnt=7&lang=zh_cn&APPID=56fdc694f42bc8b85dea556d9ae54fb4";

                Uri uri = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily?").buildUpon()
                        .appendQueryParameter("id",paramses[0])
                        .appendQueryParameter("mode","json")
                        .appendQueryParameter("units","metric")
                        .appendQueryParameter("cnt","7")
                        .appendQueryParameter("lang","zh_cn")
                        .appendQueryParameter("APPID","56fdc694f42bc8b85dea556d9ae54fb4")//f56b68bedcdcbced0e128b010ea7ab0f
                        .build();

                URL url=new URL(uri.toString());
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect() ;

                InputStream inputStream =urlConnection .getInputStream() ;
                if(inputStream ==null){
                    Log.v("ForecastAsyncTask","");
                    return null;
                }
                reader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer =new StringBuffer();
                String line=null;

                while ((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0){
                    Log.v("ForecastAsyncTask","");
                }else {
                    forecastJsonStr=buffer.toString();
                    Log.v("ForecastAsyncTask","Forecast Json String:" +forecastJsonStr);
                }


            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("ForecastAsyncTask","IO异常!",e);
            }finally {
                if (urlConnection !=null) {
                    urlConnection.disconnect();
                }
                if (reader !=null){
                    try {
                        reader.close();
                    }catch (IOException e){
                        e.printStackTrace() ;
                    }
                }
            }
            String[] result=null;

            if(forecastJsonStr !=null){
                try {
                    result= WeatherDataParser.getWeatherDataFromJson(forecastJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("ForecastAsyncTask","Json处理异常!",e);
                }

            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if(result!=null){
                mForecastAdapter.clear();
                mForecastAdapter.addAll(result);
            }

        }
    }
}
