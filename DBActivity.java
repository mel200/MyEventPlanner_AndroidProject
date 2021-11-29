package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBActivity extends AppCompatActivity {
    protected interface OnQuerySuccess {
        public void OnSuccess();
    }

    protected interface OnSelectSuccess {
        public void OnElementSelected(
                String ID, String Name, String Tel, String Event, String Place, String Time, String Date
        );
    }
    // protected String DBFILE = getFilesDir().getPath()+"/EVENT.db";

    protected boolean matchString(String string_, String regexp) {
        final String regex = regexp;
        final String string = string_;

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            return true;
        }
        return false;
    }

    protected void validation(EditText editEvent, EditText editTel) throws Exception {


        if (!matchString(editTel.getText().toString(), "^(\\+|00)?\\d+(\\/\\d+)?$")) {
            throw new Exception("Invalid Phone");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void SelectSQL(String SelectQ,
                             String[] args,
                             OnSelectSuccess success
    )
            throws Exception {
        SQLiteDatabase db = SQLiteDatabase
                .openOrCreateDatabase(getFilesDir().getPath() + "/NEWEVENT.db", null);
        Cursor cursor = db.rawQuery(SelectQ, args);
        while (cursor.moveToNext()) {
            String ID = cursor.getString(cursor.getColumnIndex("ID"));
            String Name = cursor.getString(cursor.getColumnIndex("Name"));
            String Tel = cursor.getString(cursor.getColumnIndex("Tel"));
            String Event = cursor.getString(cursor.getColumnIndex("Event"));
            String Place = cursor.getString(cursor.getColumnIndex("Place"));
            String Time = cursor.getString(cursor.getColumnIndex("Time"));
            String Date = cursor.getString(cursor.getColumnIndex("Date"));
            success.OnElementSelected(ID, Name, Tel, Event, Place, Time, Date);
        }
        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception {
        SQLiteDatabase db = SQLiteDatabase
                .openOrCreateDatabase(getFilesDir().getPath() + "/NEWEVENT.db", null);
        if (args != null)
            db.execSQL(SQL, args);
        else
            db.execSQL(SQL);

        db.close();
        success.OnSuccess();
    }

    protected void initDB() throws Exception {
        ExecSQL(
                "CREATE TABLE if not exists NEWEVENT( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null, " +
                        "Tel text not null, " +
                        "Event text not null, " +
                        "Place text not null, " +
                        "Time text not null, " +
                        "Date text not null, " +
                        "unique(Name, Tel) " +
                        ")",
                null,
                () -> Toast.makeText(getApplicationContext(),
                        "DB Init Successful", Toast.LENGTH_LONG).show()

        );
    }


}