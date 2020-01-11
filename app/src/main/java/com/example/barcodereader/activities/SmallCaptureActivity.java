package com.example.barcodereader.activities;

import com.example.barcodereader.R;
import com.example.barcodereader.utils.CaptureActivity;
import com.example.barcodereader.utils.DecoratedBarcodeView;

/**
 * This activity has a margin.
 */
public class SmallCaptureActivity extends CaptureActivity {
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.capture_small);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
