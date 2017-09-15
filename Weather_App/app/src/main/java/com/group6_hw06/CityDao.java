package com.group6_hw06;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shashank on 10/16/2016.
 */
public class CityDao {
    private SQLiteDatabase db;

    public CityDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(City city) {
        //db.execSQL("DROP TABLE IF EXISTS city");
        ContentValues values = new ContentValues();
        values.put(CityTable.COLUMN_CITYNAME, city.getCityName());
        values.put(CityTable.COLUMN_COUNTRY, city.getCountry());
        values.put(CityTable.COLUMN_TEMPERATURE, city.getTemperature());
        values.put(CityTable.COLUMN_FAVORITE, city.getFavorite());
        values.put(CityTable.COLUMN_DATE, city.getDate());
        return db.insert(CityTable.TABLENAME, null, values);
    }

   /* public boolean update(City city) {
        ContentValues values = new ContentValues();
        values.put(CityTable.COLUMN_CITYNAME, city.getCityName());
        values.put(CityTable.COLUMN_COUNTRY, city.getCountry());
        values.put(CityTable.COLUMN_TEMPERATURE, city.getTemperature());
        values.put(CityTable.COLUMN_FAVORITE, city.getFavorite());
        return db.update(CityTable.TABLENAME, values, CityTable.COLUMN_CITYNAME+"=?", new String[] {city.getCityName()+""})>0;
    }*/
   public boolean updateTemperature(String cityName, String temperature, String date) {
       ContentValues values = new ContentValues();
       //values.put(CityTable.COLUMN_CITYNAME, city.getCityName());
       //values.put(CityTable.COLUMN_COUNTRY, city.getCountry());
       values.put(CityTable.COLUMN_TEMPERATURE, temperature);
      // values.put(CityTable.COLUMN_FAVORITE, city.getFavorite());
       values.put(CityTable.COLUMN_DATE, date);
       return db.update(CityTable.TABLENAME, values, CityTable.COLUMN_CITYNAME+"=?", new String[] {cityName})>0;
   }
    public boolean updateFavorite(String cityName, String favorite) {
        ContentValues values = new ContentValues();
        //values.put(CityTable.COLUMN_CITYNAME, city.getCityName());
        //values.put(CityTable.COLUMN_COUNTRY, city.getCountry());
       // values.put(CityTable.COLUMN_TEMPERATURE, temperature);
         values.put(CityTable.COLUMN_FAVORITE, favorite);
        return db.update(CityTable.TABLENAME, values, CityTable.COLUMN_CITYNAME+"=?", new String[] {cityName})>0;
    }

    public boolean delete(City city) {
        return db.delete(CityTable.TABLENAME, CityTable.COLUMN_CITYNAME+"=?", new String[] {city.getCityName()+""})>0;
    }

    public City get(String cityName) {
        City city = null;
        Cursor c = db.query(true, CityTable.TABLENAME, new String[] {CityTable.COLUMN_CITYNAME, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_TEMPERATURE, CityTable.COLUMN_FAVORITE, CityTable.COLUMN_DATE},
                CityTable.COLUMN_CITYNAME+"="+cityName, null, null, null, null, null);
        if(c!=null &&   c.moveToFirst()) {
            city = buildNoteFromCursor(c);
            if(!c.isClosed()) {
                c.close();
            }
        }
        return city;
    }

    public List<City> getAll() {
        List<City> favorites = new ArrayList<>();
        List<City> cities = new ArrayList<City>();
        Cursor c = db.query(CityTable.TABLENAME, new String[] {CityTable.COLUMN_CITYNAME, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_TEMPERATURE, CityTable.COLUMN_FAVORITE, CityTable.COLUMN_DATE}, null, null, null, null, null);
        if(c!=null &&   c.moveToFirst()) {
            do {
                City city = buildNoteFromCursor(c);
                if(city!=null) {
                    cities.add(city);
                }
            } while (c.moveToNext());
            if(!c.isClosed()) {
                c.close();
            }
        }
        List<City> newList = new ArrayList<>();
        newList.addAll(cities);
        for(City city: cities) {
            if(city.getFavorite().equals("true")) {
                favorites.add(city);
                newList.remove(city);
            }
        }
        newList.addAll(0, favorites);
        return newList;
    }

    private City buildNoteFromCursor(Cursor c) {
        City city = null;
        if(c!=null) {
            city = new City(c.getString(0),c.getString(1), c.getString(2), c.getString(3), c.getString(4));
        }
        return city;
    }
}
