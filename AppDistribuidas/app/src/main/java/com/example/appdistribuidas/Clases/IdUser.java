package com.example.appdistribuidas.Clases;

import android.content.Context;
import android.content.SharedPreferences;

public class IdUser {

    String USERID = "userid";
    String USER_LOGID = "usuarioLogID";

    SharedPreferences userID;


    public IdUser(Context ctx){
        userID=  ctx.getSharedPreferences(USERID, Context.MODE_PRIVATE);
    }

    public void  saveID(String estado){
        SharedPreferences.Editor  editor =  userID.edit();
        editor.putString(USER_LOGID, estado);
        editor.commit();
    }

    public String returnID(){
        return userID.getString(USER_LOGID,"");
    }

}
