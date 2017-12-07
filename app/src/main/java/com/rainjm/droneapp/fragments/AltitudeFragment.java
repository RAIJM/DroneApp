package com.rainjm.droneapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rainjm.droneapp.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by stone on 11/11/17.
 */

public class AltitudeFragment extends Fragment {

    private View view;
    private LineChart lineChart;
    private LineDataSet altDataset;
    private LineDataSet heightDataset;
    private LineData lineData;
    private float desired_height = 3.0f;

    public AltitudeFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_altitude,container,false);

        lineChart = view.findViewById(R.id.line_graph);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0,4f));
        entries.add(new Entry(1,8f));
        entries.add(new Entry(2,6f));
        entries.add(new Entry(3,10f));
        entries.add(new Entry(4,18f));
        entries.add(new Entry(5,9f));

        ArrayList<Entry> entry = new ArrayList<>();
        entry.add(new Entry(0,3f));
        entry.add(new Entry(1,10f));
        entry.add(new Entry(2,4f));
        entry.add(new Entry(3, 14f));
        entry.add(new Entry(4, 12f));
        entry.add(new Entry(5, 5f));

        YAxis yAxis = lineChart.getAxisLeft();
        LimitLine ll = new LimitLine(desired_height);
        yAxis.addLimitLine(ll);



        ArrayList<ILineDataSet> lines = new ArrayList<ILineDataSet> ();
        String[] xAxis = new String[] {"1", "2", "3", "4", "5","6"};
        altDataset = new LineDataSet(entries, "Alt");
        altDataset.setColor(Color.parseColor("#00ff00"));

        //lDataSet1.setDrawFilled(true);
        lines.add(altDataset);

        heightDataset = new LineDataSet(entry, "Height");
        heightDataset.setColor(Color.parseColor("#0000ff"));
        //lDataSet2.setDrawFilled(true);
        lines.add(heightDataset);

        lineData = new LineData(lines);

        lineChart.setData(lineData);
        lineChart.animateY(5000);

        return view;
    }

    public void update_data(Map<String,String> dataMap)
    {
        float altitude = Float.valueOf(dataMap.get("altitude"));
        float height = Float.valueOf(dataMap.get("height"));
        int step = Integer.valueOf(dataMap.get("step"));

        altDataset.addEntry(new Entry(step,altitude));
        heightDataset.addEntry(new Entry(step,height));

        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

    }
}
