package com.rainjm.droneapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rainjm.droneapp.R;

import org.w3c.dom.Text;

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
    private LineDataSet pitchDataset;
    private LineDataSet rollDataset;
    private TextView tvPitch;
    private  TextView tvRoll;
    private ArrayList<ILineDataSet> lines;
    private LineData lineData;


    public AttitudeFragment()
    {

    }


    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_attitude,container,false);

        lineChart = view.findViewById(R.id.line_graph);

        tvPitch = (TextView) view.findViewById(R.id.att_pitch);
        tvRoll = (TextView) view.findViewById(R.id.att_roll);



        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(30);
        yAxis.setAxisMinimum(-30);
        yAxis.setGranularity(-30);
        LimitLine ll = new LimitLine(0f);
        yAxis.addLimitLine(ll);



        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0,0f));
//        entries.add(new Entry(1,8f));
//        entries.add(new Entry(2,6f));
//        entries.add(new Entry(3,10f));
//        entries.add(new Entry(4,18f));
//        entries.add(new Entry(5,9f));

        ArrayList<Entry> entry = new ArrayList<>();
        entry.add(new Entry(0,0f));
//        entry.add(new Entry(1,10f));
//        entry.add(new Entry(2,4f));
//        entry.add(new Entry(3, 14f));
//        entry.add(new Entry(4, 12f));
//        entry.add(new Entry(5, 5f));


        lines = new ArrayList<ILineDataSet> ();
        String[] xAxis = new String[] {"1", "2", "3", "4", "5","6"};
        pitchDataset = new LineDataSet(entries, "Pitch");
        pitchDataset.setColor(Color.parseColor("#00ff00"));
        lines.add(pitchDataset);

        rollDataset = new LineDataSet(entry, "Roll");
        rollDataset.setColor(Color.parseColor("#0000ff"));
        lines.add(rollDataset);

        lineData = new LineData(lines);
        lineChart.setData(lineData);
        lineChart.animateY(5000);





        return view;
    }

    public void update_data(Map<String,String> dataMap)
    {
        try{
            pitch = Double.parseDouble(dataMap.get("attitude_pitch"));
            roll = Double.parseDouble(dataMap.get("attitude_roll"));

            int step = Integer.valueOf(dataMap.get("step"));

            tvPitch.setText(pitch+"");
            tvRoll.setText(roll+"");


            pitchDataset.addEntry(new Entry(step,(float)pitch));
            rollDataset.addEntry(new Entry(step,(float)roll));

            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }catch (Exception e){

        }


    }
}
