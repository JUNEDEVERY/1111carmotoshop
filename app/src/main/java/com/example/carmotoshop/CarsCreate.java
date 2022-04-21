package com.example.carmotoshop;

import android.content.ContentValues;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CarsCreate extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear;
    EditText brand, model, carPrice;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_create);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        brand = findViewById(R.id.brand);
        model = findViewById(R.id.model);
        carPrice = findViewById(R.id.carPrice);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();

        brand.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    brand.setHint("");
                else
                    brand.setHint("Марка");
            }
        });
        model.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    model.setHint("");
                else
                    model.setHint("Модель");
            }
        });
        carPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    carPrice.setHint("");
                else
                    carPrice.setHint("Стоимость");
            }
        });
    }
    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_CARS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID1);
            int carBrandIndex = cursor.getColumnIndex(DBHelper.KEY_CARBRAND);
            int carModelIndex = cursor.getColumnIndex(DBHelper.KEY_CARMODEL);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICECAR);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                outputID.setTextSize(12);
                dbOutputRow.addView(outputID);

                TextView outputStart = new TextView(this);
                params.weight = 3.0f;
                outputStart.setLayoutParams(params);
                outputStart.setText(cursor.getString(carBrandIndex));
                outputStart.setTextSize(12);
                dbOutputRow.addView(outputStart);

                TextView outputEnd = new TextView(this);
                params.weight = 3.0f;
                outputEnd.setLayoutParams(params);
                outputEnd.setText(cursor.getString(carModelIndex));
                outputEnd.setTextSize(12);
                dbOutputRow.addView(outputEnd);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                outputPrice.setTextSize(12);
                dbOutputRow.addView(outputPrice);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить\nзапись");
                deleteBtn.setTextSize(12);
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String carBrand = brand.getText().toString();
                String carModel = model.getText().toString();
                String price = carPrice.getText().toString();
                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_CARBRAND, carBrand);
                contentValues.put(DBHelper.KEY_CARMODEL, carModel);
                contentValues.put(DBHelper.KEY_PRICECAR, price);

                database.insert(DBHelper.TABLE_CARS, null, contentValues);
                brand.setText("");
                model.setText("");
                carPrice.setText("");
                UpdateTable();
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CARS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_CARS, DBHelper.KEY_ID1 + " = ?", new String[]{String.valueOf(v.getId())});

                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_CARS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID1);
                    int carBrandIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_CARBRAND);
                    int carModelIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_CARMODEL);
                    int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICECAR);
                    int realID = 1;
                    do{
                        if(cursorUpdater.getInt(idIndex) > idIndex){
                            contentValues.put(DBHelper.KEY_ID1, realID);
                            contentValues.put(DBHelper.KEY_CARBRAND, cursorUpdater.getString(carBrandIndex));
                            contentValues.put(DBHelper.KEY_CARMODEL, cursorUpdater.getString(carModelIndex));
                            contentValues.put(DBHelper.KEY_PRICECAR, cursorUpdater.getString(priceIndex));
                            database.replace(DBHelper.TABLE_CARS, null, contentValues);
                        }
                        realID++;
                    } while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast() && (cursorUpdater.getInt(idIndex) == realID)){
                        database.delete(DBHelper.TABLE_CARS, DBHelper.KEY_ID1 + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}
