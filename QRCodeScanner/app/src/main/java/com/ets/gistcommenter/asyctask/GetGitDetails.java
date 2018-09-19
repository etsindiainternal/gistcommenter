package com.ets.gistcommenter.asyctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ets.gistcommenter.listeners.CallBackListener;


import util.Constants;
import util.RequestHandler;

public class GetGitDetails extends AsyncTask<String,String,String>{
    private Context context;
    private String code;
    private CallBackListener listener;
    private ProgressDialog progressDialog;


    public GetGitDetails(Context context, String code, CallBackListener listener) {
        this.code=code;
        this.context=context;
        this.listener=listener;

    }

    @Override
    protected String doInBackground(String... strings) {
        String responce = null;
        try {
            RequestHandler example = new RequestHandler();
            responce = example.getData(Constants.BASE_URL+code);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responce;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(Constants.LOADING_GIST_DETAILS);
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

        listener.getdata(s);
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
}
