package cloudteam.hicube;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class AppConfig {

  private static AppConfig appConfig;

  private static final String APP_CONFIG = "config";

  public static final int STATE_FAILED = 0;
  public static final int STATE_SUCCESS = 1;
  public static final int PAGE_SIZE = 20;

  public static final int CONNECTION_TIMEOUT = 1000 * 10; // 连接超时
  public static final int SO_TIMEOUT = 1000 * 30; // 数据传输超时设置
  public static final String Cookies = "cookies";// 存储cookies

  public final static boolean DEBUG = true;
  public static final String CACHE_ROOT = "";
  
  public static final String BASE_URL = "http://yt.cloudvast.com/";

  public static final String API_URL = BASE_URL+"app/";

  public static final String LOGIN_URL = API_URL+"login.htm";

  public static final String ADD_LOCATION = API_URL+"workmate/addLocation.htm";
  public static final String ADD_PLATFORM = API_URL+"addPlatformOpenRecord.htm";
  public static final String ADD_RENEW_RECORD = API_URL+"addRenewRecord.htm";
  public static final String ADD_REVIEW = API_URL+"addReviewRecord.htm";
  public static final String ADD_TRACK = API_URL+"addTrackRecord.htm";

  
  public static final String CLOSE_PLATFORM = API_URL+"closePlatformOpenRecord.htm";
  public static final String CUSTOMER_ADD = API_URL+"addCustomer.htm";
  public static final String CUSTOMER_MODIFY = API_URL+"editCustomer.htm";
  public static final String CUSTOMER_SYNC = API_URL+"customer/sync.htm";

  public static final String FIND_CUSTOMER_BY_LOCATION = API_URL+"findCustomerByLocation.htm";
  public static final String GET_CITYS = API_URL+"cities.htm";
  public static final String GET_CRM_STATS = API_URL+"findCrmStats.htm";
  public static final String GET_CUSTOMER_DETAIL = API_URL+"findCustomer.htm";
  public static final String GET_CUSTOMER_LIST = API_URL+"customerSearch.htm";
  public static final String GET_DISTRICTS = API_URL+"districts.htm";
  public static final String GET_MY_OPEN_REPORT = API_URL+"myOpenReport.htm";
  public static final String GET_MY_RENEW = API_URL+"myRenewReport.htm";
  public static final String GET_ORG_TREE = API_URL+"findOrgTree.htm";
  public static final String GET_PLATFORM = API_URL+"findPlatforms.htm";
  public static final String GET_PLATFORM_NODES = API_URL+"findPlatformNodes.htm";
  public static final String GET_PLAT_FORM = API_URL+"findPlatformOpenRecord.htm";
  public static final String GET_PROVINCES = API_URL+"provinces.htm";
  public static final String GET_RESOURCELIST = API_URL+"resourcelist.htm";
  public static final String GET_REVIEW = API_URL+"findReviewRecords.htm";
  public static final String GET_ROLE = API_URL+"findDefaultRole.htm";
  public static final String GET_SALER_TREE = API_URL+"findSalerTree.htm";
  public static final String GET_TRACK = API_URL+"findTrackRecords.htm";




  public static final String PERMISSION_ADD_RENEW = "addRenewRecord";
  public static final String PERMISSION_DELETE_OPEN = "deleteOpenRecordBtn";
  public static final String PERMISSION_OPEN_PLAT = "openPlatformBtn";
  public static final String SCREEN_SHARE_KEY = "screenSetting";
  public static final String SHARE_CHEAK = "yunti_sp";

  public static final String IMAGE_BASE_URL = "http://img.cloudvast.com/";

  public static final String JSON_RESPONSE_STATE = "success";

  public static final String UPDATA_BASE_URL = "http://www.cloudvast.com/version/ytAndroid.js";

  public static final String publickey = "";
  public static final String seckey = "";


  public static final String DEFAULT_CACHE_PATH = Environment.getExternalStorageDirectory()
                  + File.separator + "yunti" + File.separator;

  public static final  String CACHE_IMAGE_PATH = DEFAULT_CACHE_PATH + "image" + File.separator;
  public static final  String DOWNLOAD_IMAGE_PATH = DEFAULT_CACHE_PATH + "download_image" + File.separator;
  public static final  String  UPLOAD_IMAGE_PATH = DEFAULT_CACHE_PATH + "upload" + File.separator;
  public static final  boolean CheckSD = false;

  private Context mContext;


  public static void createUploadFile() {
    if (Environment.getExternalStorageState().equals("mounted")) {
      File localFile = new File(UPLOAD_IMAGE_PATH);
      if (!(localFile.exists())) localFile.mkdirs();
    }
  }

  public static AppConfig getAppConfig(Context paramContext) {
    if (appConfig == null) {
      appConfig = new AppConfig();
      appConfig.mContext = paramContext;
    }
    return appConfig;
  }

  public String get(String key) {
    Properties props = get();
    return (props != null) ? props.getProperty(key) : null;
  }

  public Properties get() {
    FileInputStream fis = null;
    Properties props = new Properties();
    try {
      // 读取files目录下的config
      // fis = activity.openFileInput(APP_CONFIG);

      // 读取app_config目录下的config
      File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
      fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);

      props.load(fis);
    } catch (Exception e) {
    } finally {
      try {
        fis.close();
      } catch (Exception e) {
      }
    }
    return props;
  }

  private void setProps(Properties p) {
    FileOutputStream fos = null;
    try {
      File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
      File conf = new File(dirConf, APP_CONFIG);
      fos = new FileOutputStream(conf);
      p.store(fos, null);
      fos.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        fos.close();
      } catch (Exception e) {
      }
    }
  }

  public void set(Properties ps) {
    Properties props = get();
    props.putAll(ps);
    setProps(props);
  }

  public void set(String key, String value) {
    Properties props = get();
    props.setProperty(key, value);
    setProps(props);
  }

  public void remove(String... key) {
    Properties props = get();
    for (String k : key)
      props.remove(k);
    setProps(props);
  }
}