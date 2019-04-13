package com.toanvq.fpoly.dubaothoitiet;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText edtSeach;
    private Button btnSearch;
    private TextView tvTenthanhpho;
    private TextView tvTenquocgia;
    private ImageView imgIcon;
    private TextView tvNhietDo;
    private TextView tvTrangThai;
    private TextView tvHumidity;
    private TextView tvCloud;
    private TextView tvWind;
    private TextView tvDay;
    private Button btnChangeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSeach = (EditText) findViewById(R.id.edtSeach);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvTenthanhpho = (TextView) findViewById(R.id.tvTenthanhpho);
        tvTenquocgia = (TextView) findViewById(R.id.tvTenquocgia);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);
        tvNhietDo = (TextView) findViewById(R.id.tvNhietDo);
        tvTrangThai = (TextView) findViewById(R.id.tvTrangThai);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvCloud = (TextView) findViewById(R.id.tvCloud);
        tvWind = (TextView) findViewById(R.id.tvWind);
        tvDay = (TextView) findViewById(R.id.tvDay);
        btnChangeActivity = (Button) findViewById(R.id.btnChangeActivity);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtSeach.getText().toString();
                GetCurrentWeatherData(city);
            }
        });
    }

    public void GetCurrentWeatherData(final String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=8f423259303c98a3ad37848b2fbc6bff";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            tvTenthanhpho.setText(name);

                            long lday = Long.valueOf(day);
                            Date date = new Date(lday*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String day1 = simpleDateFormat.format(date);
                            tvDay.setText(day1);

                            JSONArray jsonArrayWeather  = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgIcon);
                            tvTrangThai.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");
                            Double a = Double.valueOf(nhietdo);
                            String NhietDo = String.valueOf(a.intValue());
                            tvNhietDo.setText(NhietDo+"°C");
                            tvHumidity.setText(doam+"%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            tvWind.setText(gio+"m/s");

                            JSONObject jsonObjectClounds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClounds.getString("all");
                            tvCloud.setText(may+"%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String quocgia = jsonObjectSys.getString("country");
                            tvTenquocgia.setText(quocgia);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
    }
}
