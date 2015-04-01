package cloudteam.hicube.api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import cloudteam.hicube.AppConfig;
import cloudteam.hicube.AppContext;
import cloudteam.hicube.bean.TaskInfo;
import cloudteam.hicube.utils.NetWorkUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;


/**
 * 所有应用的基类
 *
 * @author summer
 */
public class RequestAction {

    private final int TIP_TIMEOUT = 1;
    private final int TIP_REQUESTDERROR = 2;
    private final int TIP_CLIENT_CONN = 3;
    private final int TIP_GETCAHCEERROR = 4;
    private final int TIP_NO_NETWORK = 5;
    private final int TIMES = 5;//超时重试次数


    private int times;
    private TaskInfo mTaskinfo;
    private IJSONtoObjectUtil mJosonObjectUtil;

    private final int CONNECTION_TIMEOUT = 5000, SO_TIMEOUT = 15000;

    private Context mContext;
    private AppContext appContext;

    public RequestAction(Context context) {
        super();
        mContext = context;
        appContext = (AppContext) mContext.getApplicationContext();
    }

    public static String AuthParam; // 授权参数
    private PublicTask pt;// 异步任务类
    public String bssId;// wifi 唯一标示

    private PublicTask2 pt2;

    private Handler androidHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TIP_CLIENT_CONN:
                    Toast.makeText(mContext, "请检查网络连接是否正常", Toast.LENGTH_SHORT).show();
                    break;
                case TIP_TIMEOUT:
                    Toast.makeText(mContext, "网络连接超时", Toast.LENGTH_SHORT).show();
                    break;
                case TIP_GETCAHCEERROR:
                    Toast.makeText(mContext, "没有可用的缓存数据", Toast.LENGTH_SHORT).show();
                    break;
                case TIP_REQUESTDERROR:
                    Toast.makeText(mContext, "服务器端响应失败", Toast.LENGTH_SHORT).show();
                    break;
                case TIP_NO_NETWORK:
                    Toast.makeText(mContext, "网络连接没打开，请打开网络连接", Toast.LENGTH_SHORT).show();
                    break;
            }
            if (msg.obj != null && msg.obj instanceof IJSONtoObjectUtil) {
                IJSONtoObjectUtil Ijou = (IJSONtoObjectUtil) msg.obj;
                Ijou.onClientError();
            }
            return false;
        }
    });

    /**
     * 异步任务处理
     *
     * @author summer
     */
    private final class PublicTask extends AsyncTask<TaskInfo, Object, JSONObject> {

        private IJSONtoObjectUtil jou;


        /**
         * 加载的视图
         *
         * @param jou 数据映射接口
         */
        public PublicTask(IJSONtoObjectUtil jou) {
            this.jou = jou;
        }

        @Override
        protected JSONObject doInBackground(TaskInfo... params) {
            JSONObject responseResult = null;
            Message msg = null;
            for (TaskInfo taskInfo : params) {
                try {
                    boolean isRefresh = taskInfo.isRefresh;
                    String key = taskInfo.cacheKey;
                    if (!taskInfo.cacheOpened || !appContext.isReadDataCache(key) || isRefresh) { // 在开启缓存情况下判断是否有缓存存在
                        if (!appContext.isNetworkConnected()) {
                            msg = androidHandler.obtainMessage(TIP_NO_NETWORK, jou);
                            msg.sendToTarget();
                            return null;
                        }
                        String str = NetWorkUtil.doPostJsonStr(mContext, taskInfo.getNamevaluelist(), taskInfo.getRequestAction());
                        responseResult = new JSONObject(str);
                        if (str != null && taskInfo.cacheOpened) {
                            if (isRefresh) {  //刷新，清除所有分類緩存
                                String filPrefix = key.substring(0, key.indexOf("_") + 1);
                                appContext.clearCache(filPrefix);
                            }
                            appContext.saveObject(str, key);
                        }
                    } else {
                        String str = (String) appContext.readObject(key);
                        responseResult = new JSONObject(str);
                    }
                } catch (ClientProtocolException e) {
                    if (jou != null) {
                        msg = androidHandler.obtainMessage(TIP_CLIENT_CONN, jou);
                    } else {
                        msg = androidHandler.obtainMessage(TIP_CLIENT_CONN);
                    }
                    msg.sendToTarget();
                    if (AppConfig.DEBUG) e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    times++;
                    if (times < TIMES) {
                        pt = new PublicTask(mJosonObjectUtil);
                        pt.execute(mTaskinfo);
                    } else {
                        if (jou != null) {
                            msg = androidHandler.obtainMessage(TIP_TIMEOUT, jou);
                        } else {
                            msg = androidHandler.obtainMessage(TIP_TIMEOUT);
                        }
                        msg.sendToTarget();
                    }
                    if (AppConfig.DEBUG) e.printStackTrace();
                } catch (Exception e) {
                    times++;
                    if (times < TIMES) {
                        pt = new PublicTask(mJosonObjectUtil);
                        pt.execute(mTaskinfo);
                    } else {
                        if (jou != null) {
                            msg = androidHandler.obtainMessage(TIP_REQUESTDERROR, jou);
                        } else {
                            msg = androidHandler.obtainMessage(TIP_REQUESTDERROR);
                        }
                        msg.sendToTarget();
                    }
                    e.printStackTrace();
                }
            }
            return responseResult;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (jou != null && result != null) jou.Response(result);
        }

        @Override
        protected void onPreExecute() {
            if (jou != null && times == 0) jou.onStart();
        }

    }


    private final class PublicTask2 extends AsyncTask<Part[], Object, JSONObject> {

        private IJSONtoObjectUtil jou;

        public PublicTask2(IJSONtoObjectUtil jou) {
            this.jou = jou;
        }

        protected JSONObject doInBackground(Part[][] parts) {

            Message msg;
            JSONObject responseResult = null;

            if (!appContext.isNetworkConnected()) {
                msg = androidHandler.obtainMessage(TIP_NO_NETWORK, jou);
                msg.sendToTarget();
                return null;
            }

            HttpClient client = new HttpClient();
            PostMethod post = new PostMethod(parts[0][0].toString());
            post.setFollowRedirects(false);
            post.setRequestEntity(new MultipartRequestEntity(parts[0], post.getParams()));

            try {
                int state = client.executeMethod(post);
                if (state == HttpStatus.SC_OK) {
                    String string = post.getResponseBodyAsString();
                    responseResult = new JSONObject(string);
                }
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                if (jou != null) {
                    msg = androidHandler.obtainMessage(TIP_REQUESTDERROR, jou);
                } else {
                    msg = androidHandler.obtainMessage(TIP_REQUESTDERROR);
                }
                msg.sendToTarget();
                e.printStackTrace();
            }

            return responseResult;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if ((jou == null) || (jsonObject == null)) return;
            jou.Response(jsonObject);
        }

        @Override
        protected void onPreExecute() {
            if (jou == null) return;
            jou.onStart();
        }
    }


    // 注入数据对象
    public interface IJSONtoObjectUtil {
        /**
         * 开始请求数据
         */
        void onStart();

        /**
         * errorCode=1 成功才调用该方法 返回数据处理
         *
         * @param jsonobj
         */
        void Response(JSONObject jsonobj);

        /**
         * 客户端请求失败
         * <p/>
         * 显示错误信息
         */
        void onClientError();
    }

    public final void Request(TaskInfo taskinfo, IJSONtoObjectUtil jou) {
        times = 0;
        mTaskinfo = taskinfo;
        mJosonObjectUtil = jou;
        pt = new PublicTask(jou);
        pt.execute(taskinfo);
    }


    public final void Request2(Part[] paramArrayOfPart, IJSONtoObjectUtil jou) {
        mJosonObjectUtil = jou;
        pt2 = new PublicTask2(jou);
        pt2.execute(new Part[][]{paramArrayOfPart});
    }


}
