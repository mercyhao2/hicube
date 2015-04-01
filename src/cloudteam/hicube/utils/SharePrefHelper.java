package cloudteam.hicube.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SharePrefHelper
{
  private Context context;
  private String sName;

  public SharePrefHelper(Context paramContext, String paramString)
  {
    this.context = paramContext;
    this.sName = paramString;
  }

  public boolean getSharePreBoolean(String paramString)
  {
    return this.context.getSharedPreferences(this.sName, 0).getBoolean(paramString, false);
  }

  public boolean getSharePreBoolean(String paramString, boolean paramBoolean)
  {
    return this.context.getSharedPreferences(this.sName, 0).getBoolean(paramString, paramBoolean);
  }

  public float getSharePreFloat(String paramString)
  {
    return this.context.getSharedPreferences(this.sName, 0).getFloat(paramString, 0.0F);
  }

  public int getSharePreInt(String paramString)
  {
    return this.context.getSharedPreferences(this.sName, 0).getInt(paramString, 0);
  }

  public long getSharePreLong(String paramString)
  {
    return this.context.getSharedPreferences(this.sName, 0).getLong(paramString, 0L);
  }

  public String getSharePreStr(String paramString)
  {
    return this.context.getSharedPreferences(this.sName, 0).getString(paramString, "");
  }

  public void setSharePre(String paramString, float paramFloat)
  {
    Editor localEditor = this.context.getSharedPreferences(this.sName, 0).edit();
    localEditor.putFloat(paramString, paramFloat);
    localEditor.commit();
  }

  public void setSharePre(String paramString, int paramInt)
  {
    Editor localEditor = this.context.getSharedPreferences(this.sName, 0).edit();
    localEditor.putInt(paramString, paramInt);
    localEditor.commit();
  }

  public void setSharePre(String paramString, long paramLong)
  {
    Editor localEditor = this.context.getSharedPreferences(this.sName, 0).edit();
    localEditor.putLong(paramString, paramLong);
    localEditor.commit();
  }

  public void setSharePre(String paramString1, String paramString2)
  {
    Editor localEditor = this.context.getSharedPreferences(this.sName, 0).edit();
    localEditor.putString(paramString1, paramString2);
    localEditor.commit();
  }

  public void setSharePre(String paramString, boolean paramBoolean)
  {
    Editor localEditor = this.context.getSharedPreferences(this.sName, 0).edit();
    localEditor.putBoolean(paramString, paramBoolean);
    localEditor.commit();
  }
}
