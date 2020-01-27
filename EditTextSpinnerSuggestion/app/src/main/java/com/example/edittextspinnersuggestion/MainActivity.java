package com.example.edittextspinnersuggestion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String[] items={"ABc","AEF","aDE","ALE","ale"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView imageView=findViewById(R.id.image);
        final AutoCompleteTextView text=(AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,items);
        text.setAdapter(adp);
        text.setThreshold(2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.showDropDown();
            }
        });


        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Item : "+ items[position]+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
