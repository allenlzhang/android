package com.carlt.yema;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.carlt.yema.dao.DBManager;
import com.carlt.yema.utils.CipherUtils;
import com.carlt.yema.utils.ILog;

import java.util.UUID;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class YemaApplication extends Application {

    public static int Version_API = 100;

    public static int VERSION_API_REMOTE = 100;// 远程下发相关Api版本

    public static int Version;

    public static String VersionName;

    public static boolean Formal_Version = false;

    public static android.content.pm.PackageManager PackageManager;

    public static int ScreenWith;

    public static int ScreenHeight;

    public static float ScreenDensity;

    public static String MODEL_NAME;// 手机名称

    public static String MODEL;// 手机型号

    public static String ANDROID_VERSION;// 手机安卓系统版本号

    public static String DISPLAY;// UI定制系统（如mi ui,Flyme OS 4.5A）

    public static String NIMEI;//手机唯一标识吗 (新的)

    public static String IMEI;//手机唯一标识吗 (旧的)

    public final static String packDate = "_2017112001";// 打包日期，打包的时候记得改

    private static YemaApplication instance;

    public static Context ApplicationContext;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ApplicationContext=this.getApplicationContext();
        Glide.init(Glide.get(instance.getApplicationContext()));
        DBManager.init(this);
        PackageManager = getPackageManager();
        try {
            Version = PackageManager.getPackageInfo(this.getPackageName(), 0).versionCode;
            VersionName = PackageManager.getPackageInfo(this.getPackageName(), 0).versionName;
            ILog.e("info","YemaApplication——————————Version=="+Version);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            ILog.e("info", "获取版本信息失败");
        }

        ILog.e("info", "YemaApplication---onCreate");

        MODEL_NAME = Build.MODEL;
        ANDROID_VERSION = "android " + Build.VERSION.RELEASE;
        MODEL = MODEL_NAME + " " + ANDROID_VERSION;
        DISPLAY = "ui_sysinfo " + android.os.Build.DISPLAY;
        ILog.e("info", "mobile_model==" + MODEL);
        ILog.e("info", "mobile_version==" + ANDROID_VERSION);
        ILog.e("info", "model_name==" + MODEL_NAME);
        ILog.e("info", "display==" + android.os.Build.DISPLAY);

        NIMEI = getNewUniquePsuedoID();
        IMEI=getUniquePsuedoID();

        // 屏幕信息
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        ScreenWith = dm.widthPixels;
        ScreenHeight = dm.heightPixels;
        ScreenDensity = dm.density;
        ILog.e("info", "ScreenDensity==" + ScreenDensity);
        ILog.e("info", "ScreenWith==" + ScreenWith);
        ILog.e("info", "ScreenHeight==" + ScreenHeight);
        // 错误LOG
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public static YemaApplication getInstanse(){
        return instance;
    }

    public static String getNewUniquePsuedoID() {
        // 主板 + CPU 类型 +
        String m_szDevIDShort = Build.BOARD
                + Build.CPU_ABI
                + Build.PRODUCT
                + Build.SERIAL
                + Build.HARDWARE;
        ILog.e("Tags:", "m_szDevIDShort_原始数据==：" + m_szDevIDShort);
        m_szDevIDShort = "ZT-" + UUID.nameUUIDFromBytes(m_szDevIDShort.getBytes()).toString();
        ILog.e("Tags:", "m_szDevIDShort==:" + m_szDevIDShort);
        ILog.e("Tags:", "NEW:" + CipherUtils.md5(m_szDevIDShort));
        return CipherUtils.md5(m_szDevIDShort);
    }

    public static String getUniquePsuedoID() {
        // Build.BOARD // 主板
        // Build.BRAND // android系统定制商
        // Build.CPU_ABI // cpu指令集
        // Build.DEVICE // 设备参数
        // Build.DISPLAY // 显示屏参数
        // Build.FINGERPRINT // 硬件名称
        // Build.HOST
        // Build.ID // 修订版本列表
        // Build.MANUFACTURER // 硬件制造商
        // Build.MODEL // 版本
        // Build.PRODUCT // 手机制造商
        // Build.TAGS // 描述build的标签
        // Build.TIME
        // Build.TYPE // builder类型
        // Build.USER
        Log.e("build", "Build.BRAND==" + Build.BRAND);
        Log.e("build", "Build.CPU_ABI==" + Build.CPU_ABI);
        Log.e("build", "Build.DEVICE==" + Build.DEVICE);
        Log.e("build", "Build.HOST==" + Build.HOST);
        Log.e("build", "Build.MANUFACTURER==" + Build.MANUFACTURER);
        Log.e("build", "Build.MODEL==" + Build.MODEL);
        Log.e("build", "Build.PRODUCT==" + Build.PRODUCT);
        Log.e("build", "Build.TYPE==" + Build.TYPE);
        Log.e("build", "Build.USER==" + Build.USER);
        Log.e("build", "Build.FINGERPRINT==" + Build.FINGERPRINT);
        Log.e("build", "Build.SERIAL==" + Build.SERIAL);
        Log.e("build", "Build.HARDWARE==" + Build.HARDWARE);
        String m_szDevIDShort = Build.BRAND + Build.CPU_ABI + Build.DEVICE
                + Build.HOST + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT
                + Build.TYPE + Build.USER + Build.FINGERPRINT + Build.SERIAL
                + Build.HARDWARE;
        m_szDevIDShort = "ZT-"
                + UUID.nameUUIDFromBytes(m_szDevIDShort.getBytes()).toString();
        Log.e("Tags:", "OLD:" + CipherUtils.md5(m_szDevIDShort));
        return CipherUtils.md5(m_szDevIDShort);
    }

    public static int dpToPx(int dp){
        int px;
        px= (int) (dp*ScreenDensity);
        return px;
    }
}
