package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ryan on 1/27/18.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;
    private final ForecastAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        int layoutID = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;
        View item = inflater.inflate(layoutID,parent,attachToParent);

        return new ForecastAdapterViewHolder(item);

    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position){
        holder.mWeatherTextView.setText(mWeatherData[position]);
    }

    @Override
    public int getItemCount(){
        if (mWeatherData == null) {
            return 0;
        } else {
            return mWeatherData.length;
        }
    }

    void setWeatherData(String[] weatherData){
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View itemView){
            super(itemView);
            mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String weatherForDay = mWeatherData[adapterPosition];
            mClickHandler.onClick(weatherForDay);
        }

    }


}
