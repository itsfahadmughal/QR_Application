package com.example.qr_application;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Button b1,b2;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b1=findViewById(R.id.button2);
        b2=findViewById(R.id.button3);
        fingerprintManager= (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        keyguardManager= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Authentication_fingerprint.class);
                startActivity(intent);
                finish();
            }
        });// direct login button...

        final MainActivity activity=this;
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFingerPrint();
            }
        });// fingerprint authentication button...
    }//ends of oncreate


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startFingerPrint() {
        if(checkFingerPrintSettings()){
            Toast.makeText(this, "Place Your Finger On Sensor !!!", Toast.LENGTH_SHORT).show();
            com.example.fingerprintauth.fingerPrintAuthenticator authenticator= com.example.fingerprintauth.fingerPrintAuthenticator.getInstance();
            if (authenticator.cipherInit()){
                FingerprintManager.CryptoObject cryptoObject=new FingerprintManager.CryptoObject(authenticator.getCipher());

                FingerprintHandler fingerprintHandler=new FingerprintHandler();
                fingerprintHandler.startAuthentication(cryptoObject);
            }


        }
    }//function endings


    @RequiresApi(api = Build.VERSION_CODES.M)
    private class FingerprintHandler extends FingerprintManager.AuthenticationCallback{
        CancellationSignal signal;

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Toast.makeText(MainActivity.this, "Authentication Error!!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            Toast.makeText(MainActivity.this, "Authentication Help!!!", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Toast.makeText(MainActivity.this, "Authentication Failed!!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            Toast.makeText(MainActivity.this, "Authentication Succeeded...", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MainActivity.this,Authentication_fingerprint.class));
        }

        public void startAuthentication(FingerprintManager.CryptoObject cryptoObject) {
            signal=new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
                return;
            }

            fingerprintManager.authenticate(cryptoObject,signal,0,this,null);
        }

        void cancelFingerprint(){
            signal.cancel();
        }
    }//end of class fingerprint handler

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean checkFingerPrintSettings() {
        if (fingerprintManager.isHardwareDetected()){
            if (fingerprintManager.hasEnrolledFingerprints()){
                if (keyguardManager.isKeyguardSecure()){
                    return true;
                }
            }else {
                Toast.makeText(this, "Enroll FingerPrint!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
            }
        }
        return false;
    }//function endings


}
