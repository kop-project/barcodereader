package com.example.barcodereader;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.barcodereader.activities.AnyOrientationCaptureActivity;
import com.example.barcodereader.activities.ContinuousCaptureActivity;
import com.example.barcodereader.activities.CustomScannerActivity;
import com.example.barcodereader.activities.SmallCaptureActivity;
import com.example.barcodereader.activities.TabbedScanning;
import com.example.barcodereader.activities.ToolbarCaptureActivity;
import com.example.barcodereader.model.QRURLModel;
import com.example.barcodereader.model.QRVCardModel;
import com.example.barcodereader.utils.BarcodeEncoder;
import com.example.barcodereader.utils.CaptureActivity;
import com.example.barcodereader.utils.CaptureManager;
import com.example.barcodereader.utils.DecoratedBarcodeView;
import com.example.barcodereader.zxing.client.android.Intents;
import com.example.barcodereader.zxing.integration.android.IntentIntegrator;
import com.example.barcodereader.zxing.integration.android.IntentResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity  {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private Button button;
    private static boolean status = false;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        MainActivity.status = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barcodeScannerView = initializeContent();
        button = findViewById(R.id.switch_flashlight);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
    }

    public void sendMessage(View view) {
        capture.decode();
    }

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.zxing_capture);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("--------------------------------------------------------LOL-----------------------------------------------");
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /*

    private ZXingScannerView scannerView;
    private TextView textResult;
    private Button button;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  scannerView = findViewById(R.id.zxscan);
        textResult = findViewById(R.id.text_result);
        button = findViewById(R.id.start_scanner);*/

        /*Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();
                        scannerView.setAutoFocus(true);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must accept", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }

    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void scanMarginScanner(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(SmallCaptureActivity.class);
        integrator.initiateScan();
    }

    @Override
    public void handleResult(Result rawResult) {

    }


    public static class ScanFragment extends Fragment {
        private String toast;

        public ScanFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            displayToast();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scan, container, false);
            Button scan = view.findViewById(R.id.scan_from_fragment);
            scan.setOnClickListener(v -> scanFromFragment());
            return view;
        }

        public void scanFromFragment() {
            IntentIntegrator.forSupportFragment(this).initiateScan();
        }

        private void displayToast() {
            if(getActivity() != null && toast != null) {
                Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                toast = null;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    toast = "Cancelled from fragment";
                } else {
                    toast = "Scanned from fragment: " + result.getContents();
                }

                // At this point we may or may not have a reference to the activity
                displayToast();
            }
        }
    }


    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

   /* @Override
    public void handleResult(final Result rawResult) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    processRawResult(rawResult.getText());
                } catch (WriterException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        scannerView.resumeCameraPreview(this);
    }
*/
   /* //not-ended
    private void processRawResult(String text) throws WriterException, IOException {
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
        generateQRCode(text);
        scannerView.resumeCameraPreview(MainActivity.this);
    }

    public void generateQRCode(String data) throws WriterException, IOException {
        int width = 350;
        int height = 350;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        FileOutputStream fos = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            //Toast.makeText(this, "Can't create directory to store image", Toast.LENGTH_LONG).show();
            //return;
            System.out.println("file not created");
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "FileName" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);
        System.out.println("new_file created");
        fos = new FileOutputStream(new_file);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Toast.makeText(this, "Save success", Toast.LENGTH_LONG).show();
        fos.flush();
        fos.close();
        refreshGallery(new_file);
    }

    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private File getDisc() {
        String t = getCurrentDateAndTime();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "ImageDemo");
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
*/

}
