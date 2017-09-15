package com.group6_hw06;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shruti on 10/16/2016.
 */
public class CityTable {
    static final String TABLENAME = "city";
    static final String COLUMN_CITYNAME = "city_name";
    static final String COLUMN_COUNTRY = "country";
    static final String COLUMN_TEMPERATURE = "temperature";
    static final String COLUMN_FAVORITE = "favorite";
    static final String COLUMN_DATE = "date";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+TABLENAME+" (");
        sb.append(COLUMN_CITYNAME+" text primary key not null, ");
        sb.append(COLUMN_COUNTRY+" text not null, ");
        sb.append(COLUMN_TEMPERATURE+" text not null, ");
        sb.append(COLUMN_FAVORITE+" text not null, ");
        sb.append(COLUMN_DATE+" text);");
        //db.execSQL("DROP TABLE IF EXISTS city");
        try {
            db.execSQL(sb.toString());
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        CityTable.onCreate(db);
    }

}
