package cloudteam.hicube.fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import cloudteam.hicube.R;
import cloudteam.hicube.api.ApiParam;
import cloudteam.hicube.api.RequestAction;
import cloudteam.hicube.utils.OtherUtil;
import cloudteam.hicube.utils.TimeUtil;
import com.alibaba.fastjson.JSON;
import net.tsz.afinal.annotation.view.ViewInject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MineFragment extends BaseFragment {
    private Calendar calendar = null;

    /*@ViewInject(id = R.id.customer_count)
    private TextView customerCount;

    @ViewInject(click = "onClick", id = R.id.end_date)
    private TextView endDate;

    private String endTimeStr = "";
    private CrmStatInfo info;

    @ViewInject(id = R.id.list_count)
    private TextView listCount;

    @ViewInject(id = R.id.name)
    private TextView name;

    @ViewInject(click = "onClick", id = R.id.search)
    private TextView search;

    @ViewInject(click = "onClick", id = R.id.start_date)
    private TextView startDate;
    private String startTimeStr = "";

    @ViewInject(id = R.id.visit_count)
    private TextView visitCount;*/


    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        setContentView(R.layout.fragement_mine);
        return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }

   /* private void getCrmStats(String paramString1, String paramString2) {
        Request(ApiParam.getCrmStats(paramString1, paramString2, token), new RequestAction.IJSONtoObjectUtil() {
            public void Response(JSONObject paramJSONObject) {
                OtherUtil.stopPD();
                try {
                    if (paramJSONObject.getBoolean("success")) {
                        if (JSON.parseArray(paramJSONObject.getString("result"),CrmStatInfo.class).size() <= 0) return;
                        info = JSON.parseArray(paramJSONObject.getString("result"), CrmStatInfo.class).get(0);
                        setText(info);
                        return;
                    }
                    Toast.makeText(context, paramJSONObject.getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException localJSONException) {
                    localJSONException.printStackTrace();
                }
            }

            public void onClientError() {
                OtherUtil.stopPD();
            }

            public void onStart() {
                OtherUtil.creatPD(context, "加载中...");
            }
        });
    }

    protected void initData() {
        getCrmStats(TimeUtil.getFirstOrEndDate(0), TimeUtil.getTodayDate());
    }

    protected void initView() {
        super.initView();
        startDate.setText(TimeUtil.getFirstOrEndDate(0));
        endDate.setText(TimeUtil.getTodayDate());
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.start_date:
                startTimeStr = startDate.getText().toString();
                onCreateDialog((TextView) paramView, startTimeStr).show();
                break;
            case R.id.end_date:
                endTimeStr = endDate.getText().toString();
                onCreateDialog((TextView) paramView, endTimeStr).show();
                break;
            case R.id.search:
                String str1 = startDate.getText().toString();
                String str2 = endDate.getText().toString();
                if ((str2.equals("")) || (str1.equals(""))) {
                    Toast.makeText(context, "请输入起止日期", Toast.LENGTH_LONG).show();
                    return;
                }
                getCrmStats(str1, str2);
                break;
        }

    }

    protected Dialog onCreateDialog(final TextView paramTextView, String paramString) {
        calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.YEAR);
        int j = calendar.get(Calendar.MONTH);
        int k = calendar.get(Calendar.DATE);
        if (!(paramString.equals(""))) {
            String[] arrayOfString = paramString.split("-");
            i = Integer.parseInt(arrayOfString[0]);
            j = -1 + Integer.parseInt(arrayOfString[1]);
            k = Integer.parseInt(arrayOfString[2]);
        }
        return new DatePickerDialog(context, new OnDateSetListener() {
            public void onDateSet(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3) {
                paramTextView.setText(paramInt1 + "-"
                        + TimeUtil.Timezerofill(Integer.valueOf(paramInt2 + 1)) + "-"
                        + TimeUtil.Timezerofill(Integer.valueOf(paramInt3))); }
        }, i, j, k);
    }


    public void setText(CrmStatInfo paramCrmStatInfo) {
        name.setText(paramCrmStatInfo.getCreatorName());
        customerCount.setText(paramCrmStatInfo.getNewCustomerCount() + "人");
        listCount.setText(paramCrmStatInfo.getTrackCount() + "次");
        visitCount.setText(paramCrmStatInfo.getReviewCount() + "次");
    }*/
}
