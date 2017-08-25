package com.pencilbox.user.getcurrentlocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 7/13/2017.
 */

public class WeatherReport {

    private double day;
    private double night;
    private double morn;
    private long date;

    //List<WeatherForecastResponse.ForecastList> forecast = (List<WeatherForecastResponse.ForecastList>) getIntent().getSerializableExtra("forecast");


    public WeatherReport(double day, double night, double morn, long date) {
        this.day = day;
        this.night = night;
        this.morn = morn;
        this.date = date;
    }

    public WeatherReport(double day, double night, double morn) {
        this.day = day;
        this.night = night;
        this.morn = morn;
    }




    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    public long getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
