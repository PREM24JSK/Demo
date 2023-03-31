package prem.world.demo;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class PrefManger
{
    public static final String IsLogin="Login";

    public static final String Uid="uid";
    static final String Email="email";
    public static final String Password="password";
    private final SharedPreferences sharedPreferences;

    public PrefManger(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences("Demo", Context.MODE_PRIVATE);
    }

    public Boolean getBoolean(String key) {
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, false);
        } else {
            return false;
        }
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getString(key, null);
        } else {
            return null;
        }
    }


}
