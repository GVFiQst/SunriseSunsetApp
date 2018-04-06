package gv_fiqst.teamvoytestsunrise.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Simple util class for easing the use of shared preference.
 * <p>
 * Created by GV_FiQst on 19.12.2017.
 *
 * Version 1.1 (27.03.2018) (Added {@link ChangeListener})
 */
public class SharedPrefUtil implements SharedPreferences.OnSharedPreferenceChangeListener {
    /**
     * Name of the preference file
     */
    private static final String APP_PREFS = "application_preferences";

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private ChangeListener mListener;

    public SharedPrefUtil(Context context) {
        mContext = context;
    }

    public SharedPrefUtil setListener(ChangeListener listener) {
        ensurePrefs();
        mListener = listener;
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return this;
    }

    /**
     * Save a string into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public SharedPrefUtil saveString(String key, String value) {
        ensureEditor();

        mEditor.putString(key, value);
        return this;
    }

    /**
     * Save a int into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public SharedPrefUtil saveInt(String key, int value) {
        ensureEditor();

        mEditor.putInt(key, value);
        return this;
    }

    /**
     * Save a long into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public SharedPrefUtil saveLong(String key, long value) {
        ensureEditor();

        mEditor.putLong(key, value);
        return this;
    }

    /**
     * Save a boolean into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public SharedPrefUtil saveBoolean(String key, boolean value) {
        ensureEditor();

        mEditor.putBoolean(key, value);
        return this;
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or null.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    public String getString(String key, String defaultValue) {
        ensurePrefs();

        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public int getInt(String key, int defaultValue) {
        ensurePrefs();

        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * Throws ClassCastException if there is a preference with this name that is not a long.
     */
    public long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public long getLong(String key, long defaultValue) {
        ensurePrefs();

        return mSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or false.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        ensurePrefs();

        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Clears the shared preference file
     */
    public SharedPrefUtil clear() {
        ensureEditor();

        mEditor.clear();
        return this;
    }

    private void ensurePrefs() {
        if (mSharedPreferences == null) {
            mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void ensureEditor() {
        ensurePrefs();

        if (mEditor == null) {
            mEditor = mSharedPreferences.edit();
        }
    }

    public void apply() {
        ensureEditor();

        mEditor.apply();
        mEditor = null;
    }

    public void commit() {
        ensureEditor();

        mEditor.commit();
        mEditor = null;
    }

    public void release() {
        if (mSharedPreferences != null) {
            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }

        mContext = null;
        mSharedPreferences = null;
        mEditor = null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mListener != null) {
            mListener.onChange(key);
        }
    }

    public interface ChangeListener {
        void onChange(String key);
    }
}
