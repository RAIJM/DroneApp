package com.rainjm.droneapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rainjm.droneapp.R;
import com.rainjm.droneapp.activities.MainActivity;

/**
 * Created by stone on 11/11/17.
 */

public class PIDFragment extends Fragment {
    private View view;
    private EditText editKpThrottle;
    private EditText editKdThrottle;
    private EditText editKiThrottle;
    private EditText editKpPitch;
    private EditText editKdPitch;
    private EditText editKiPitch;
    private EditText editKpRoll;
    private EditText editKdRoll;
    private EditText editKiRoll;
    private EditText editKpYaw;
    private EditText editKdYaw;
    private EditText editKiYaw;
    private Button updateButton;





    public PIDFragment()
    {

    }

    private void initViews()
    {
        editKpThrottle = (EditText) view.findViewById(R.id.throttle_kp);
        editKdThrottle = (EditText) view.findViewById(R.id.throttle_kd);
        editKiThrottle = (EditText) view.findViewById(R.id.throttle_ki);
        editKpPitch = (EditText) view.findViewById(R.id.pitch_kp);
        editKdPitch = (EditText) view.findViewById(R.id.pitch_kd);
        editKiPitch = (EditText) view.findViewById(R.id.pitch_ki);
        editKpRoll = (EditText) view.findViewById(R.id.roll_kp);
        editKdRoll = (EditText) view.findViewById(R.id.roll_kd);
        editKiRoll = (EditText) view.findViewById(R.id.roll_ki);
//        editKpYaw = (EditText) view.findViewById(R.id.yaw_kp);
//        editKdYaw = (EditText) view.findViewById(R.id.yaw_kd);
//        editKiYaw = (EditText) view.findViewById(R.id.yaw_ki);
        updateButton = (Button) view.findViewById(R.id.update_button);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_pid,container,false);

        initViews();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePidValues();
            }
        });

        return view;
    }

    private void updatePidValues()
    {
        double throttle_kp = Double.parseDouble(editKpThrottle.getText().toString());
        double throttle_kd = Double.parseDouble(editKdThrottle.getText().toString());
        double throttle_ki = Double.parseDouble(editKiThrottle.getText().toString());
        double pitch_kp = Double.parseDouble(editKpPitch.getText().toString());
        double pitch_kd = Double.parseDouble(editKdPitch.getText().toString());
        double pitch_ki = Double.parseDouble(editKiPitch.getText().toString());
        double roll_kp = Double.parseDouble(editKpRoll.getText().toString());
        double roll_kd = Double.parseDouble(editKdRoll.getText().toString());
        double roll_ki = Double.parseDouble(editKiRoll.getText().toString());
//        double yaw_kp = Double.parseDouble(editKpYaw.getText().toString());
//        double yaw_kd = Double.parseDouble(editKdYaw.getText().toString());
//        double yaw_ki = Double.parseDouble(editKiYaw.getText().toString());

        StringBuilder sb = new StringBuilder();
        sb.append("s"+throttle_kp+" ");
        sb.append(throttle_kd + " ");
        sb.append(throttle_ki + " ");
        sb.append(pitch_kp + " ");
        sb.append(pitch_kd + " ");
        sb.append(pitch_ki + " ");
        sb.append(roll_kp + " ");
        sb.append(roll_kd  + " ");
        sb.append(roll_ki + "\n");
//        sb.append(yaw_kp + "&");
//        sb.append(yaw_kd );
//        sb.append(yaw_ki +"\n");

        updateButton.setEnabled(false);

        ((MainActivity)getActivity()).sendPIDData(sb.toString(),updateButton);


    }
}
