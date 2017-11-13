package com.rainjm.droneapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rainjm.droneapp.R;

import java.util.ArrayList;

/**
 * Created by stone on 11/11/17.
 */

public class RecieverFragment extends Fragment {

    private View view;
    private BarChart barChart;

    public RecieverFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_reciever,container,false);

        barChart = view.findViewById(R.id.bar_graph);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 1700f));
        entries.add(new BarEntry(1, 2000));
        entries.add(new BarEntry(2, 1000));
        entries.add(new BarEntry(3, 1500));




        BarDataSet dataset = new BarDataSet(entries, "# of Calls");

        //LimitLine line = new LimitLine(10f);
        //dataset.addLimitLine(line);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Throttle");
        labels.add("Roll");
        labels.add("Pitch");
        labels.add("Yaw");

        BarData data = new BarData(dataset);
        barChart.setData(data);

        barChart.animateY(5000);


        return view;
    }
}
