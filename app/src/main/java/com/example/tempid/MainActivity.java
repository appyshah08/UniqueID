package com.example.tempid;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaDrm;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String uniqueID =UUID.randomUUID().toString();
        Toast.makeText(this, "The message is :"+uniqueID,Toast.LENGTH_LONG).show();
    }

    public String getUniqueID(){
        UUID WIDEVINE_UUID = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
        MediaDrm wvDrm = null;
        try {
            wvDrm = new MediaDrm(WIDEVINE_UUID);
            byte[] widevineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(widevineId);

            BigInteger no = new BigInteger(1, md.digest());

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            //WIDEVINE is not available
            return null;
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                wvDrm.close();
            } else {
                wvDrm.release();
            }
        }
    }


}
