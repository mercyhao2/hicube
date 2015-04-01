package cloudteam.hicube;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import cloudteam.hicube.api.RequestAction;
import cloudteam.hicube.utils.StringUtils;
import net.tsz.afinal.FinalBitmap;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

public class AppContext extends Application {


    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static final String MAP_KEY = "a63ae42bcb10330a1d21311a0f9341cd";

    public static final int PAGE_SIZE = 20;// 默认分页大小
    private static final int CACHE_TIME = 60 * 60000;// 缓存失效时间

    ArrayList<ExitListener> sExitListeners = new ArrayList<ExitListener>();

    public static String sVesionName = "";

    public int screenWidth;
    public int screenHeight;

    private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();

    private String saveImagePath;// 保存图片路径

    private FinalBitmap finalBitmap;

    private RequestAction requestAction;

    // -1- 百度地图--
    //private LocationClient locationClient;---1-


    //版本检测是否在继续
    public static boolean isVersionChecking = false;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            requestAction = new RequestAction(this);
            sVesionName = getVersionName();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        init();
       // initLocationClient();-1-
        initScreenDimension();
    }

    /**
     * 初始化
     */
    private void init() {
       // SDKInitializer.initialize(getApplicationContext());-1-
        finalBitmap = FinalBitmap.create(this);
        finalBitmap.configDiskCachePath(AppConfig.CACHE_IMAGE_PATH);
    }

    public FinalBitmap getFinalBitmap() {
        return finalBitmap;
    }

    /*private void initLocationClient() {  -1-
        if (locationClient != null) return;
        locationClient = new LocationClient(this);
        LocationClientOption localLocationClientOption = new LocationClientOption();
        localLocationClientOption.setOpenGps(true);
        localLocationClientOption.setAddrType("all");
        localLocationClientOption.setCoorType("bd09ll");
        localLocationClientOption.setScanSpan(1000);
        locationClient.setLocOption(localLocationClientOption);
    }

    public LocationClient getLocationClient() {-1-
        return this.locationClient;
    }*/

    /**
     * 添加退出监听
     *
     * @param exitListener
     */
    public void addExitListener(ExitListener exitListener) {
        sExitListeners.add(exitListener);
    }

    public void exit() {
        for (ExitListener exitListener : sExitListeners) {
            if (exitListener != null) {
                exitListener.onExit();
            }
        }
        System.exit(0);
    }

    public interface ExitListener {
        public void onExit();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }


    /**
     * 获取屏幕大小
     */
    private void initScreenDimension() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }


    /**
     * 检测当前系统声音是否为正常模式
     *
     * @return
     */
    public boolean isAudioNormal() {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }


    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }


    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


    /**
     * 判断缓存数据是否可读
     *
     * @param cachefile
     * @return
     */
    public boolean isReadDataCache(String cachefile) {
        return readObject(cachefile) != null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile) {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 判断缓存是否失效
     *
     * @param cachefile
     * @return
     */
    public boolean isCacheDataFailure(String cachefile) {
        boolean failure = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

    /**
     * 获取当前应用的版本号
     *
     * @return
     * @throws android.content.pm.PackageManager.NameNotFoundException
     */
    private String getVersionName() throws NameNotFoundException {
        PackageManager packageManager = getPackageManager();// 获取packagemanager的实例
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);// getPackageName()是你当前类的包名，0代表是获取版本信息
        return packInfo.versionName;
    }

    /**
     * 是否需要更新
     *
     * @param versionName
     * @return
     * @throws android.content.pm.PackageManager.NameNotFoundException
     */
    public boolean isNeededUpdate(String versionName) throws NameNotFoundException {
        PackageManager packageManager = getPackageManager();// 获取packagemanager的实例
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);// getPackageName()是你当前类的包名，0代表是获取版本信息
        int compare = versionName.compareTo(packInfo.versionName);
        return compare > 0 ? true : false;
    }


    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    public int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 清除缓存目录
     *
     * @return
     */
    public int clearCache(String prefix) {
        int deletedFiles = 0;
        File dir = getFilesDir();
        long curTime = System.currentTimeMillis();
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {

                    if (child.lastModified() < curTime && child.getName().startsWith(prefix)) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 将对象保存到内存缓存
     *
     * @param key
     * @param value
     */
    public void setMemCache(String key, Object value) {
        memCacheRegion.put(key, value);
    }

    /**
     * 从内存缓存中获取对象
     *
     * @param key
     * @return
     */
    public Object getMemCache(String key) {
        return memCacheRegion.get(key);
    }

    /**
     * 保存磁盘缓存
     *
     * @param key
     * @param value
     * @throws java.io.IOException
     */
    public void setDiskCache(String key, String value) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.flush();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取磁盘缓存数据
     *
     * @param key
     * @return
     * @throws java.io.IOException
     */
    public String getDiskCache(String key) throws IOException {
        FileInputStream fis = null;
        try {
            fis = openFileInput("cache_" + key + ".data");
            byte[] datas = new byte[fis.available()];
            fis.read(datas);
            return new String(datas);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws java.io.IOException
     */
    public boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(file, MODE_APPEND);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public Serializable readObject(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取内存中保存图片的路径
     *
     * @return
     */
    public String getSaveImagePath() {
        return saveImagePath;
    }

    /**
     * 设置内存中保存图片的路径
     *
     * @return
     */
    public void setSaveImagePath(String saveImagePath) {
        this.saveImagePath = saveImagePath;
    }

}
