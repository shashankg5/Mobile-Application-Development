package com.group6_hw06;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by shashank on 10/16/2016.
 */
public class DatabaseDataManager {
    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private CityDao cityDAO;

    public DatabaseDataManager(Context mContext) {

        this.mContext = mContext;
        this.dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        this.db = dbOpenHelper.getWritableDatabase();
        this.cityDAO = new CityDao(db);
    }

    public void close() {
        if(db!=null) {
            db.close();
        }
    }

    public CityDao getNoteDAO() {
        return this.cityDAO;
    }

    public long saveNote(City city) {
        return cityDAO.save(city);
    }

    public boolean updateNoteTemperature(String cityName, String temperature, String date) {
        return cityDAO.updateTemperature(cityName, temperature, date);
    }
    public boolean updateNoteFavorite(String cityName, String favorite) {
        return cityDAO.updateFavorite(cityName, favorite);
    }
    public boolean deleteNote(City city) {
        return cityDAO.delete(city);
    }
    public City getNote(String cityName) {
        return cityDAO.get(cityName);
    }
    public List<City> getAllNotes() {
        return  cityDAO.getAll();
    }
}
