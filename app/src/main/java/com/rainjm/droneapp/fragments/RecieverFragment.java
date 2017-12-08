package com.rainjm.droneapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rainjm.droneapp.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by stone on 11/11/17.
 */

public class RecieverFragment extends Fragment {

    private View view;
    private BarChart barChart;
    TextView tvThrottle;
    TextView tvPitch;
    TextView tvRoll;
    TextView tvYaw;
    BarDataSet dataset;

    public RecieverFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_reciever,container,false);

        barChart = view.findViewById(R.id.bar_graph);

        tvThrottle = (TextView) view.findViewById(R.id.throttle);
        tvPitch = (TextView) view.findViewById(R.id.pitch);
        tvRoll =(TextView) view.findViewById(R.id.roll);
        tvYaw = (TextView) view.findViewById(R.id.yaw);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(2000);
        yAxis.setAxisMinimum(1000);
        yAxis.setGranularity(1000);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 1700f));
        entries.add(new BarEntry(1, 2000));
        entries.add(new BarEntry(2, 1000));
        entries.add(new BarEntry(3, 1500));




        dataset = new BarDataSet(entries, "# of Calls");


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

    public void update_data(Map<String,String> dataMap)
    {

        try{
            float throttle = Float.valueOf(dataMap.get("throttle"));
            float roll = Float.valueOf(dataMap.get("roll"));
            float pitch = Float.valueOf(dataMap.get("pitch"));
            float yaw = Float.valueOf(dataMap.get("yaw"));

            tvThrottle.setText((int)throttle+"");
            tvRoll.setText((int)roll+"");
            tvPitch.setText((int)pitch+"");
            tvYaw.setText((int)yaw+"");

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, throttle));
            entries.add(new BarEntry(1, pitch));
            entries.add(new BarEntry(2, roll));
            entries.add(new BarEntry(3, yaw));

            BarDataSet set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }catch (Exception e){

        }


    }
}
