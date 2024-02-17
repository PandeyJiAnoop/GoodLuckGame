package com.akp.aonestar.PlayBuyModule.NetworkManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;


public class DialogManager {

    public static void openCheckInternetDialog(Activity activity) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("No Internet Connection");
        alertDialogBuilder.setMessage("You are offline please check your internet connection");
        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show();
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (!InternetConnection.checkConnection(activity)) {
                    DialogManager.openCheckInternetDialog(activity);
                }
                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
