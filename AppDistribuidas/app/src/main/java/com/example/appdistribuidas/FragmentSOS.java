package com.example.appdistribuidas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdistribuidas.Clases.IdUser;
import com.example.appdistribuidas.Clases.RegistrarUsuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentSOS extends Fragment {

    ImageButton btnAux;
    double latitude;
    double longitud;
    RequestQueue queue;

    IdUser iduser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ubicacion();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_o_s, container, false);
        btnAux = view.findViewById(R.id.btnHelp);

        iduser = new IdUser(getContext());

        getLocalizacion();
        SolicitarAyuda();
        ubicacion();
        return view;
    }


    public void SolicitarAyuda() {
        btnAux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubicacion();
                crearNotificacioLanzar();
                enviarInformacionBD();
                incidencia();
            }
        });
    }

    public void crearNotificacioLanzar() {
        String url = "https://onesignal.com/api/v1/notifications";
        JSONObject jsonBody;
        try {
            jsonBody = new JSONObject("{\"app_id\":\"f9e64775-8e88-4f6d-9cfd-3db77223ace4\",\"headings\": {\"en\":\"SOLICITUD DE AUXILIO\"},\"included_segments\": [\"Subscribed Users\"],\"contents\": {\"en\":\"I need your help...\"},\"data\":{\"latitud\": \""+latitude+"\",\"altitud\": \""+longitud+"\"}}");
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //now handle the response
                    Toast.makeText(getContext(), "Notification successfully sent", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //handle the error
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Basic MzhiODZmNDItM2E1MC00MDEwLWI2NDAtZjdiNjI1Zjk2YmRj");
                    params.put("Content-type", "application/json");
                    return params;
                }
            };
            // Add the request to the queue
            Volley.newRequestQueue(getContext()).add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //enviar datos ubicacion a la base
    public void enviarInformacionBD() {
        //Salidas de prueba
        //Toast.makeText(getContext(),String.valueOf(latitude),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),String.valueOf(longitud),Toast.LENGTH_SHORT).show();
        String sms = "Help me";
        String idUser = iduser.returnID();
        String URL = "http://www.diegomauri.somee.com/api/Notificaciones/"+idUser+"/"+sms+"/"+latitude+"/"+longitud;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), "Insertado", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdUsuario", "1");
                parametros.put("Mensaje", "pm");
                parametros.put("Latitud", "-151");
                parametros.put("Longitud", "8921245");

                return parametros;
            }
        };*/
        queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    public void incidencia() {
        String sms = "Houston tenemos problemas!!!";
        String idUser = iduser.returnID();
        String URL = "http://www.dtoapanta.somee.com/api/Incidencia/"+idUser+"?mensaje="+sms+"&lat="+latitude+"&lng="+longitud;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), "Insertado en BD", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Id", "0");
                parametros.put("IdUsuario", "2");
                parametros.put("Nodo", "");
                parametros.put("Fecha", "2022-07-26T07:46:28.6878163-05:00");
                parametros.put("Mensaje", "Se presento un error");
                parametros.put("Estado", "CREADO");
                parametros.put("Longitud", "8921245");
                parametros.put("Latitud", "-151");

                return parametros;
            }
        };*/
        queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }



    public void ubicacion() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitud = location.getLongitude();
                LatLng miUbicacion = new LatLng(latitude, longitud );
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {

            }
            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
    }

