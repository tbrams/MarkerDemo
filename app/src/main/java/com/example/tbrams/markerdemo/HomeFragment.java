package com.example.tbrams.markerdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.tbrams.markerdemo.data.MarkerLab;
import com.example.tbrams.markerdemo.db.DataSource;
import com.example.tbrams.markerdemo.dbModel.TripItem;

import static com.example.tbrams.markerdemo.TripAdapter.TRIP_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG="TBR:HF";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.front_page, container, false);

        final TextView txtInstr = (TextView) view.findViewById(R.id.frontTextInstruction);
        final Button createBtn = (Button) view.findViewById(R.id.frontCreateBtn);
        final Button browseBtn = (Button) view.findViewById(R.id.frontBrowseBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogStyle);
                builder.setTitle("Title");

                // The android.R.id.content is a good way to identify the root view
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.enter_name, (ViewGroup) view.findViewById(android.R.id.content), false);

                // Set up the input
                final AutoCompleteTextView input = (AutoCompleteTextView) viewInflated.findViewById(R.id.input_trip_name);
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();

                        // Any name is OK, use it for constructor and store it in the database
                        TripItem trip = new TripItem(null, name, null, null);
                        DataSource datasource = new DataSource(getContext());
                        datasource.open();
                        // CreateTrip returns an object with a unique ID when not
                        // given a specific ID
                        trip = datasource.createTrip(trip);
                        datasource.close();

                        Log.d(TAG, "Newly created trip ID : " + trip.getTripId());

                        // Save trip name in markerLab singleton storage
                        MarkerLab markerLab = MarkerLab.getMarkerLab(getContext());
                        markerLab.setTripName(trip.getTripName());

                        // Start MarkerDemoActivity with tripID extra argument
                        Intent intent = new Intent(getContext(), MarkerDemoActivity.class);
                        intent.putExtra(TRIP_KEY, trip.getTripId());
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // not so much to do here....
                        dialog.cancel();
                    }
                });


                builder.show();

            }
        });


        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Browsing existing trips is done from the MainActivity class
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }



// Not sure what this one was intended for to be honest...
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    // Get a handle on the Acitivy implementation of the the listener here
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity_about_us and potentially other fragments contained in that
     * activity_about_us.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
