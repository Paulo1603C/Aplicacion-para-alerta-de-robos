package com.example.appdistribuidas.Clases;

import android.content.Context;
import android.content.SharedPreferences;

public class RegistrarUsuario {

    String USER = "user";
    String USER_LOG = "usuariosLog";

    SharedPreferences usuarios;

    public RegistrarUsuario(Context contexto){
        usuarios =  contexto.getSharedPreferences(USER_LOG, Context.MODE_PRIVATE);
    }

    public void  saveUser(String usu){
        SharedPreferences.Editor  editor1 = usuarios.edit();
        editor1.putString(USER, usu);
        editor1.commit();
    }

    public String returnUser(){
        return usuarios.getString(USER,"");
    }

}
