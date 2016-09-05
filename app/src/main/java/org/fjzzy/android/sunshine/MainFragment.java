package org.fjzzy.android.sunshine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] forecastArray = {
                "    星期一 -   多云  -   27/33",
                "    星期二 -   多云  -   27/33",
                "    星期三 -   多云  -   27/33",
                "    星期四 -   多云  -   27/33",
                "    星期五 -   多云  -   27/33",
                "    星期六 -   多云  -   27/33",
                "    星期日 -   多云  -   27/33",
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

}
