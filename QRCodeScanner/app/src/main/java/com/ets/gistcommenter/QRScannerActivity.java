package com.ets.gistcommenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ets.gistcommenter.asyctask.GetGitComments;
import com.ets.gistcommenter.asyctask.GetGitDetails;
import com.ets.gistcommenter.listeners.CallBackListener;
import com.google.zxing.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import util.Constants;

import static android.Manifest.permission.CAMERA;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    @BindView(R.id.zxscan)
    ZXingScannerView mScannerView;
    @BindView(R.id.btn_flash)
    ImageView flashBtn;
    @BindView(R.id.isonoroff)
    TextView txtFlashOnOff;


    private final int REQUEST_CAMERA = 1;
    private int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean flag = false;
    private int TIME_DELAY = 3000;
    private long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scaner);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
            } else {
                requestPermission();
            }
        }
        ButterKnife.bind(this);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            flashBtn.setVisibility(View.GONE);
            txtFlashOnOff.setVisibility(View.GONE);
        }
        flashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag) {
                    mScannerView.setFlash(false);
                    flag = false;
                    txtFlashOnOff.setText("Off");
                    flashBtn.setImageResource(R.mipmap.flashoff);

                } else {
                    mScannerView.setFlash(true);
                    flag = true;
                    txtFlashOnOff.setText("On");
                    flashBtn.setImageResource(R.mipmap.flashon);

                }

            }
        });


    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        //Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QRScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag) {
            mScannerView.setFlash(false);
            flag = false;
            txtFlashOnOff.setText("Off");
            flashBtn.setImageResource(R.mipmap.flashoff);
        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (mScannerView == null) {
                    setContentView(R.layout.activity_qr_code_scaner);
                    mScannerView = (ZXingScannerView) findViewById(R.id.zxscan);

                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera(camId);
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mScannerView.stopCamera();
            mScannerView = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleResult(Result rawResult) {
        final String result = rawResult.getText();
        Log.e("QRCodeScanner", rawResult.getText());
        Log.e("QRCodeScanner", rawResult.getBarcodeFormat().toString());

        String resultString = null;


        if (result.startsWith(Constants.QR_SYNTAX)) {
            String[] arrOfStr = result.split(Constants.QR_SYNTAX, 0);

            for (String a : arrOfStr)
                resultString = a;
            if (Constants.isNetworkConnected(this)) {
                loadData(resultString.trim());
            } else {
                Constants.showDialog(this, Constants.NO_INTERNET);
            }

        } else {
            showDialog(Constants.INVALID_QR_CODE_1 + result + ". " + Constants.INVALID_QR_CODE_2, result);

        }


    }

    //method to handle gist API  response data
    private void loadData(final String code) {

        CallBackListener listener = new CallBackListener() {

            @Override
            public void getdata(String data) {
                getCommentData(data, code);
            }
        };

        GetGitDetails task = new GetGitDetails(this, code, listener);
        task.execute();
    }

    //method to handle gist API comments response sata
    private void getCommentData(final String data, final String code) {
        CallBackListener listener = new CallBackListener() {
            @Override
            public void getdata(String comments) {

                Intent i = new Intent(getApplicationContext(), ScanDetailActivity.class);
                i.putExtra("maindata", data);
                i.putExtra("commentdata", comments);
                i.putExtra("code", code);
                startActivity(i);
                finish();
            }
        };

        GetGitComments task = new GetGitComments(this, code, listener);
        task.execute();

    }

    //invalid saning alert dialog
    private void showDialog(String s, final String url) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.invalid_qr_dilog);
        TextView msgtxt = (TextView) dialog.findViewById(R.id.qr_errmsg);
        msgtxt.setText(s);
        Button dialogButton = (Button) dialog.findViewById(R.id.idbtnok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView.resumeCameraPreview(QRScannerActivity.this);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                dialog.dismiss();

            }
        });
        Button idbtncancel = (Button) dialog.findViewById(R.id.idbtncancel);
        idbtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView.resumeCameraPreview(QRScannerActivity.this);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            exitApp();
        } else {
            Constants.showToast(this, Constants.BACK_PRESS_MSG);
        }
        back_pressed = System.currentTimeMillis();
    }

    private void exitApp() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


}