package com.example.appdistribuidas;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

//import com.onesignal.OSNotificationAction;
//import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
//import okhttp3.Request;
import okhttp3.RequestBody;
//import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView botonesMenu;
    private LocationManager ubicacion;
    double lng = 0.0;
    double lat = 0.0;

    Fragment sosFragment, mapaFragment, perfilFragment;

    public static final String ONESIGNAL_APP_ID = "f9e64775-8e88-4f6d-9cfd-3db77223ace4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sosFragment = new FragmentSOS();
        mapaFragment = new FragmentMapa();
        perfilFragment = new FragmentProfile();
        //
        botonesMenu = findViewById(R.id.navegacion);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, sosFragment).commit();

        String data = getIntent().getStringExtra("usuario");
        String externalUserId = data; // You will supply the external user id to the OneSignal SDK
        OneSignal.setExternalUserId(externalUserId);

        suscribirse();

        //Métodos
        navegacionMenu();
    }


    private void navegacionMenu(){
        botonesMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch ( item.getItemId() ){
                    case R.id.btnSos:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,sosFragment).commit();
                        getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        return  true;
                    case R.id.btnMapa:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,mapaFragment).commit();
                        getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        return  true;
                    case R.id.btnPerfil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,perfilFragment).commit();
                        getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        return  true;
                }

                return false;
            }
        });
    }

    //metodo para suscribirse al OneSiganl
    private void suscribirse(){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }


}