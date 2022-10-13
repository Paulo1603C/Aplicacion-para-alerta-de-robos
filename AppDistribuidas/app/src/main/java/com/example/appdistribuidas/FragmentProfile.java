package com.example.appdistribuidas;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdistribuidas.Clases.IdUser;
import com.example.appdistribuidas.Clases.MantenerSession;
import com.example.appdistribuidas.Clases.RegistrarUsuario;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentProfile extends Fragment {

    Button cerrarSession;
    MantenerSession ms;

    TextView identi, social, represent, direc, tel;
    RegistrarUsuario ru;
    IdUser iu;

    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        cerrarSession = view.findViewById(R.id.btnSalir);
        //variables para datos
        identi = view.findViewById(R.id.tvIdentificacion);
        social = view.findViewById(R.id.tvRedSocial);
        represent = view.findViewById(R.id.tvRepresentante);
        direc = view.findViewById(R.id.tvDireccion);
        tel = view.findViewById(R.id.tvTelefono);

        //recuperar usuario logeado
        ru = new RegistrarUsuario(getContext());
        iu = new IdUser(getContext());
        /*String aux = ru.returnUser();
        Toast.makeText(getContext(),"Hey"+aux,Toast.LENGTH_SHORT).show();*/

        //metodo para cargar la info
        cargarInformacionUser("http://www.dtoapanta.somee.com/api/Nodo?cedula="+ru.returnUser());

        ms = new MantenerSession(getContext());
        cerrarSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ms.saveStatus(false);
                ru.saveUser("");
                iu.saveID("");
                Intent intent = new Intent(getContext(), LogIn.class);
                startActivity(intent);
            }
        });
        return view;
    }

    String user,pass;

    //carga de datos
    public void cargarInformacionUser(String url){
        JsonObjectRequest jr =new JsonObjectRequest( Request.Method.GET , url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), "Cargando", Toast.LENGTH_SHORT).show();
                try {
                    if ( response.length() != 0 ) {
                        identi.setText(response.getString("TipoIdentificacion").toString());
                        social.setText(response.getString("RazonSocial").toString());
                        represent.setText(response.getString("Representante").toString());
                        direc.setText(response.getString("Direccion").toString());
                        tel.setText(response.getString("Telefono").toString());
                    } else {
                        Toast.makeText(getContext(), "Usuario o contraseña no existen!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Usuario o contraseña no existen!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jr);
    }
}