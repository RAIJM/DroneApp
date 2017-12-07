package com.rainjm.droneapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rainjm.droneapp.R;

import java.util.Map;

/**
 * Created by stone on 11/11/17.
 */

public class GpsFragment extends Fragment{

    private GoogleMap mMap;
    MapView mMapView;
    private Marker droneMarker;
    LatLng currentLoaction;
    private View view;
    private TextView tvLat;
    private TextView tvLng;
    private Context mContext;
    public GpsFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.frag_gps,container,false);

        mContext = getActivity();

        mMapView = (MapView) view.findViewById(R.id.mapView);

        tvLat = (TextView) view.findViewById(R.id.tv_lat);
        tvLng = (TextView) view.findViewById(R.id.tv_lng);

        mMapView.onCreate(savedInstanceState);



        mMapView.onResume();

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }catch(Exception e){
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_night));

                // For showing a move to my location button
               // mMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng uwi = new LatLng(18.00445, -76.74820);
                droneMarker = googleMap.addMarker(new MarkerOptions().position(uwi).title("EPOS").snippet("R.A.I.N"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(uwi).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        return view;
    }


    public void update_data(Map<String,String> dataMap)
    {
        float latitude = Float.valueOf(dataMap.get("latitude"));
        float longitude = Float.valueOf(dataMap.get("longitude"));

        tvLat.setText(latitude+"");
        tvLng.setText(longitude+"");

        droneMarker.setPosition(new LatLng(latitude,longitude));

    }
}
