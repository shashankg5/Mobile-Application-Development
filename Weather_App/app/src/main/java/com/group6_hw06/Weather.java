package com.group6_hw06;

import java.io.Serializable;

/**
 * Created by shruti on 10/8/2016.
 */
public class Weather implements Serializable{

    private String temperature, temperatureInC;
    private String iconUrl;
    private String date, condition, pressure, humidity, wind;

    public String getTemperatureInC() {
        return temperatureInC;
    }

    public void setTemperatureInC(String temperatureInC) {
        this.temperatureInC = temperatureInC;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
