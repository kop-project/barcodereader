package com.example.barcodereader;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodereader.model.QRURLModel;
import com.example.barcodereader.model.QRVCardModel;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    private ZXingScannerView scannerView;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerView = findViewById(R.id.zxscan);
        textResult = findViewById(R.id.text_result);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must accept", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        processRawResult(rawResult.getText());

    }

    //not-ended
    private void processRawResult(String text) {
        if (text.startsWith("BEGIN:")) {
            String[] tokens = text.split("\n");
            QRVCardModel qrvCardModel = new QRVCardModel();
            for (String token : tokens) {
                if (token.startsWith("BEGIN:")) {
                    qrvCardModel.setType(token.substring("BEGIN:".length()));
                }
                if (token.startsWith("N:")) {
                    qrvCardModel.setName(token.substring("BEGIN:".length()));
                }
                if (token.startsWith("ORG:")) {
                    qrvCardModel.setOrg(token.substring("ORG:".length()));
                }
                if (token.startsWith("TEL:")) {
                    qrvCardModel.setTel(token.substring("TEL:".length()));
                }
                if (token.startsWith("URL:")) {
                    qrvCardModel.setUrl(token.substring("URL:".length()));
                }
                if (token.startsWith("EMAIL:")) {
                    qrvCardModel.setEmail(token.substring("EMAIL:".length()));
                }
                if (token.startsWith("ADR:")) {
                    qrvCardModel.setAddress(token.substring("ADR:".length()));
                }
                if (token.startsWith("NOTE:")) {
                    qrvCardModel.setNote(token.substring("NOTE:".length()));
                }
                if (token.startsWith("SUMMARY:")) {
                    qrvCardModel.setUrl(token.substring("SUMMARY:".length()));
                }
                if (token.startsWith("DTSTART:")) {
                    qrvCardModel.setDtStart(token.substring("DTSTART:".length()));
                }
                if (token.startsWith("DTEND:")) {
                    qrvCardModel.setDtEnd(token.substring("DTEND:".length()));
                }
                textResult.setText(qrvCardModel.getType());
            }
        } else {
            if (text.startsWith("http://") || text.startsWith("https://") || text.startsWith("www.")) {
                QRURLModel qrurlModel = new QRURLModel(text);
                textResult.setText(qrurlModel.getUrl());
            } else {
                textResult.setText(text);
            }
        }
        scannerView.resumeCameraPreview(MainActivity.this);
    }
}
