package com.ets.gistcommenter.asyctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ets.gistcommenter.listeners.CallBackListener;

import org.json.JSONObject;

import util.Constants;
import util.RequestHandler;

public class SendCommentTask extends AsyncTask<String,String,String>{
   private Context context;
   private String uname;
   private String upass;
   private String comment;
   private ProgressDialog progressDialog;
   private CallBackListener listioner;
   private String code;
    public SendCommentTask(Context context, String uname, String upass, String comment, CallBackListener listioner, String code) {
          this.comment=comment;
          this.context=context;
          this.listioner=listioner;
          this.uname=uname;
          this.upass=upass;
          this.code=code;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait.");
        progressDialog.setCancelable(false);
        try {

            progressDialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listioner.getdata(s);
        try {
            if ((null != progressDialog) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        Constants.showDialog(context,Constants.UNABLE_TO_CONNECT_SERVER);
    }

    @Override
    protected String doInBackground(String... strings) {
        String getResponse = "";
        try {
        JSONObject obj = new JSONObject();
        obj.put("body", comment);
        RequestHandler example = new RequestHandler();
        getResponse = example.doPostRequest(Constants.BASE_URL +code+ Constants.END_URL, obj.toString(), uname, upass);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return getResponse;
    }
}
