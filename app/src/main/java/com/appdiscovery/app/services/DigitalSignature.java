package com.appdiscovery.app.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.appdiscovery.app.R;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class DigitalSignature {
    private static final int KEY_LENGTH = 2048;
    private static final String charsetName = "UTF8";
    private static final String pkAlgorithm = "RSA";
    private static final String signAlgorithm = "SHA1withRSA";

    private static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        return KeyFactory.getInstance(pkAlgorithm).generatePrivate(
                new PKCS8EncodedKeySpec(Base64.decode(key64, Base64.DEFAULT))
        );
    }

    private static PublicKey loadPublicKey(String key64) throws GeneralSecurityException {
        return KeyFactory.getInstance(pkAlgorithm).generatePublic(
                new X509EncodedKeySpec(Base64.decode(key64, Base64.DEFAULT))
        );
    }

    private static String sign(String text, String strPrivateKey) {
        try {
            PrivateKey pk = loadPrivateKey(strPrivateKey);
            byte[] data = text.getBytes(charsetName);
            Signature sig = Signature.getInstance(signAlgorithm);
            sig.initSign(pk);
            sig.update(data);
            byte[] signatureBytes = sig.sign();
            return Base64.encodeToString(signatureBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean verfiySignature(String signature, String original, String publicKey) {
        try {
            Signature sig = Signature.getInstance(signAlgorithm);
            sig.initVerify(loadPublicKey(publicKey));
            sig.update(original.getBytes(charsetName));
            return sig.verify(Base64.decode(signature, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void init(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String publicKey = sharedPref.getString(context.getString(R.string.public_key), "");
        String privateKey = sharedPref.getString(context.getString(R.string.private_key), "");
        if (publicKey.equals("") || privateKey.equals("")) {
            try {
                KeyPairGenerator keygen = KeyPairGenerator.getInstance(pkAlgorithm);
                keygen.initialize(KEY_LENGTH);
                KeyPair keyPair = keygen.generateKeyPair();

                privateKey = Base64.encodeToString(keyPair.getPrivate().getEncoded(), Base64.DEFAULT);
                publicKey = Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.DEFAULT);

//                String signature = sign("aaa", privateKey);
//                Boolean verified = verfiySignature(signature, "aaab", publicKey);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
}
