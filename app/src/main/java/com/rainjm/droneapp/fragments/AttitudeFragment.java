package com.rainjm.droneapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Map;

import static android.R.attr.entries;

/**
 * Created by stone on 11/11/17.
 */

public class AttitudeFragment extends Fragment {

    private LineChart lineChart;
    private double pitch;
    private double roll;
    private double yaw;

    LineDataSet pitchDataset;
    LineDataSet rollDataset;
    LineDataSet yawDataset;


    public AttitudeFragment()
    {

    }

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_attitude,container,false);

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

        ArrayList<Entry> entry2 = new ArrayList<>();
        entry2.add(new Entry(0,3f));
        entry2.add(new Entry(1,5f));
        entry2.add(new Entry(2,7f));
        entry2.add(new Entry(3, 12f));
        entry2.add(new Entry(4, 15f));
        entry2.add(new Entry(5, 10f));

        ArrayList<ILineDataSet> lines = new ArrayList<ILineDataSet> ();
        String[] xAxis = new String[] {"1", "2", "3", "4", "5","6"};
        pitchDataset = new LineDataSet(entries, "Roll");
        pitchDataset.setColor(Color.parseColor("#00ff00"));

        //lDataSet1.setDrawFilled(true);
        lines.add(pitchDataset);

        rollDataset = new LineDataSet(entry, "Pitch");
        rollDataset.setColor(Color.parseColor("#0000ff"));
        //lDataSet2.setDrawFilled(true);
        lines.add(rollDataset);

        yawDataset= new LineDataSet(entry2, "Heading");
        yawDataset.setColor(Color.parseColor("#ff0000"));
        //lDataSet2.setDrawFilled(true);
        lines.add(yawDataset);

        lineChart.setData(new LineData(lines));
        lineChart.animateY(5000);





        return view;
    }

    public void update_data(Map<String,String> dataMap)
    {
        pitch = Double.parseDouble(dataMap.get("attitude_pitch"));
        roll = Double.parseDouble(dataMap.get("attitude_roll"));
        yaw = Double.parseDouble(dataMap.get("attitude_yaw"));

        int step = Integer.valueOf(dataMap.get("step"));


        pitchDataset.addEntry(new Entry(step,(float)pitch));
        rollDataset.addEntry(new Entry(step,(float)roll));
        yawDataset.addEntry(new Entry(step,(float)yaw));

    }
}
