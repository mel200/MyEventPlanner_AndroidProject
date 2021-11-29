package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class UpdateDelete extends DBActivity {
    protected EditText editName, editTel, editEvent, editPlace, editTime, editDate;
    protected Button btnUpdate, btnDelete;
    protected String ID;

    private void BackToMain(){
        finishActivity(200);
        Intent i=new Intent(UpdateDelete.this, MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editName=findViewById(R.id.editName);
        editTel=findViewById(R.id.editTel);
        editEvent=findViewById(R.id.editEvent);
        editPlace=findViewById(R.id.editPlace);
        editTime=findViewById(R.id.editTime);
        editDate=findViewById(R.id.editDate);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            editName.setText(b.getString("Name"));
            editTel.setText(b.getString("Tel"));
            editEvent.setText(b.getString("Event"));
            editPlace.setText(b.getString("Place"));
            editTime.setText(b.getString("Time"));
            editDate.setText(b.getString("Date"));
        }
        btnDelete.setOnClickListener(view -> {
            try{
                ExecSQL("DELETE FROM NEWEVENT WHERE " +
                                "ID = ?",
                        new Object[]{ID},
                        ()-> Toast.makeText(getApplicationContext(),
                                "Delete Successful", Toast.LENGTH_LONG).show()
                );

            }catch (Exception exception){
                Toast.makeText(getApplicationContext(),
                        "Delete Error: "+exception.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }finally {
                BackToMain();
            }
        });

        btnUpdate.setOnClickListener(view -> {
            try{
                validation(editEvent, editTel);
                ExecSQL("UPDATE NEWEVENT SET " +
                                "Name = ?, " +
                                "Tel = ?, " +
                                "Event = ?, " +
                                "Place = ?, " +
                                "Time = ?, " +
                                "Date = ? " +
                                "WHERE ID = ?",
                        new Object[]{
                                editName.getText().toString(),
                                editTel.getText().toString(),
                                editEvent.getText().toString(),
                                editPlace.getText().toString(),
                                editTime.getText().toString(),
                                editDate.getText().toString(),
                                ID},
                        ()-> Toast.makeText(getApplicationContext(),
                                "Update Successful", Toast.LENGTH_LONG).show()
                );

            }catch (Exception exception){
                Toast.makeText(getApplicationContext(),
                        "Update Error: "+exception.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }finally {
                BackToMain();
            }
        });


    }
}