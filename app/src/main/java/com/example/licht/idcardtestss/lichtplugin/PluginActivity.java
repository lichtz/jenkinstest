package com.example.licht.idcardtestss.lichtplugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.licht.idcardtestss.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public class PluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        AssetManager assets = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assets.open("plugin1.apk");
            Source source = Okio.source(inputStream);
            BufferedSource buffer = Okio.buffer(source);
            Log.i("PluginActivity", getFileStreamPath("plugin1.apk") + "");
            buffer.readAll(Okio.sink(getFileStreamPath("plugin1.apk")));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        InputStream inputStream2 = null;
        try {
            inputStream2 = assets.open("plugin2.apk");
            Source source = Okio.source(inputStream2);
            BufferedSource buffer = Okio.buffer(source);
            Log.i("PluginActivity", getFileStreamPath("plugin2.apk") + "");
            buffer.readAll(Okio.sink(getFileStreamPath("plugin2.apk")));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream2 != null) {
                try {
                    inputStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        String[] uninstallApkInfo = getUninstallApkInfo(this, getFileStreamPath("plugin1.apk").getAbsolutePath());
        for (String s: uninstallApkInfo
             ) {
            Log.i("PluginActivity",s);

        }

        Resources pluginResource = getPluginResource(getFileStreamPath("plugin1.apk");
    }

//    public void addAssetPath(Context context, String apkName){
//        try {
//            AssetManager assetManager = AssetManager.class.newInstance();
//            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
//            addAssetPath.invoke(assetManager, pluginInfos.get(apkName).getDexPath());
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//    }
    //获取apk 信息

    private String[] getUninstallApkInfo(Context context,String archiveFilePath){
        String [] info = new String[2];
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (packageArchiveInfo !=null){
            ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
            int versionCode = packageArchiveInfo.versionCode;
            Drawable applicationIcon = packageManager.getApplicationIcon(applicationInfo);
          String appName =   packageManager.getApplicationLabel(applicationInfo).toString();
            String packageName = applicationInfo.packageName;
            info[0] = packageName;
            info[1] =appName;
        }
        return  info;
    }

    //获取apkresource 对象

    private Resources getPluginResource(String apk){
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
          Method addAssetPath =   assetManager.getClass().getMethod("addAssetPatg",String.class);
          addAssetPath.invoke(assetManager,apk);
            Resources resources = this.getResources();
            Resources mResource = new Resources(assetManager, resources.getDisplayMetrics(),resources.getConfiguration());
            return  mResource;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载apk获得内部资源
     * @param apkDir apk目录
     * @param apkName apk名字,带.apk
     * @throws Exception
     */
    private void dynamicLoadApk(String apkDir, String apkName, String apkPackageName) throws Exception {
        File optimizedDirectoryFile = getDir("dex", Context.MODE_PRIVATE);//在应用安装目录下创建一个名为app_dex文件夹目录,如果已经存在则不创建
        Log.v("zxy", optimizedDirectoryFile.getPath().toString());// /data/data/com.example.dynamicloadapk/app_dex
        //参数：1、包含dex的apk文件或jar文件的路径，2、apk、jar解压缩生成dex存储的目录，3、本地library库目录，一般为null，4、父ClassLoader
        DexClassLoader dexClassLoader = new DexClassLoader(apkDir+File.separator+apkName, optimizedDirectoryFile.getPath(), null, ClassLoader.getSystemClassLoader());
        Class<?> clazz = dexClassLoader.loadClass(apkPackageName + ".R$mipmap");//通过使用apk自己的类加载器，反射出R类中相应的内部类进而获取我们需要的资源id
        Field field = clazz.getDeclaredField("one");//得到名为one的这张图片字段
        int resId = field.getInt(R.id.class);//得到图片id
        Resources mResources = getPluginResources(apkName);//得到插件apk中的Resource
        if (mResources != null) {
            //通过插件apk中的Resource得到resId对应的资源
            findViewById(R.id.background).setBackgroundDrawable(mResources.getDrawable(resId));
        }
    }

}
