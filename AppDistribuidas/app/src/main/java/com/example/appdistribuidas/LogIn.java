package com.example.appdistribuidas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdistribuidas.Clases.IdUser;
import com.example.appdistribuidas.Clases.MantenerSession;
import com.example.appdistribuidas.Clases.RegistrarUsuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class LogIn extends AppCompatActivity {

    EditText campoUser, campoPass;
    CheckBox iniciado;
    Button btnIngresar;

    MantenerSession ms;
    RegistrarUsuario ru;
    IdUser iduser;

    String userVal, pass;
    String user;
    String userID;
    int aux3=0;

    RequestQueue queue;

    private boolean aux;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        campoUser = (EditText)findViewById(R.id.txtUser);
        campoPass = (EditText)findViewById(R.id.txtPass);

        btnIngresar = findViewById(R.id.btnLogIn);
        iniciado = findViewById(R.id.btnMantenerSession);

        //SharedPreferences

        //Mcode para mantener session iniciada
        ms = new MantenerSession( this );
        ru = new RegistrarUsuario(this);
        iduser = new IdUser(this);
        verificarSession();

        //accion boton
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ms.saveStatus(iniciado.isChecked());
                iniciar();
            }
        });
    }

    private void LogIn(String url){

       JsonObjectRequest jr =new JsonObjectRequest( Request.Method.GET , url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Cargando", Toast.LENGTH_SHORT).show();
                try {
                    if ( response.length() != 0 ) {
                        userID = response.getString("Id").toString();
                        user = response.getString("NumeroIdentificacion").toString();
                        ru.saveUser(user);
                        iduser.saveID(userID);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("usuario",user);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña no existen!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "Usuario o contraseña no existen!", Toast.LENGTH_SHORT).show();
        }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jr);
    }

    private void iniciar(){
        userVal = campoUser.getText().toString().trim();
        pass = campoPass.getText().toString().trim();
        if( userVal.isEmpty() || pass.isEmpty() ){
            Toast.makeText(getApplicationContext(), "Complete los campos", Toast.LENGTH_SHORT).show();
        }else{
            //logIn("http://www.dtoapanta.somee.com/api/Loggin?user="+userVal+"&pass="+pass);
            LogIn("http://www.dtoapanta.somee.com/api/Loggin?user="+userVal+"&pass="+pass);
        }
    }

    private void verificarSession(){
        if( ms.returnStatus() == true ){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("usuario",user);
            startActivity(i);
        }
    }

}