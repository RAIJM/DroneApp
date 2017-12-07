package com.rainjm.droneapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gospelware.compassviewlib.CompassView;
import com.rainjm.droneapp.R;

import org.w3c.dom.Text;

import java.util.Map;

/**
 * Created by stone on 12/7/17.
 */

public class BearingFragment extends Fragment {


    private View mView;
    private CompassView compassView;
    private TextView tvBearing;



    public BearingFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.frag_bearing,container,false);

        compassView = (CompassView) mView.findViewById(R.id.compass);
        tvBearing = (TextView) mView.findViewById(R.id.bearing);

        tvBearing.setText("120" + (char) 0x00B0);
        compassView.setRotation(120);

        return mView;
    }


    public void update_data(Map<String,String> dataMap)
    {
        int bearing = Integer.valueOf(dataMap.get("heading"));

        tvBearing.setText(bearing+"" + (char) 0x00B0);
        compassView.setRotation(bearing);
    }
}
