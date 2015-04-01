package cloudteam.hicube.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import cloudteam.hicube.AppConfig;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import javax.net.ssl.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.List;

/**
 * 网络连接及网络数据传输处理工具类
 * 
 * @author summer
 */
public class NetWorkUtil {
	 static  HttpClient mHttpclient;
	 Proxy mProxy = null;
	 Context context;
	 static {
		 setDefaultHostnameVerifier();
		 try {  
	            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
	            trustStore.load(null, null);  
	            
	            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);  
	            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
	    
	            HttpParams params = new BasicHttpParams();  
	            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
	            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);  
	    
	            SchemeRegistry registry = new SchemeRegistry();  
	            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
	            registry.register(new Scheme("https", sf, 443));  
	    
	            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);  
	    
	            mHttpclient = new DefaultHttpClient(ccm, params);  
	        } catch (Exception e) {  
	        	mHttpclient = new DefaultHttpClient();  
	        }  
	 }
	public NetWorkUtil(Context context){
		this.context = context;
		
	}
	
	/**
	 * 取得网络信息数据包
	 * @param htturl 访问地址
	 * @return 获取字节流
	 */
	public byte[] getPacketByByte(String htturl) throws Exception{
		byte[] HTTPBody = null;
		HttpURLConnection conn = null;
		detectProxy();
			URL url = new URL(htturl);
			if (mProxy!=null) {
				conn = (HttpURLConnection) url.openConnection(mProxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setDoInput(true);
			conn.setConnectTimeout(AppConfig.CONNECTION_TIMEOUT);
			conn.setReadTimeout(AppConfig.SO_TIMEOUT);
			conn.connect();
			InputStream in = conn.getInputStream();
			int length = -1;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte [] bytes = new byte[1024];
			while ((length = in.read(bytes)) != -1) {
				out.write(bytes,0,length);
			}
			HTTPBody = out.toByteArray();
			in.close();
			out.close();
		return HTTPBody;
	}
	
	/**
	 * 获取网络输入流写到输出流中
	 * @param out 输出流
	 * @param htturl 访问地址
	 */
	public void getNetStream(OutputStream out,String htturl) throws Exception{
		HttpURLConnection conn = null;
		detectProxy();
			URL url = new URL(htturl);
			if (mProxy!=null) {
				conn = (HttpURLConnection) url.openConnection(mProxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setDoInput(true);
			conn.setConnectTimeout(AppConfig.CONNECTION_TIMEOUT);
			conn.setReadTimeout(AppConfig.SO_TIMEOUT);
			conn.connect();
			InputStream in = conn.getInputStream();
			int length = -1;
			
			byte [] bytes = new byte[1024];
			while ((length = in.read(bytes)) != -1) {
				out.write(bytes,0,length);
			}
			in.close();
			out.close();
	}
	
	/**
	 * 检查代理，是否cnwap接入
	 */
	private  void detectProxy() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable()
				&& ni.getType() == ConnectivityManager.TYPE_MOBILE) {
			String proxyHost = android.net.Proxy.getDefaultHost();
			int port = android.net.Proxy.getDefaultPort();
			if (proxyHost != null) {
				final InetSocketAddress sa = new InetSocketAddress(proxyHost,
						port);
				mProxy = new Proxy(Proxy.Type.HTTP, sa);
			}
		}
	}

	/**
	 * 通过URL取得字串信息
	 * @param htturl 访问地址
	 */
	public  String getHTTPPacketString(String htturl) throws Exception {
		byte[] bytes =getPacketByByte(htturl);
		String str="";
		if(bytes!=null)
			str=new String(bytes, 0, bytes.length);
		return str;
	}

	/*
	 * 通过URL取得XML内容并分解，返回XML对象
	 */
	public  Document getXML(String uri)throws Exception {
		Document dom = null;
		String xml = getHTTPPacketString(uri);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			dom = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
		}
		return dom;
	}

	/**
	 * 上传文件至Server的方法并返回是否上传成功数据
	 * 
	 * @param uri
	 *            服务器请求地址
	 * 
	 * @param fileName
	 *            文件全路径
	 * @return
	 */
	public  String uploadFileToWebServer(String uri,
			String fileName) {
		detectProxy();
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String HTTPBody = "";
		try {
			URL url = new URL(uri);
			HttpURLConnection conn = null;
			if(mProxy!=null){
				conn = (HttpURLConnection) url.openConnection(mProxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			/* 允许Input、Output，不使用Cache */
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			/* 设置传送的method=POST */
			conn.setRequestMethod("POST");
			/* setRequestProperty */
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"uploadfile\";filename=\"" + fileName + "\"" + end);
			ds.writeBytes(end);

			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(fileName);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			// 读取返回信息
			InputStream in = conn.getInputStream();
			int ch = -1;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				out.write(ch);
			}
			HTTPBody = out.toString();
			conn.disconnect();
			in.close();
			out.close();
		} catch (Exception e) {
			HTTPBody = "[数据错误]" + e.getMessage();
		}
		return HTTPBody;
	}

	/**
	 * 获取网址内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getContent(String url) throws Exception {
		StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		// 设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams,
				AppConfig.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"), 8192);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}

	// 判断GPS是否可用
	public static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = locationManager.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * 判断网络是否联接并防止wap访问网络检查
	 */
	public static boolean CheckNetworkConnected(Context context) {
		boolean isConnect = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null && network.isConnected()) {
			isConnect = true;
		} else {
//			UiUtil.showNoNetworkDialog(context);
			isConnect = false;
		}
		return isConnect;
	}

	/**
	 * 通过url获取json数据并返回jsonObject
	 * 
	 * @param url
	 *            请求地址
	 * @throws java.io.IOException
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws org.json.JSONException
	 */
	public static JSONObject doGetJson(String url)
			throws ClientProtocolException, IOException, JSONException {
		JSONObject object = null;
		String result = null;
		HttpClient httpClient = mHttpclient;
		HttpParams httpParams = httpClient.getParams();
		// 设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams,
				AppConfig.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
		HttpGet request = new HttpGet(url);
		request.setParams(httpParams);
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			object = new JSONObject(result);
		}

		return object;
	}

	/**
	 * post请求参数获取json对象
	 *
	 * @param nameValuePairlist
	 *            请求的名值对
	 * @param requestAction
	 *            请求业务接口
	 * @return JSONObject
	 * @throws java.io.IOException
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws org.json.JSONException
	 */
	public static JSONObject doPostJson(Context context,
			List<NameValuePair> nameValuePairlist, String requestAction)
			throws ClientProtocolException, IOException, JSONException {
		String result = doPostJsonStr(context,nameValuePairlist,requestAction);
		JSONObject object = new JSONObject(result);
		return object;
	}

	/**
	 * post请求参数获取json字符串
	 *
	 * @param nameValuePairlist
	 *            请求的名值对
	 * @param requestAction
	 *            请求业务接口
	 * @return JSONStr
	 * @throws java.io.IOException
	 * @throws org.apache.http.client.ClientProtocolException
	 */
	public static String doPostJsonStr(Context context,
			List<NameValuePair> nameValuePairlist, String requestAction)
			throws ClientProtocolException, IOException{
		String result = null;
		
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,AppConfig.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
		
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
	
		HttpPost httpPost = new HttpPost(requestAction);
		if (nameValuePairlist != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairlist,
					HTTP.UTF_8));
		}
		httpPost.setParams(httpParams);
		byte bytes[] = FileUtil.readLocalFile(context, AppConfig.Cookies);
		if (bytes != null && bytes.length>0 ) {
			String cookies_str = new String(bytes, 0, bytes.length);
			httpPost.setHeader("Cookie", cookies_str);
		}
		
		String httpStr = httpPost.getURI().toString();
		if (AppConfig.DEBUG) {
			for (NameValuePair nameValuePair : nameValuePairlist) {
				httpStr += ("&" + nameValuePair.getName() + "=" + nameValuePair
						.getValue());
			}
		}
		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Header[] headers = response.getHeaders("Set-Cookie");
			StringBuilder cookiesb=new StringBuilder();
			for (Header header : headers) {
				cookiesb.append(header.getValue()+";");
			}
			
			if(!"".equals(cookiesb.toString())){
				FileUtil.writeLocalFile(context, AppConfig.Cookies, cookiesb.toString().getBytes());
			}
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (AppConfig.DEBUG) {
				System.out.println("请求地址：" + httpStr);
				System.out.println("result=" + result);
				for(Header header :httpPost.getAllHeaders())
				System.out.println("===postheader参数===="+header.getName()+"="+header.getValue());
				for(Header header :response.getAllHeaders())
					System.out.println("===response参数===="+header.getName()+"="+header.getValue());
			}
		}
		return result;
	}

	/**
	 * 
	 * 获取wifi bssid 列表
	 * 
	 * @param context
	 * @return String
	 */
	public static String getNetWorkSSID(Context context) {
		String str = "";
		WifiManager wifiMan = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiMan.isWifiEnabled()) {
			WifiInfo info = wifiMan.getConnectionInfo();
			str = info.getBSSID();
		}
		return str;
	}
	
	/**
	 * get请求获取json对象
	 */
	public static JSONObject doGet(String address) {
		JSONObject json = null;
		try {
			URL url = new URL(address);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(AppConfig.CONNECTION_TIMEOUT);
			InputStream in = conn.getInputStream();
			json = new JSONObject(StringUtils.readDataFromStream(in));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	private static void setDefaultHostnameVerifier() {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
	
	static class SSLSocketFactoryEx extends SSLSocketFactory {
	        
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        
	        public SSLSocketFactoryEx(KeyStore truststore) 
	                        throws NoSuchAlgorithmException, KeyManagementException,
	                        KeyStoreException, UnrecoverableKeyException {
	                super(truststore);
	                
	                TrustManager tm = new X509TrustManager() {
	                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}  
	    
	            @Override  
	            public void checkClientTrusted(
	                            java.security.cert.X509Certificate[] chain, String authType)
	                                            throws java.security.cert.CertificateException {}  
	    
	            @Override  
	            public void checkServerTrusted(
	                            java.security.cert.X509Certificate[] chain, String authType)
	                                            throws java.security.cert.CertificateException {}
	        };  
	        sslContext.init(null, new TrustManager[] { tm }, null);  
	    }  
	    
	    @Override  
	    public Socket createSocket(Socket socket, String host, int port,boolean autoClose) throws IOException, UnknownHostException {  
	            return sslContext.getSocketFactory().createSocket(socket, host, port,autoClose);  
	    }  
	    
	    @Override  
	    public Socket createSocket() throws IOException {  
	        return sslContext.getSocketFactory().createSocket();  
	    }  
	}
}