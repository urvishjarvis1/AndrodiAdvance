package com.example.urvish.fingerprintauthenticationdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private FingerprintManager mFingerprintManager;
    private KeyStore mKeyStore;
    private static final String KEY_NAME="demo";
    private Cipher cipher;
    private TextView textView;
    private KeyguardManager keyguardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        mFingerprintManager=(FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
        textView=(TextView)findViewById(R.id.errorText);

        if(!mFingerprintManager.isHardwareDetected()){
            textView.setText("Your device doesn't have fingerprint!!");
            textView.setTextColor(Color.RED);
        }else {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
                textView.setTextColor(Color.RED);
                textView.setText("Please give permission");
                String[] permissions=new String[]{Manifest.permission.USE_FINGERPRINT};
                requestPermissions(permissions,7);

            }else {
                if(!mFingerprintManager.hasEnrolledFingerprints()){
                    textView.setText("Register at least one fingerprint!");
                }else{
                    if(!keyguardManager.isDeviceSecure())
                        textView.setText("Please setup device lock");
                    else{
                        generatekey();
                        if(cipherInit()){
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(this);
                            helper.startAuth(mFingerprintManager, cryptoObject);

                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean cipherInit() {
        try{
            cipher=Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try{
            mKeyStore.load(null);
            SecretKey secretKey=(SecretKey)mKeyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            return true;
        } catch (CertificateException | UnrecoverableKeyException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void generatekey() {
        try{
            mKeyStore=KeyStore.getInstance("AndroidKeyStore");

        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        KeyGenerator keyGenerator = null;
        try{
            keyGenerator=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        try{
            mKeyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setUserAuthenticationRequired(true).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keyGenerator.generateKey();
        } catch (CertificateException | InvalidAlgorithmParameterException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
