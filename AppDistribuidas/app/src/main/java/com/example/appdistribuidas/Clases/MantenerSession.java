package com.example.appdistribuidas.Clases;

import android.content.Context;
import android.content.SharedPreferences;

public class MantenerSession {

    //Guardaar session
    String USER_APP = "usuarios";
    String STATUS = "userStatus";

    String USER = "user";
    String USER_LOG = "usuariosLog";

    SharedPreferences almacenamiento, usuarios;

    public MantenerSession() {
    }

    public MantenerSession(Context ctx){
        almacenamiento =  ctx.getSharedPreferences(USER_APP, Context.MODE_PRIVATE);
        usuarios =  ctx.getSharedPreferences(USER_LOG, Context.MODE_PRIVATE);
    }

    public void  saveStatus(boolean estado){
        SharedPreferences.Editor  editor =  almacenamiento.edit();
        editor.putBoolean(STATUS, estado);
        editor.commit();
    }

    public boolean returnStatus(){
        return almacenamiento.getBoolean(STATUS,false);
    }



}
