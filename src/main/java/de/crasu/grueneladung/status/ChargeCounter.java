package de.crasu.grueneladung.status;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ChargeCounter {
    public static final String PREFS_NAME = "GruneLadungPrefs";
    public static final String CHARGE_COUNT = "chargeCount";

    private SharedPreferences preferences;


    public ChargeCounter(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, 0);
        Editor editor = preferences.edit();
        editor.commit();
    }

    public void count() {
        long count = preferences.getLong(CHARGE_COUNT, 0);
        Editor editor = preferences.edit();
        editor.putLong(CHARGE_COUNT, count + 1);
        editor.commit();
    }

    public void reset() {
        Editor editor = preferences.edit();
        editor.putLong(CHARGE_COUNT, 0);
        editor.commit();
    }

    public Long getCount() {
        return preferences.getLong(CHARGE_COUNT, 0);
    }
}
