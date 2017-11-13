package com.rainjm.droneapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rainjm.droneapp.R;

import java.util.ArrayList;

/**
 * Created by stone on 11/11/17.
 */

public class AltitudeFragment extends Fragment {

    View view;
    private LineChart lineChart;

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



        ArrayList<ILineDataSet> lines = new ArrayList<ILineDataSet> ();
        String[] xAxis = new String[] {"1", "2", "3", "4", "5","6"};
        LineDataSet lDataSet1 = new LineDataSet(entries, "Alt");
        lDataSet1.setColor(Color.parseColor("#00ff00"));

        //lDataSet1.setDrawFilled(true);
        lines.add(lDataSet1);

        LineDataSet lDataSet2 = new LineDataSet(entry, "Height");
        lDataSet2.setColor(Color.parseColor("#0000ff"));
        //lDataSet2.setDrawFilled(true);
        lines.add(lDataSet2);

        lineChart.setData(new LineData(lines));
        lineChart.animateY(5000);

        return view;
    }
}
