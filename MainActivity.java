package com.example.myapplication2;

import androidx.annotation.CallSuper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;


public class MainActivity extends DBActivity {
    protected EditText editName, editTel, editEvent, editPlace, editTime, editDate;
    protected Button btnInsert;
    protected ListView simpleList;
    protected void FillListView() throws Exception{
        final ArrayList<String> listResults=
                new ArrayList<>();
        SelectSQL(
                "SELECT * FROM NEWEVENT ORDER BY Name",
                null,
                (ID, Name, Tel, Event, Place, Time, Date)->{
                    listResults.add(ID+"\t"+Name+"\t"+Tel+"\n"+Event+"\t"+Place+"\t"+Time+"\t"+Date+"\t");
                }
        );
        simpleList.clearChoices();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_listview,
                R.id.textView,
                listResults

        );
        simpleList.setAdapter(arrayAdapter);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName=findViewById(R.id.editName);
        editTel=findViewById(R.id.editTel);
        editEvent=findViewById(R.id.editEvent);
        editPlace=findViewById(R.id.editPlace);
        editTime=findViewById(R.id.editTime);
        editDate=findViewById(R.id.editDate);
        btnInsert=findViewById(R.id.btnInsert);
        simpleList=findViewById(R.id.simpleList);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView clickedText=view.findViewById(R.id.textView);
                String selected = clickedText.getText().toString();
                String[] elements=selected.split("\t");
                String ID=elements[0];
                String Name=elements[1];
                String Tel=elements[2].trim();
                String Event=elements[3];
                String Place=elements[4];
                //String Time=elements[5];
                //String Date=elements[6];
                Intent intent=new Intent(MainActivity.this,
                        UpdateDelete.class
                );
                Bundle b=new Bundle();
                b.putString("ID", ID);
                b.putString("Name", Name);
                b.putString("Tel", Tel);
                b.putString("Event", Event);
                b.putString("Place", Place);
                //b.putString("Time", Time);
                //b.putString("Date", Date);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);






            }
        });



        try {
            initDB();
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnInsert.setOnClickListener(view -> {
            try {
                validation(editTime , editTel);

                ExecSQL(
                        "INSERT INTO NEWEVENT(Name, Tel, Event, Place, Time, Date) " +
                                "VALUES(?, ?, ?, ?, ?, ?) ",
                        new Object[]{
                                editName.getText().toString(),
                                editTel.getText().toString(),
                                editEvent.getText().toString(),
                                editPlace.getText().toString(),
                                editTime.getText().toString(),
                                editDate.getText().toString()
                        },
                        ()-> Toast.makeText(getApplicationContext(),
                                "Record Inserted", Toast.LENGTH_LONG).show()

                );
                FillListView();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Insert Failed: "+e.getLocalizedMessage()
                        , Toast.LENGTH_SHORT).show();
            }

        });

    }


}