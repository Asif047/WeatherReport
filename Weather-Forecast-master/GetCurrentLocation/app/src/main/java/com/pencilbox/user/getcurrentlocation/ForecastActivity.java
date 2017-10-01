package com.pencilbox.user.getcurrentlocation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ForecastActivity extends AppCompatActivity {

    ListView weatherLV;
    private WeatherAdapter weatherAdapter;

    List<WeatherReport> temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        weatherLV= (ListView) findViewById(R.id.weatherListView);


        List<WeatherForecastResponse.ForecastList> forecast = MainActivity.forecastLists;

        Log.e("TAG", "forcast size" + forecast.size());



       for(int i=0;i<forecast.size();i++)
       {
           Log.e("Forcast:"+ i, forecast.get(i).toString());

           WeatherReport weatherReport = new WeatherReport(forecast.get(i).getTemp().getDay(),forecast.get(i).getTemp().getMorn(),forecast.get(i).getTemp().getNight(),forecast.get(i).getDt());
           temp.add(weatherReport);
       }


        weatherAdapter=new WeatherAdapter(this,temp);
        weatherLV.setAdapter(weatherAdapter);



        checkNetwork();

    }



    private void checkNetwork() {
        NetworkUtil networkUtil = new NetworkUtil(this);
        if (!networkUtil.isNetworkAvailable()) {
            networkUtil.showNoNetworkDialog("Please turn on the Internet & restart the app");

            return;
        }

        if (!networkUtil.isGPSon()) {
            networkUtil.showNoNetworkDialog("Please turn on the GPS & restart the app");
        }


    }






    public void MainActivity(View view) {

        Intent intent=new Intent(ForecastActivity.this,MainActivity.class);

//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("forecast", (ArrayList<? extends Parcelable>) forecastLists);
//        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void WeatherForecastActivity(View view) {
        Intent intent=new Intent(ForecastActivity.this,ForecastActivity.class);

//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("forecast", (ArrayList<? extends Parcelable>) forecastLists);
//        intent.putExtras(bundle);
        startActivity(intent);

    }




    //back button operation starts

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    //back button operation ends



}

