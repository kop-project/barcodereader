package com.example.barcodereader;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.barcodereader.camera.PreviewCallback;
import com.example.barcodereader.utils.CaptureManager;
import com.example.barcodereader.utils.DecoratedBarcodeView;
import com.example.barcodereader.utils.SourceData;
import com.example.barcodereader.utils.ViewfinderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class CustomScannerActivity extends Activity {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ViewfinderView viewfinderView;
    private TextView textView;
    private Bundle bundle;

    public void setTextView(String text) {
        this.textView.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        textView = findViewById(R.id.result_text);

        viewfinderView = (ViewfinderView) findViewById(R.id.zxing_viewfinder_view);

        bundle = savedInstanceState;
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), bundle);
        changeMaskColor(null);
        changeLaserVisibility(true);
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
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    public void changeMaskColor(View view) {
        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        viewfinderView.setMaskColor(color);
    }

    public void changeLaserVisibility(boolean visible) {
        viewfinderView.setLaserVisibility(visible);
    }

   /* private void makePhoto() {
        barcodeScannerView.getBarcodeView().getCameraInstance().requestPreview(new PreviewCallback() {
            @Override
            public void onPreview(SourceData sourceData) {
                sourceData.setCropRect(new Rect(0, 0, 500, 500));
                Bitmap bmp = sourceData.getBitmap();
                File new_file = null;
                try {
                    FileOutputStream fos = null;
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "ImageDemo");
                    if (!file.exists() && !file.mkdirs()) {
                        //Toast.makeText(this, "Can't create directory to store image", Toast.LENGTH_LONG).show();
                        //return;
                        System.out.println("file not created");

                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
                    String date = simpleDateFormat.format(new Date());
                    String name = "FileName" + date + ".jpg";

                    String file_name = file.getAbsolutePath() + "/" + name;
                    new_file = new File(file_name);
                    System.out.println("new_file created");
                    fos = new FileOutputStream(new_file);

                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    System.out.println(213);
                }
            }

            @Override
            public void onPreviewError(Exception e) {
                System.out.println("ERROR");
            }

        });
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);

    }
*/

    public void scanBarcode(View view) {
        capture.decode();
    }
}
