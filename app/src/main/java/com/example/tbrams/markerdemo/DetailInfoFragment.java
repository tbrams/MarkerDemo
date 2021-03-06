package com.example.tbrams.markerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tbrams.markerdemo.data.MarkerLab;
import com.example.tbrams.markerdemo.data.MarkerObject;

import java.util.List;
import java.util.Locale;


public class DetailInfoFragment extends Fragment {
    public static final String EXTRA_MARKER_ID = "com.example.tbrams.markerdemo.marker_id";


    MarkerLab markerLab = MarkerLab.getMarkerLab(getActivity());
    List<MarkerObject> markerList = markerLab.getMarkers();


    public static Fragment newInstance(int markerIndex) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MARKER_ID, markerIndex);

        DetailInfoFragment fragment = new DetailInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_nav_details,container,false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_work_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Click here when you are at the runway", Snackbar.LENGTH_LONG)
                        .setAction("Go!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // If we have at least two segments, we can start the program
                                if (markerList.size()>=3) {
                                    Intent intent = new Intent(getActivity(), TimeActivity.class);
                                    startActivity(intent);

                                    Log.d("TBR:", "TimeActivity Started (Plan execution)...");

                                } else {
                                    Toast.makeText(getActivity(), "We need at least two legs for before we can launch", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
            }
        });


        // Get marker id from argument bundle
        int markerIndex = (int) getArguments().getSerializable(EXTRA_MARKER_ID);

        TextView tvFromWP = (TextView) v.findViewById(R.id.tvFromWP);
        TextView tvToWP = (TextView) v.findViewById(R.id.tvToWP);
        TextView tvDist = (TextView) v.findViewById(R.id.tvDist);
        TextView tvMinAlt = (TextView) v.findViewById(R.id.tvMinAlt);
        TextView tvAlt = (TextView) v.findViewById(R.id.tvAlt);
        TextView tvIAS = (TextView) v.findViewById(R.id.tvIAS);
        TextView tvTAS = (TextView) v.findViewById(R.id.tvTAS);
        TextView tvWv = (TextView) v.findViewById(R.id.tvWV);
        TextView tvGS = (TextView) v.findViewById(R.id.tvGS);
        TextView tvTT = (TextView) v.findViewById(R.id.tvTT);
        TextView tvTH = (TextView) v.findViewById(R.id.tvTH);
        TextView tvWCA = (TextView) v.findViewById(R.id.tvWCA);
        TextView tvVAR = (TextView) v.findViewById(R.id.tvVAR);
        TextView tvMH = (TextView) v.findViewById(R.id.tvMH);
        TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
        TextView tvAccTime = (TextView) v.findViewById(R.id.tvAccTime);


        if (markerIndex <markerList.size()) {
            tvFromWP.setText(markerList.get(markerIndex).getMarker().getTitle());
            tvToWP.setText(markerList.get(markerIndex +1).getMarker().getTitle());

            MarkerObject mo = markerList.get(markerIndex +1);
            tvDist.setText(String.format(Locale.ENGLISH, "%.1f nm", mo.getDist()));
            tvIAS.setText(String.format(Locale.ENGLISH, "%.1f kts", mo.getIAS()));
            tvGS.setText(String.format(Locale.ENGLISH, "%.1f kts", mo.getGS()));
            tvTT.setText(String.format(Locale.ENGLISH, "%03d ˚", (int)mo.getTT()));
            tvTH.setText(String.format(Locale.ENGLISH, "%03d ˚", (int)mo.getTH()));
            tvWCA.setText(String.format(Locale.ENGLISH, "%d ˚",  (int)mo.getWCA()));
            tvVAR.setText(String.format(Locale.ENGLISH, "%.1f ˚", mo.getVAR()));
            tvMH.setText(String.format(Locale.ENGLISH, "%03d ˚", (int)mo.getMH()));
            tvTAS.setText(String.format(Locale.ENGLISH, "%.1f kts", mo.getTAS()));
            tvAlt.setText(String.format(Locale.ENGLISH, "%.1f ft", mo.getALT()));
            tvMinAlt.setText(String.format(Locale.ENGLISH, "%.1f ft", mo.getMIN_ALT()));
            tvTime.setText(String.format(Locale.ENGLISH, "%02d", (int)mo.getTIME()));

            String wind = String.format(Locale.ENGLISH, "%03d", (int)mo.getWindDir())+"/"+String.format(Locale.ENGLISH, "%02d", (int)mo.getWindStr());
            tvWv.setText(wind);

            double time=0;
            for (int i = 1; i<= markerIndex +1; i++){
                time+=markerList.get(i).getTIME();
            }

            String hh="";
            String mm="";
            String ss="";

            int hrs=(int)(time/60);
            int min=(int)(time%60);
            tvAccTime.setText(String.format(Locale.ENGLISH, "%02d:%02d", hrs, min));
        }

            return v;
    }

}
