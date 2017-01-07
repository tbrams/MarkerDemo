package com.example.tbrams.markerdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tbrams.markerdemo.db.DataSource;
import com.example.tbrams.markerdemo.dbModel.TripItem;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    public static final String WP_KEY = "markerdemo.tbrams.wp_key";
    public static final String TRIP_KEY = "markerdemo.tbrams.trip_key";

    private List<TripItem>  mTrips;
    private Context         mContext;
    private DataSource mDataSource;



    /*
     * constructor
     * Keep context and a copy of the data list
     */
    public TripAdapter(Context context, List<TripItem> trips) {
        this.mContext = context;
        this.mTrips = trips;

        // Get a handle to the database helper and prepare the database
        mDataSource = new DataSource(mContext);
        mDataSource.open();
    }


    /*
     * Prepare the list_element layout for new list elements
     */
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_element, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    /*
     * Display list item number(position) using handles from ViewHolder
     * Also alternate background color on uneven rows
     */
    @Override
    public void onBindViewHolder(TripAdapter.ViewHolder holder, int position) {
        final TripItem trip = mTrips.get(position);

        holder.tvName.setText(trip.getTripName());
        holder.tvDist.setText(String.format("%.2f nm",trip.getTripDistance()));
        holder.tvDate.setText(trip.getTripDate());

        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#A4A4A4"));
        }

    }


    @Override
    public int getItemCount() {
        return mTrips.size();
    }


    /*
     * Get handle to fields for display
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView tvName;
        public TextView tvDist;
        public TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tripNameText);
            tvDist = (TextView) itemView.findViewById(R.id.tripDistText);
            tvDate = (TextView) itemView.findViewById(R.id.tripDateText);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        /*
         * Start a detail session based on Trip Id
         */
        @Override
        public void onClick(View view) {

            TripItem trip= mTrips.get(this.getAdapterPosition());
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(TRIP_KEY, trip.getTripId());
            Log.d("TBR","Passing id: "+trip.getTripId());
            mContext.startActivity(intent);
        }



        /*
         * This is the severe action - delete the Trip
         */
        @Override
        public boolean onLongClick(View view) {
            Log.d("TBR", "ViewHolder Long Clicked");

            TripItem trip = mTrips.get(this.getAdapterPosition());

            if (MainActivity.isThisDbMaintenance()) {

                // Remove from the list and update the listview
                mTrips.remove(this.getAdapterPosition());
                notifyDataSetChanged();

                Toast.makeText(mContext, "You deleted " + trip.getTripName(), Toast.LENGTH_SHORT).show();

                // Drop from database
                mDataSource.deleteTrip(trip);

                return false;

            } else {

                // Trip Selection Mode - Start this trip at WP 0

                Intent intent = new Intent(mContext, MarkerDemoActivity.class);
                intent.putExtra(TRIP_KEY, trip.getTripId());
                intent.putExtra(WP_KEY, 0);
                Log.d("TBR","Passing Trip# "+trip.getTripId()+" and WP #0");
                mContext.startActivity(intent);

                return false;

            }
        }
    }
}