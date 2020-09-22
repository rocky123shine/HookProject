package com.rocky.hookproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static PluginManager pluginManager;
    private Context context;
    public static final String TAG = PluginManager.class.getSimpleName();
    private DexClassLoader dexClassLoader;
    private Resources resources;

    private PluginManager(Context context) {
        this.context = context;
    }

    public static PluginManager getInstance(Context context) {
        if (pluginManager == null) {
            synchronized (PluginManager.class) {
                if (pluginManager == null) {
                    pluginManager = new PluginManager(context);
                }
            }
        }
        return pluginManager;
    }


    public void loadPlugin() {
        try {


            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin_package-debug.apk");
            if (!file.exists()) {
                Log.d(TAG, "插件包不存在≥。。。。。");
                return;
            }
            String pluginPath = file.getAbsolutePath();
            Log.d(TAG, "loadPlugin: " + pluginPath);
            File pDir = context.getDir("pDir", Context.MODE_PRIVATE);
            dexClassLoader = new DexClassLoader(
                    pluginPath,
                    pDir.getAbsolutePath(),
                    null,
                    context.getClassLoader()
            );

            //加载res
            AssetManager assetManager
                    = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginPath);
            Resources r = context.getResources();//宿主资源
            //特殊resource 用来加载宿主资源
            resources = new Resources(assetManager,
                    r.getDisplayMetrics(),
                    r.getConfiguration());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Resources getResources() {
        return resources;
    }

    public ClassLoader getClassLoader() {
        return dexClassLoader;
    }

    public void parseApkAction() {
        //流程总结
        //首先app安装后会被系统扫描 找到AndroidManifest PMS 开始解析
        //PMS 通过 android.content.pm.PackageParser 执行 parsePackage方法 解析得到 package对象
        //此时 package对象对应的 是整个清单文件信息
        /**
         *         public final ArrayList<Permission> permissions = new ArrayList<Permission>(0);-----> 对应权限
         *         public final ArrayList<PermissionGroup> permissionGroups = new ArrayList<PermissionGroup>(0);-----对应权限组
         *         public final ArrayList<Activity> activities = new ArrayList<Activity>(0);------对应四大组件
         *         public final ArrayList<Activity> receivers = new ArrayList<Activity>(0);------对应四大组件
         *         public final ArrayList<Provider> providers = new ArrayList<Provider>(0);------对应四大组件
         *         public final ArrayList<Service> services = new ArrayList<Service>(0);------对应四大组件
         *         。。。。。 等
         */
        //通过package 拿到数组 receivers
        // 遍历 receivers 拿到activity对象
        //通过actiivty 拿到过滤器参数和receiver声明的全类名
        //组装  注册
        try {

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin_package-debug.apk");
            if (!file.exists()) {
                Log.d(TAG, "插件包不存在≥。。。。。");
                return;
            }
            String pluginPath = file.getAbsolutePath();

            //1 执行 parsePackage（）
            Class<?> mPackageParseClass = Class.forName("android.content.pm.PackageParser");
            Object mPackageParser = mPackageParseClass.newInstance();
            Method mPackageParseClassMethod = mPackageParseClass.getMethod("parsePackage", File.class, int.class);
            Object mPackage = mPackageParseClassMethod.invoke(mPackageParser, file, PackageManager.GET_ACTIVITIES);
            //拿到集合
            Field receiversField = mPackage.getClass().getDeclaredField("receivers");
            Object receivers = receiversField.get(mPackage);
            ArrayList arrayList = (ArrayList) receivers;
            for (Object mActivity : arrayList) {//此处activity并非组件activity 是Component<ActivityIntentInfo>子类

                //获取intent-filter
                //拿到intents
                Class<?> mComponentClass = Class.forName("android.content.pm.PackageParser$Component");
                Field intentsField = mComponentClass.getDeclaredField("intents");
                ArrayList<IntentFilter> intents = (ArrayList<IntentFilter>) intentsField.get(mActivity);

                Class<?> mPackageUserStateClass = Class.forName("android.content.pm.PackageUserState");

                Method generateActivityInfoMethod = mPackageParseClass.getMethod("generateActivityInfo", mActivity.getClass(), int.class,
                        mPackageUserStateClass, int.class);
                Class<?> mUserHandleClass = Class.forName("android.os.UserHandle");
                int getCallingUserId = (int) mUserHandleClass.getMethod("getCallingUserId").invoke(null);
                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfoMethod.invoke(null, mActivity, 0,
                        mPackageUserStateClass.newInstance(), getCallingUserId);
                Class<?> mStaticReceiverClass = getClassLoader().loadClass(activityInfo.name);
                BroadcastReceiver receiver = (BroadcastReceiver) mStaticReceiverClass.newInstance();
                for (IntentFilter intent : intents) {
                    //要拿到receiver注册的name---全类名
                    //需要先拿到activityInfo  activityInfo.name == android:name
                    context.registerReceiver(receiver, intent);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
