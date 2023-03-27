package com.filter.scene.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : kevinbai
 * date      : 2021/3/29 下午1:43
 */
public class FilterSharePreferenceUtil {

        private FilterSharePreferenceUtil() {
            throw new IllegalStateException("ExPreferenceUtil class cannot be instantiated");
        }

        /**
         * The name for default sharedPreferences file name.
         */
        private static String spFileName = "FILE";

        /**
         * The mode for default sharePreferences file.
         */
        private final static int SP_MODE = Context.MODE_PRIVATE;
        /**
         * The application for convenient get sharePreferences at anywhere.
         */
        private static Context application = null;
        /**
         * The tag for debug.
         */
        private final static String TAG = "FILE";

        public static void setSPFileName(String name) {
            spFileName = name;
        }

        public static void setApplication(Context application) {
            if (application == null) {
                return;
            }
            FilterSharePreferenceUtil.application = application;
        }

        public static void setApplication(Activity activity) {
            if (activity == null) {
                return;
            }
            FilterSharePreferenceUtil.application = activity.getApplication();
        }

        public static SharedPreferences getSP(Context application, String name) {
            if (application == null) {
                throw new RuntimeException("null was context");
            }
            return application.getSharedPreferences(name, SP_MODE);
        }

        private static boolean checkObject(Object object) {
            return (object != null);
        }

        private static boolean checkString(String key) {
            return (checkObject(key) && key.trim().length() != 0);
        }

        private static boolean checkApplicationAndString(Context application, String name) {
            return (checkObject(application) && checkString(name));
        }

        private static boolean checkApplicationAndStrings(Context application, String name, String key) {
            return (checkApplicationAndOject(application, name) && checkString(key));
        }

        private static boolean checkApplicationAndStrings(Context application, String name, String key, String value) {
            return (checkApplicationAndStrings(application, name, key) && checkString(value));
        }

        private static boolean checkApplicationAndOject(Context application, Object object) {
            return (checkObject(application) && checkObject(object));
        }

        public static String getString(Context application, String name, String key) {
            if (checkApplicationAndStrings(application, name, key)) {
                return getSP(application, name).getString(key, "").trim();
            }
            return "";
        }

        public static String getString(Context application, String key) {
            return getString(application, spFileName, key);
        }

        public static String getString(Activity activity, String key) {
            return getString(activity.getApplicationContext(), spFileName, key);
        }


        public static String getString(String name, String key) {
            return getString(application, name, key);
        }

        public static String getString(String key) {
            return getString(application, spFileName, key);
        }

        public static void pushString(Context application, String name, String key, String value) {
            if (checkApplicationAndStrings(application, name, key, value)) {
                getSP(application, name).edit().putString(key, value.trim()).apply();
            }
        }

        public static void pushString(Context application, String key, String value) {
            pushString(application, spFileName, key, value);
        }

        public static void pushString(Activity activity, String key, String value) {
            pushString(activity.getApplication(), spFileName, key, value);
        }

        public static void pushString(String name, String key, String value) {
            pushString(application, name, key, value);
        }

        public static void pushString(String key, String value) {
            pushString(application, spFileName, key, value);
        }

        public static int getInt(Context application, String name, String key) {
            if (checkApplicationAndStrings(application, name, key)) {
                return getSP(application, name).getInt(key, 0);
            }
            return 0;
        }

        public static int getInt(Context application, String key) {
            return getInt(application, spFileName, key);
        }

        public static int getInt(Activity activity, String key) {
            return getInt(activity.getApplication(), spFileName, key);
        }

        public static int getInt(String name, String key) {
            return getInt(application, name, key);
        }

        public static int getInt(String key) {
            return getInt(application, spFileName, key);
        }

        public static void pushInt(Context application, String name, String key, int value) {
            if (checkApplicationAndStrings(application, name, key)) {
                getSP(application, name).edit().putInt(key, value).apply();
            }
        }

        public static void pushInt(Context application, String key, int value) {
            pushInt(application, spFileName, key, value);
        }

        public static void pushInt(Activity activity, String key, int value) {
            pushInt(activity.getApplication(), spFileName, key, value);
        }

        public static void pushInt(String name, String key, int value) {
            pushInt(application, name, key, value);
        }

        public static void pushInt(String key, int value) {
            pushInt(application, spFileName, key, value);
        }

        public static float getFloat(Context application, String name, String key) {
            if (checkApplicationAndStrings(application, name, key)) {
                return getSP(application, name).getFloat(key, 0);
            }
            return 0;
        }

        public static float getFloat(Context application, String key) {
            return getFloat(application, spFileName, key);
        }

        public static float getFloat(Activity activity, String key) {
            return getFloat(activity.getApplication(), spFileName, key);
        }

        public static float getFloat(String name, String key) {
            return getFloat(application, name, key);
        }

        public static float getFloat(String key) {
            return getFloat(application, spFileName, key);
        }

        public static void pushFloat(Context application, String name, String key, float value) {
            if (checkApplicationAndStrings(application, name, key)) {
                getSP(application, name).edit().putFloat(key, value).apply();
            }
        }

        public static void pushFloat(Context application, String key, float value) {
            pushFloat(application, spFileName, key, value);
        }

        public static void pushFloat(Activity activity, String key, float value) {
            pushFloat(activity.getApplication(), spFileName, key, value);
        }

        public static void pushFloat(String name, String key, float value) {
            pushFloat(application, name, key, value);
        }

        public static void pushFloat(String key, float value) {
            pushFloat(application, spFileName, key, value);
        }

        public static long getLong(Context application, String name, String key) {
            if (checkApplicationAndStrings(application, name, key)) {
                return getSP(application, name).getLong(key, 0);
            }
            return 0;
        }

        public static long getLong(Context application, String key) {
            return getLong(application, spFileName, key);
        }

        public static long getLong(Activity activity, String key) {
            return getLong(activity.getApplication(), spFileName, key);
        }

        public static long getLong(String name, String key) {
            return getLong(application, name, key);
        }

        public static long getLong(String key) {
            return getLong(application, spFileName, key);
        }

        public static void pushLong(Context application, String name, String key, long value) {
            if (checkApplicationAndStrings(application, name, key)) {
                getSP(application, name).edit().putLong(key, value).apply();
            }
        }

        public static void pushLong(Context application, String key, long value) {
            pushLong(application, spFileName, key, value);
        }

        public static void pushLong(Activity activity, String key, long value) {
            pushLong(activity.getApplication(), spFileName, key, value);
        }

        public static void pushLong(String name, String key, long value) {
            pushLong(application, name, key, value);
        }

        public static void pushLong(String key, long value) {
            pushLong(application, spFileName, key, value);
        }

        public static boolean getBoolean(Context application, String name, String key) {
            if (checkApplicationAndStrings(application, name, key)) {
                return getSP(application, name).getBoolean(key, false);
            }
            return false;
        }

        public static boolean getBoolean(Context application, String name, String key, boolean defaultValue) {
            if (checkApplicationAndStrings(application, name, key)) {
                return getSP(application, name).getBoolean(key, defaultValue);
            }
            return false;
        }

        public static boolean getBoolean(Context application, String key) {
            return getBoolean(application, spFileName, key);
        }

        public static boolean getBoolean(Activity activity, String key) {
            return getBoolean(activity.getApplication(), spFileName, key);
        }

        public static boolean getBoolean(String name, String key) {
            return getBoolean(application, name, key);
        }

        public static boolean getBoolean(String key) {
            return getBoolean(application, spFileName, key);
        }

        public static boolean getBoolean(String key, boolean defaultValue) {
            return getBoolean(application, spFileName, key, defaultValue);
        }

        public static void pushBoolean(Context application, String name, String key, boolean value) {
            if (checkApplicationAndStrings(application, name, key)) {
                getSP(application, name).edit().putBoolean(key, value).apply();
            }
        }

        public static void pushBoolean(Context application, String key, boolean value) {
            pushBoolean(application, spFileName, key, value);
        }

        public static void pushBoolean(Activity activity, String key, boolean value) {
            pushBoolean(activity.getApplication(), spFileName, key, value);
        }

        public static void pushBoolean(String name, String key, boolean value) {
            pushBoolean(application, name, key, value);
        }

        public static void pushBoolean(String key, boolean value) {
            pushBoolean(application, spFileName, key, value);
        }

        private static void pushToSP(SharedPreferences.Editor editor, String key, Object value) {
            if (value instanceof String) {
                editor.putString(key, ((String) value).trim());
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            }
        }

        public static void pushValue(Context application, String name, String key, Object value) {
            if (checkApplicationAndStrings(application, name, key)) {

                SharedPreferences.Editor editor = getSP(application, name).edit();

                pushToSP(editor, key, value);

                editor.apply();
            }
        }

        public static void pushValue(Context application, String key, Object value) {
            pushValue(application, spFileName, key, value);
        }

        public static void pushValue(Activity activity, String key, Object value) {
            pushValue(activity.getApplication(), spFileName, key, value);
        }

        public static void pushValue(String name, String key, Object value) {
            pushValue(application, name, key, value);
        }

        public static void pushValue(String key, Object value) {
            pushValue(application, spFileName, key, value);
        }

        public static void pushValues(Context application, String name, Map<String, Object> keyValues) {
            if (checkApplicationAndOject(application, keyValues)) {

                SharedPreferences.Editor editor = getSP(application, name).edit();

                for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    pushToSP(editor, key, value);
                }

                editor.apply();
            }
        }

        public static void pushValues(Context application, Map<String, Object> keyValues) {
            pushValues(application, spFileName, keyValues);
        }

        public static void pushValues(Activity activity, Map<String, Object> keyValues) {
            pushValues(activity.getApplication(), spFileName, keyValues);
        }

        public static void pushValues(String name, Map<String, Object> keyValues) {
            pushValues(application, name, keyValues);
        }

        public static void pushValues(Map<String, Object> keyValues) {
            pushValues(application, spFileName, keyValues);
        }

        private static Object getFromSP(SharedPreferences sp, String key, Object type) {
            Object value = null;

            if (type instanceof String) {
                value = sp.getString(key, "").trim();
            } else if (type instanceof Integer) {
                value = sp.getInt(key, 0);
            } else if (type instanceof Float) {
                value = sp.getFloat(key, 0);
            } else if (type instanceof Long) {
                value = sp.getLong(key, 0);
            } else if (type instanceof Boolean) {
                value = sp.getBoolean(key, false);
            }

            return value;
        }

        public static Object getValue(Context application, String name, String key, Object type) {
            if (checkApplicationAndStrings(application, name, key)) {
                SharedPreferences sp = getSP(application, name);

                return getFromSP(sp, key, type);
            }
            return null;
        }

        public static Object getValue(Context application, String key, Object type) {
            return getValue(application, spFileName, key, type);
        }

        public static Object getValue(Activity activity, String key, Object type) {
            return getValue(activity.getApplication(), spFileName, key, type);
        }

        public static Object getValue(String name, String key, Object type) {
            return getValue(application, name, key, type);
        }

        public static Object getValue(String key, Object type) {
            return getValue(application, spFileName, key, type);
        }

        public static Map<String, Object> getValues(Context application, String name, Map<String, Object> keyTypes) {
            HashMap<String, Object> keyValues = new HashMap<>();
            if (checkApplicationAndString(application, name)) {
                SharedPreferences sp = getSP(application, name);

                for (Map.Entry<String, Object> entry : keyTypes.entrySet()) {
                    String key = entry.getKey();
                    Object type = entry.getValue();

                    keyValues.put(key, getFromSP(sp, key, type));
                }

                return keyValues;
            }
            return keyValues;
        }

        public static Map<String, Object> getValues(Context application, Map<String, Object> keyTypes) {
            return getValues(application, spFileName, keyTypes);
        }

        public static Map<String, Object> getValues(Activity activity, Map<String, Object> keyTypes) {
            return getValues(activity.getApplication(), spFileName, keyTypes);
        }

        public static Map<String, Object> getValues(String name, Map<String, Object> keyTypes) {
            return getValues(application, name, keyTypes);
        }

        public static Map<String, Object> getValues(Map<String, Object> keyTypes) {
            return getValues(application, spFileName, keyTypes);
        }



}
