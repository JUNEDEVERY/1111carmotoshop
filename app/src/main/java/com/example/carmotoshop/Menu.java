package com.example.carmotoshop;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Menu extends AppCompatActivity implements View.OnClickListener {
    Button btnCar, btnMotorcycle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnCar = findViewById(R.id.btnCar);
        btnCar.setOnClickListener(this);

        btnMotorcycle = findViewById(R.id.btnMotorcycles);
        btnMotorcycle.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnCar:
                startActivity(new Intent(this, CarsCreate.class));
                break;
            case R.id.btnMotorcycles:
                startActivity(new Intent(this, MotoCreate.class));
                break;

        }
    }
}
