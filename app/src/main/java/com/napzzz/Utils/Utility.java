package com.napzzz.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by koizen12 on 8/26/15.
 */
public class Utility {
    public static boolean checkIntenetConnectivity(Context context) {
        if (context != null) {
            ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if ((localConnectivityManager.getActiveNetworkInfo() != null)
                    && (localConnectivityManager.getActiveNetworkInfo() .isAvailable())
                    && (localConnectivityManager.getActiveNetworkInfo().isConnected())) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static String getSHA1KEY(Context mContext){
        String shaKey = null;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo( "com.napzzz",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                shaKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", shaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return shaKey;

    }

    /*public static boolean Mod10Check(String creditCardNumber)

    {
        //// check whether input string is null or empty
        if(creditCardNumber == null && creditCardNumber.isEmpty()
        {
            return false;
        }

        //// 1.	Starting with the check digit double the value of every other digit
        //// 2.	If doubling of a number results in a two digits number, add up
        ///   the digits to get a single digit number. This will results in eight single digit numbers
        //// 3. Get the sum of the digits
        int sumOfDigits = creditCardNumber.Where((e) = e >= '0' && e <= '9')
        .Reverse()
            .Select((e, i) = ((int)e - 48) * (i % 2 == 0 ? 1 : 2)).Sum((e) = e / 10 + e % 10);


        //// If the final sum is divisible by 10, then the credit card number
        //   is valid. If it is not divisible by 10, the number is invalid.
        return sumOfDigits % 10 == 0;
    }*/
}
