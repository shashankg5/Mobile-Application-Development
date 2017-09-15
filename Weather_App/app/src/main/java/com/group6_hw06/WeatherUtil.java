package com.group6_hw06;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shruti on 10/8/2016.
 */
public class WeatherUtil {
    static public class WeatherJSONParser {
        static ArrayList<Weather> parseWeather(String in) {
            ArrayList<Weather> weatherList = new ArrayList<>();
            int minTemp = 100000;
            int maxTemp = -100000;
            try {
                JSONObject root = new JSONObject(in);
                JSONArray list = root.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject hourlyForecastObject = list.getJSONObject(i);
                        Weather weather = new Weather();
                        weather.setDate(hourlyForecastObject.getString("dt_txt"));
                        weather.setTemperature(hourlyForecastObject.getJSONObject("main").getString("temp"));
                        Double temp = (Double.parseDouble(weather.getTemperature())-32)*5/9;
                        weather.setTemperatureInC(String.format("%.2f", temp));
                        weather.setPressure(hourlyForecastObject.getJSONObject("main").getString("pressure")+" hPa");
                        weather.setHumidity(hourlyForecastObject.getJSONObject("main").getString("humidity")+"%");

                        JSONArray arr = hourlyForecastObject.getJSONArray("weather");
                        for (int y = 0; y < arr.length(); y++) {
                            JSONObject obj = arr.getJSONObject(y);
                            weather.setCondition(obj.getString("description"));
                            weather.setIconUrl("http://openweathermap.org/img/w/" + obj.getString("icon") + ".png");
                        }
                        weather.setWind(hourlyForecastObject.getJSONObject("wind").getString("speed")+" mps, "+hourlyForecastObject.getJSONObject("wind").getString("deg") + (char) 0x00B0);
                        weatherList.add(weather);

                    }

                return weatherList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weatherList;
        }
    }

}
