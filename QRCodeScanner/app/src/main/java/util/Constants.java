package util;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.Toast;

import com.ets.gistcommenter.QRScannerActivity;
import com.ets.gistcommenter.ScanDetailActivity;

import java.io.UnsupportedEncodingException;

public class Constants {



    //Api end points
    public static String BASE_URL = "https://api.github.com/gists/";
    public static String END_URL = "/comments";

    //Constant variables
    public static final String UNABLE_TO_CONNECT_SERVER = "Unable to connect server. Try after some time.";
    public static final String INVALID_CERDENTIALS = "Invalid username or password. Please try with valid credentials";
    public static final String COMMENT_ADD_SUCCESSFULLY = "Your comment is successfully added to gist.";
    public static final CharSequence CMT_ERR = "Your comment can't be blank";
    public static String INVALID_QR_CODE_1 = "This QR code wants to take you to ";
    public static String INVALID_QR_CODE_2 = "This is an external site and Gist Commenter has no control on its content.";
    public static final String NO_INTERNET = "Please check your internet connection";
    public static final String ENTER_VALID_DATA = "Please enter valid details";
    public static final CharSequence LOADING_GIST_DETAILS = "Fetching latest gist information.";
    public static final String BACK_PRESS_MSG = "Press back once again to exit from app";
    public static final String JUST_NOW = "Just Now";
    public static final String MINUTE_AGO = " minutes ago";
    public static final String HOURS_AGO =" hours ago" ;
    public static final String DAY_AGO = " days ago";
    public static final String VOICE_FILENAME = "us-eng_final.mp3";
    public static final String QR_SYNTAX = "gid-gist:";
    public static final String DEV_AUTH_USERNAME = "etsindiainternal";
    public static final String DEV_AUTH_PASS = "Rmxhd2xlc3NAMTIz";

    //generic method to handle alert popup message
    public static void showDialog(Context context, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setMessage(s);
        AlertDialog alert1 = builder.create();
        alert1.show();

    }

    //generic method to check internet connectivity
    public static boolean isNetworkConnected(Context contxt) {
        ConnectivityManager cm = (ConnectivityManager) contxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    //generic method to handle toast messages
    public static void showToast(Context context, String commetAddScuessfully) {
        Toast.makeText(context, commetAddScuessfully, Toast.LENGTH_LONG).show();
    }


    public String decodePass(String passString){
        String pass = null;

        byte[] data = Base64.decode(passString, Base64.DEFAULT);
        try {
            pass = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return pass;
    }

}

