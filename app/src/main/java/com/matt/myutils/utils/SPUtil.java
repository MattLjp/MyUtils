package com.matt.myutils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author matt.Ljp
 * @time 2020/2/27 13:49
 * @description SharedPreferences保存工具类
 */
public class SPUtil {
    public SPUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object == null) {
            return;
        } else if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ((Bitmap) object).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String headPicBase64 = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            editor.putString(key, headPicBase64);
        } else if (object instanceof Serializable) {
            // obj必须实现Serializable接口，否则会出问题
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                String string64 = new String(Base64.encode(baos.toByteArray(), 0));
                editor.putString(key, string64);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Bitmap) {
            String headPic = sp.getString(key, "");
            if (!"".equals(headPic)) {
                byte[] bytes = Base64.decode(headPic.getBytes(), 1);
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bitmap;
            } else {
                return defaultObject;
            }
        } else if (defaultObject instanceof Serializable) {
            Object obj = null;
            try {
                String base64 = sp.getString(key, "");
                if ("".equals(base64)) {
                    return defaultObject;
                }
                byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
                ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                obj = ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;

        }
        return null;
    }

    /**
     * 使用SharedPreferences保存List
     * 支持类型：List<String>，List<JavaBean>
     *
     * @param context  上下文
     * @param key      储存的key
     * @param dataList 存储数据
     * @param <T>      泛型
     */
    public static <T> void setDataList(Context context, String key, List<T> dataList) {
        if (null == dataList || dataList.size() < 0) {
            return;
        }

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(dataList);
        editor.putString(key, strJson);
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * 获取SharedPreferences保存的List
     *
     * @param context 上下文
     * @param key     储存的key
     * @param <T>     泛型
     * @return 存储List<T>数据
     */
    public static <T> List<T> getDataList(Context context, String key, Class<T> cls) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        List<T> dataList = new ArrayList<T>();
        String strJson = sp.getString(key, null);
        if (null == strJson) {
            return dataList;
        }
        Gson gson = new Gson();

        JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
        for (JsonElement jsonElement : arry) {
            dataList.add(gson.fromJson(jsonElement, cls));
        }
        return dataList;
    }


    /**
     * 存储Map集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public static <K, V> void putHashMapData(Context context, String key, Map<K, V> map) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取Map集合
     *
     * @param key key
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(Context context, String key, Class<V> clsV) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        String json = sp.getString(key, "");
        if ("".equals(json)) {
            return null;
        } else {
            HashMap<String, V> map = new HashMap<>();
            Gson gson = new Gson();
            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                String entryKey = entry.getKey();
                JsonObject value = (JsonObject) entry.getValue();
                map.put(entryKey, gson.fromJson(value, clsV));
            }
            return map;
        }
    }

    /**
     * 存储List<Map<String, String>>格式
     *
     * @param context
     * @param key
     * @param datas
     */
    public static void saveInfo(Context context, String key, List<Map<String, String>> datas) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        JSONArray mJsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> itemMap = datas.get(i);
            Iterator<Map.Entry<String, String>> iterator = itemMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        mJsonArray.put(object);
        editor.putString(key, mJsonArray.toString());
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 读取List<Map<String, String>>格式
     *
     * @param context
     * @param key
     * @return
     */
    public static List<Map<String, String>> getInfo(Context context, String key) {
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        String result = sp.getString(key, "");

        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                Map<String, String> itemMap = new HashMap<String, String>();
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = itemObject.getString(name);
                        itemMap.put(name, value);
                    }
                }
                datas.add(itemMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return datas;
    }


    /**
     * 移除某个 key 值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个 key 是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决 SharedPreferencesCompat.apply 方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method S_APPLY_METHOD = findApplyMethod();

        /**
         * 反射查找 apply 的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException ignored) {
            }

            return null;
        }

        /**
         * 如果找到则使用 apply 执行，否则使用 commit
         *
         * @param editor
         */
        private static void apply(SharedPreferences.Editor editor) {
            try {
                if (S_APPLY_METHOD != null) {
                    S_APPLY_METHOD.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) {
            }
            editor.commit();
        }
    }


}
