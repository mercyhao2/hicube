package cloudteam.hicube.fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import cloudteam.hicube.R;
import cloudteam.hicube.api.ApiParam;
import cloudteam.hicube.api.RequestAction;

import cloudteam.hicube.utils.OtherUtil;
import cloudteam.hicube.utils.StringUtils;
import cloudteam.hicube.utils.TimeUtil;
import cloudteam.hicube.view.xlistview.XListView;
import cloudteam.hicube.view.xlistview.XListView.XListViewListener;
import com.alibaba.fastjson.JSON;
import net.tsz.afinal.annotation.view.ViewInject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainFragment extends BaseFragment
        implements XListViewListener {
    //private ListFoundStatisticsAdapter adapter;
    /*private Calendar calendar = null;

    @ViewInject(click = "onClick", id = R.id.end_date)
    private TextView endDate;
    private String endTimeStr = "";
    private List<FoundStatisticsInfo> infos = new ArrayList();

    @ViewInject(id = R.id.list_found)
    private XListView listFound;

    @ViewInject(click = "onClick", id = R.id.search)
    private TextView search;

    @ViewInject(click = "onClick", id = R.id.start_date)
    private TextView startDate;
    private String startTimeStr = "";*/


    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        setContentView(R.layout.fragement_main);
        return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }

    /*private void getOpenReport(String paramString1, String paramString2) {
        Request(ApiParam.getMyOpenReport(paramString1, paramString2, token), new RequestAction.IJSONtoObjectUtil() {
            public void Response(JSONObject paramJSONObject) {
                OtherUtil.stopPD();
                try {
                    if (paramJSONObject.getBoolean("success")) {
                        infos.addAll(JSON.parseArray(paramJSONObject.getString("result"),
                                FoundStatisticsInfo.class));
                        for (int i = 1; i < infos.size(); ++i){
                            infos.get(i).setNumber(i + "");
                        }
                         adapter.notifyDataSetChanged();
                        return;
                    }
                    Toast.makeText(context, paramJSONObject.getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
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
        listFound.setXListViewListener(this);
        listFound.setPullRefreshEnable(false);
        listFound.setPullLoadEnable(false);
        FoundStatisticsInfo localFoundStatisticsInfo = new FoundStatisticsInfo();
        localFoundStatisticsInfo.setAnnualFee("年费");
        localFoundStatisticsInfo.setEndYmd("到期日期");
        localFoundStatisticsInfo.setCustomerName("店铺名称");
        localFoundStatisticsInfo.setNumber("序号");
        localFoundStatisticsInfo.setSalerName("业务员");
        localFoundStatisticsInfo.setOpenYmd("开通日期");
        localFoundStatisticsInfo.setPlatformName("业务系统");
        infos.add(localFoundStatisticsInfo);
        adapter = new ListFoundStatisticsAdapter(context, infos);
        listFound.setAdapter(adapter);

        listFound.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                if (StringUtils.isEmpty(infos.get(paramInt - 1).getCustomerId())) return;
                Intent localIntent = new Intent(context, CustomerDetailActivity.class);
                localIntent.putExtra("customerId", infos.get(paramInt - 1).getCustomerId());
                startActivity(localIntent);
            }
        });
        getOpenReport(TimeUtil.getFirstOrEndDate(0), TimeUtil.getTodayDate());
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
                getOpenReport(str1, str2);
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
                        + TimeUtil.Timezerofill(Integer.valueOf(paramInt3)));
            }
        }, i, j, k);
    }
*/

    public void onListViewLoadMore(XListView paramXListView) {
    }

    public void onListViewRefresh(XListView paramXListView) {
    }
}
