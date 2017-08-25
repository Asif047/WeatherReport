package com.pencilbox.user.getcurrentlocation;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by User on 7/13/2017.
 */

public class WeatherAdapter extends ArrayAdapter<WeatherReport> {

    private Context context;
    private List<WeatherReport> forecastList;

    TextView dayTV;
    TextView nightTV;
    TextView mornTV;
    TextView dateTV;
    ImageView weatherIV;

    public WeatherAdapter(Context context, List<WeatherReport> forecastList) {
        super(context,R.layout.single_list_row,forecastList);

        this.context=context;
        this.forecastList=forecastList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.single_list_row,parent,false);


        final String strDate = forecastList.get(position).getDate()+"";
        long date_val = Long.parseLong(strDate)*1000;
        Date date= new Date(date_val);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        final String dateText = df2.format(date);


        dayTV= (TextView) convertView.findViewById(R.id.day_weather);
        nightTV= (TextView) convertView.findViewById(R.id.night_weather);
        mornTV= (TextView) convertView.findViewById(R.id.morn_weather);
        dateTV= (TextView) convertView.findViewById(R.id.date_weather);


        //getting day, morning, night in celcius
        double day= forecastList.get( position).getDay()-273;

        DecimalFormat dec_f = new DecimalFormat("#.##");
        double day_weather = Double.parseDouble(dec_f.format(day));



        double morning= forecastList.get( position).getMorn()-273;

        DecimalFormat dec_f2 = new DecimalFormat("#.##");
        double day_morn = Double.parseDouble(dec_f2 .format(morning));


        double night= forecastList.get( position).getNight()-273;

        DecimalFormat dec_f3 = new DecimalFormat("#.##");
        double day_night = Double.parseDouble(dec_f2 .format(night));





        dayTV.setText("Day: "+ day_weather+"°c");
        mornTV.setText("Night: "+day_morn+"°c");
        nightTV.setText("Morning: "+day_night+"°c");
        dateTV.setText("Date: "+dateText);

        return convertView;


    }
}
