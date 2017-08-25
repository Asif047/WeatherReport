package com.pencilbox.user.getcurrentlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity  extends AppCompatActivity {
    private static final String TAG = "Location";
    private static final String APPID = "380199723cebdb85ef2e16cc30cee5b6";
    private static final String METIC = "metric";
    private static final String IMPERIAL = "imperial";
    private static final int CNT = 7;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    private List<Address> addressList;

    private Retrofit retrofit;
    private WeatherApiService weatherApiService;
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private TextView showTempTV,dayNameTV,minTV,maxTV,addresslineTV,cityTV,countryTV;
    private Button next_button;
    static double day;
    static double min;
    static double max;

    public static  List<WeatherForecastResponse.ForecastList>forecastLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        showTempTV= (TextView) findViewById(R.id.showTempTextView);
        dayNameTV= (TextView) findViewById(R.id.showDayTextView);
        minTV= (TextView) findViewById(R.id.showMinTextView);
        maxTV= (TextView) findViewById(R.id.showMaxTextView);

        addresslineTV= (TextView) findViewById(R.id.addressLineTextView);
        cityTV= (TextView) findViewById(R.id.cityTextView);
        countryTV= (TextView) findViewById(R.id.countryTextView);

        next_button= (Button) findViewById(R.id.nextButton);

        geocoder = new Geocoder(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApiService = retrofit.create(WeatherApiService.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location : locationResult.getLocations()){

                    requestForecastResponse(location.getLatitude(),location.getLongitude());
                    Log.e(TAG, "current Latitude: "+location.getLatitude());
                    Log.e(TAG, "current Longitude: "+location.getLongitude());
                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        Log.e(TAG, "address line: "+addressList.get(0).getAddressLine(0));
                        Log.e(TAG, "city: "+addressList.get(0).getLocality());
                        Log.e(TAG, "country: "+addressList.get(0).getCountryName());
                        Log.e(TAG, "postal code: "+addressList.get(0).getPostalCode());
                        Log.e(TAG, "sublocality: "+addressList.get(0).getSubLocality());
                        Log.e(TAG, "phone: "+addressList.get(0).getPhone());



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };
        getLastLocation();
        createLocationUpadates();

        
        //day name


        Calendar c = Calendar.getInstance();
        SimpleDateFormat sd=new SimpleDateFormat("EEEE");
        String dayofweek=sd.format(c.getTime());

        dayNameTV.setText(dayofweek);



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




    private void requestForecastResponse(double latitude, double longitude) {

        String urlString = String.format("forecast/daily?lat=%f&lon=%f&cnt=%d&appid=%s",latitude,longitude,CNT,APPID);
        Call<WeatherForecastResponse>weatherForecastResponseCall = weatherApiService.getForecastResponse(urlString);
        weatherForecastResponseCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if(response.code()== 200){
                    WeatherForecastResponse weatherForecastResponse = response.body();
                   forecastLists = weatherForecastResponse.getList();





//                    Intent intent2=new Intent(MainActivity.this,WeatherReport.class);
//                    intent2.putExtra("forecast", (Serializable) forecastLists);


                    //getting day temperature value
                    day= forecastLists.get(0).getTemp().getDay()-273;

                    DecimalFormat df = new DecimalFormat("#.##");
                    double day_weather = Double.parseDouble(df.format(day));


                    //getting min temperature value
                    min= forecastLists.get(0).getTemp().getMin()-273;

                    DecimalFormat df2 = new DecimalFormat("#.##");
                    double min_weather = Double.parseDouble(df.format(min));



                    //getting max temperature value
                    max= forecastLists.get(0).getTemp().getMax()-273;

                    DecimalFormat df3 = new DecimalFormat("#.##");
                    double max_weather = Double.parseDouble(df.format(max));



                   // showCloudTV.setText(forecastLists.get(6).getTemp().getDay().toString());

                    showTempTV.setText(day_weather+"°c");
                    minTV.setText(min_weather+"°c");
                    maxTV.setText(max_weather+"°c");


                    addresslineTV.setText(addressList.get(0).getAddressLine(0)+",");
                    cityTV.setText(addressList.get(0).getLocality());
                    countryTV.setText(addressList.get(0).getCountryName());


                    forecastLists.get(0).getWeather().get(0).getId();


                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void createLocationUpadates() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    Toast.makeText(MainActivity.this, "Latitude: " + location.getLatitude() +
                            "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "latitude"+location.getLatitude());
                    Log.e(TAG, "logintude"+location.getLongitude());

                }

            }
        });
    }

    public void nextActivity(View view) {

        Intent intent=new Intent(MainActivity.this,ForecastActivity.class);

//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("forecast", (ArrayList<? extends Parcelable>) forecastLists);
//        intent.putExtras(bundle);
        startActivity(intent);

    }


    public void currentWeatherActivity(View view) {
        Intent intent=new Intent(MainActivity.this,MainActivity.class);

//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("forecast", (ArrayList<? extends Parcelable>) forecastLists);
//        intent.putExtras(bundle);
        startActivity(intent);

    }
}
