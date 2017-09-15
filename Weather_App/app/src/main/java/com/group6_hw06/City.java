package com.group6_hw06;

/**
 * Created by shashank on 10/16/2016.
 */
public class City {

    private String cityName, country, temperature;
    private String favorite, date;

    public City(String cityName, String country, String temperature, String favorite, String date) {
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.favorite = favorite;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
