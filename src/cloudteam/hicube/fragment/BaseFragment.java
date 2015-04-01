package cloudteam.hicube.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cloudteam.hicube.api.RequestAction;
import cloudteam.hicube.api.RequestAction.IJSONtoObjectUtil;
import cloudteam.hicube.bean.TaskInfo;
import cloudteam.hicube.utils.SharePrefHelper;
import net.tsz.afinal.FinalActivity;

public class BaseFragment extends Fragment {
    protected Context context;
    private RequestAction requestAction;
    protected SharePrefHelper sharePrefHelper;
    protected String token;
    private int viewLayoutId;


    protected void setContentView(int paramInt) {
        viewLayoutId = paramInt;
    }

    protected void Request(TaskInfo paramTaskInfo, IJSONtoObjectUtil paramIJSONtoObjectUtil) {
        requestAction.Request(paramTaskInfo, paramIJSONtoObjectUtil);
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    protected void initView() {
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(viewLayoutId, paramViewGroup, false);
        FinalActivity.initInjectedView(this, localView);
        context = getActivity();
        requestAction = new RequestAction(context);
        sharePrefHelper = new SharePrefHelper(context, "yunti_sp");
        token = sharePrefHelper.getSharePreStr("token");
        initData();
        initView();
        initListener();
        return localView;
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onFClick(View paramView) {
    }

    public void onFItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    }

    public boolean onFItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        return false;
    }

    public void onFraResume() {
    }

}

