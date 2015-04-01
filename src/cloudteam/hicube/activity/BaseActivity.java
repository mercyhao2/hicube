package cloudteam.hicube.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import cloudteam.hicube.AppContext;
import cloudteam.hicube.api.RequestAction;
import cloudteam.hicube.bean.TaskInfo;
import cloudteam.hicube.db.DBHelper;
import cloudteam.hicube.utils.SharePrefHelper;
import org.apache.commons.httpclient.methods.multipart.Part;

public class BaseActivity extends Activity {
    protected AppContext appContext;
    protected Context context;
    protected DBHelper dbHelper;
    protected RequestAction requestAction;
    protected SharePrefHelper sharePrefHelper;
    protected String token;

    protected void Request(TaskInfo taskInfo, RequestAction.IJSONtoObjectUtil jou) {
        requestAction.Request(taskInfo, jou);
    }

    protected void Request2(Part[] parts, RequestAction.IJSONtoObjectUtil jou) {
        requestAction.Request2(parts, jou);
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        context = this;
        requestAction = new RequestAction(this);
        appContext = ((AppContext) getApplication());
        dbHelper = new DBHelper(this);
        sharePrefHelper = new SharePrefHelper(this, "yunti_sp");
        token = sharePrefHelper.getSharePreStr("token");
    }

    protected void showToast(String paramString) {
        Toast.makeText(context, paramString, Toast.LENGTH_LONG).show();
    }
}

