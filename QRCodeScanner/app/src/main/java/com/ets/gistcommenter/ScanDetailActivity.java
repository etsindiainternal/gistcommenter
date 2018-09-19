package com.ets.gistcommenter;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ets.gistcommenter.adapters.CommentAdapter;
import com.ets.gistcommenter.adapters.GistDataAdapter;
import com.ets.gistcommenter.asyctask.GetGitComments;
import com.ets.gistcommenter.asyctask.SendCommentTask;
import com.ets.gistcommenter.listeners.CallBackListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.Constants;
import util.DateTimeFormats;

public class ScanDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.txtusernameid)
    TextView txtname;
    @BindView(R.id.txtcreatedonid)
    TextView txtondate;
    @BindView(R.id.txtdiscriptionid)
    TextView txtdisc;
    @BindView(R.id.idAvtar)
    ImageView imgAvtar;
    @BindView(R.id.sendcmt)
    ImageView imgsendcmt;
    @BindView(R.id.comentlistview)
    ListView cmtlist;
    @BindView(R.id.detaillistview)
    ListView dtllist;
    @BindView(R.id.entercmtid)
    EditText edtentercmtid;

    private JSONObject mainobject;
    private String mdata, cmtdata, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_detail);
        ButterKnife.bind(this);


        mdata = getIntent().getStringExtra("maindata");
        cmtdata = getIntent().getStringExtra("commentdata");
        code = getIntent().getStringExtra("code");

        try {
            mainobject = new JSONObject(mdata);
            JSONObject subObjOwnname = mainobject.getJSONObject("owner");
            txtname.setText(subObjOwnname.getString("login"));
            txtondate.setText("Last activity " + new DateTimeFormats().isCommentedAgo(mainobject.getString("updated_at")).toString());
            txtdisc.setText(mainobject.getString("description").toString());
            Picasso.with(this).load(subObjOwnname.getString("avatar_url")).into(imgAvtar);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setGitDetails(mdata);
        setCommentAdapter(cmtdata);

        imgsendcmt.setOnClickListener(this);

    }

    private void setGitDetails(String mdata) {

        try {
            JSONObject mainobj = new JSONObject(mdata);
            JSONObject objchild = mainobj.getJSONObject("files");
            JSONArray gistdata = new JSONArray();

            Iterator<?> keys = objchild.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (objchild.get(key) instanceof JSONObject) {
                    JSONObject parseobject = new JSONObject(objchild.get(key).toString());
                    JSONObject obj = new JSONObject();
                    obj.put("filename", parseobject.getString("filename"));
                    obj.put("content", parseobject.getString("content"));
                    gistdata.put(obj);
                }
            }
            //Adapter for gist information
            GistDataAdapter adapter = new GistDataAdapter(this, gistdata);
            dtllist.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setCommentAdapter(String cmtdata1) {

        try {
            //Adapter for gist comments
            JSONArray commentaray = new JSONArray(cmtdata1);
            CommentAdapter adapter = new CommentAdapter(this, commentaray);
            cmtlist.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendcmt:
                String cmt = edtentercmtid.getText().toString().trim();
                if (validate(cmt)) {
                    if (Constants.isNetworkConnected(this)) {
                        authonticationDialog(cmt);
                    } else {
                        Constants.showDialog(this, Constants.NO_INTERNET);
                    }

                }

                break;
        }

    }

    //user authentication dialog
    private void authonticationDialog(String cmt) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.authontication_dialog);
        final TextView textuserid = (TextView) dialog.findViewById(R.id.userid);
        final TextView textuserpass = (TextView) dialog.findViewById(R.id.userpass);
        Button dialogButton = (Button) dialog.findViewById(R.id.sendcmtid);
        ImageView dialogButtonCancel = (ImageView) dialog.findViewById(R.id.cancelcmt);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = textuserid.getText().toString().trim();
                String upass = textuserpass.getText().toString().trim();
                String comment = edtentercmtid.getText().toString().trim();
                if (userValidation(uname, upass)) {
                    sendComment(uname, upass, comment, dialog);
                } else {
                    Constants.showDialog(ScanDetailActivity.this, Constants.ENTER_VALID_DATA);
                }
            }
        });

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtentercmtid.getText().clear();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void sendComment(String uname, String upass, String comment, final Dialog dialog) {
        CallBackListener listioner = new CallBackListener() {
            @Override
            public void getdata(String data) {
                try {
                    JSONObject obj = new JSONObject(data);
                    if (obj.has("message")) {
                        Constants.showDialog(ScanDetailActivity.this, Constants.INVALID_CERDENTIALS);
                    } else {
                        getCommentData(code);
                        edtentercmtid.getText().clear();
                        dialog.dismiss();
                        Constants.showToast(ScanDetailActivity.this, Constants.COMMENT_ADD_SUCCESSFULLY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        if (Constants.isNetworkConnected(this)) {
            SendCommentTask task = new SendCommentTask(this, uname, upass, comment, listioner, code);
            task.execute();
        } else {
            Constants.showDialog(this, Constants.NO_INTERNET);
        }

    }


    private boolean userValidation(String uname, String upass) {
        boolean result = false;
        if (uname.length() > 0) {
            result = true;
        }
        if (upass.length() > 0) {
            result = true;
        }

        return result;
    }

    private boolean validate(String cmt) {
        boolean result = false;
        if (!cmt.isEmpty() && cmt.length() > 0) {
            result = true;
        } else {
            edtentercmtid.setError(Constants.CMT_ERR);
            result = false;
        }
        return result;
    }


    private void getCommentData(final String code) {
        CallBackListener listener = new CallBackListener() {
            @Override
            public void getdata(String comments) {
                setCommentAdapter(comments);
            }
        };

        if (Constants.isNetworkConnected(this)) {
            GetGitComments task = new GetGitComments(this, code, listener);
            task.execute();
        } else {
            Constants.showDialog(this, Constants.NO_INTERNET);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.backicon) {
            startActivity(new Intent(ScanDetailActivity.this, QRScannerActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ScanDetailActivity.this, QRScannerActivity.class));
        finish();
    }
}
