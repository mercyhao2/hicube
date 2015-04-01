package cloudteam.hicube.bean;

import android.annotation.SuppressLint;
import cloudteam.hicube.utils.DesUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"ParserError"})
public class TaskInfo
  implements Serializable
{
  private static final String DATA_DES_KEY = "qwertzxcvb";
  private static final String PARAM = "param";
  private static final long serialVersionUID = 1L;
  public String cacheKey;
  public boolean cacheOpened = true;
  public String cachePath;
  public boolean isRefresh = false;
  private JSONObject jsonObject;
  private List<NameValuePair> namevaluelist = new ArrayList();
  private String requestAction;

  public void addDesParam(String paramString, Object paramObject)
  {
    Object localObject = "";
    try
    {
      String str = DesUtil.encryptDES(paramObject.toString(), "qwertzxcvb");
      localObject = str;
      this.namevaluelist.add(new BasicNameValuePair(paramString, (String)localObject));
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void addParam(String paramString, Object paramObject)
  {
    this.namevaluelist.add(new BasicNameValuePair(paramString, paramObject.toString()));
  }

  public JSONObject getJsonObject()
  {
    return this.jsonObject;
  }

  public List<NameValuePair> getNamevaluelist()
  {
    return this.namevaluelist;
  }

  public String getRequestAction()
  {
    return this.requestAction;
  }

  public void setDesJsonObject(JSONObject paramJSONObject)
  {
    this.jsonObject = paramJSONObject;
    addDesParam("param", paramJSONObject);
  }

  public void setJsonObject(JSONObject paramJSONObject)
  {
    this.jsonObject = paramJSONObject;
    addParam("param", paramJSONObject);
  }

  public void setRequestAction(String paramString)
  {
    this.requestAction = paramString;
  }
}
