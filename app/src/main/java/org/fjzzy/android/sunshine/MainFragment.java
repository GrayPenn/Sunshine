package org.fjzzy.android.sunshine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] forecastArray = {
                "星期一 -   暴雨  -   27/33",
                "星期二 -   暴雨  -   27/33",
                "星期三 -   暴雨  -   27/33",
                "星期四 -   暴雨  -   27/33",
                "星期五 -   暴雨  -   27/33",
                "星期六 -   暴雨  -   27/33",
                "星期日 -   暴雨  -   27/33",
    };

        List<String> forecastList = new ArrayList<String>(Arrays.asList(forecastArray));

        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                forecastList

        );

        ListView listViewFroecast = (ListView)rootView.findViewById(R.id.listview_forecast);
        listViewFroecast.setAdapter(forecastAdapter);

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
        int id = item.getItemId();
        if(id == R.id.action_refresh){

            //处理代码
            ForecastAsyncTask forecastAsyncTask = new ForecastAsyncTask();
            forecastAsyncTask.execute();

            return true;
        }



        return super.onOptionsItemSelected(item);
    }






}
