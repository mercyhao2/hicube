package cloudteam.hicube.fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import cloudteam.hicube.R;
import cloudteam.hicube.api.ApiParam;
import cloudteam.hicube.api.RequestAction;
import cloudteam.hicube.api.RequestAction.IJSONtoObjectUtil;
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

public class StoreFragment extends BaseFragment
        implements XListViewListener {
   /* private ListMoneyStatisticsAdapter adapter;
    private Calendar calendar = null;

    @ViewInject(id = R.id.edit_gather)
    private Spinner editGather;

    @ViewInject(click = "onClick", id = R.id.end_date)
    private TextView endDate;
    private String endTimeStr = "";
    private List<MoneyStatisticsInfo> infos = new ArrayList();

    @ViewInject(id = R.id.list_money)
    private XListView listMoney;

    @ViewInject(click = "onClick", id = R.id.search)
    private TextView search;
    private ArrayAdapter spinnerAdapter;

    @ViewInject(click = "onClick", id = R.id.start_date)
    private TextView startDate;
    private String startTimeStr = "";

    @ViewInject(id = R.id.store_id)
    private TextView storeId;*/

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        setContentView(R.layout.fragement_store);
        return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }

   /* private void getRenew(String paramString1, String paramString2, String paramString3) {
        Request(ApiParam.getMyRenew(paramString1, paramString2, paramString3, token), new RequestAction.IJSONtoObjectUtil() {
            public void Response(JSONObject paramJSONObject) {
                OtherUtil.stopPD();
                try {
                    if (paramJSONObject.getBoolean("success")) {
                        infos.addAll(JSON.parseArray(paramJSONObject.getString("result"), MoneyStatisticsInfo.class));
                        for (int i = 1; i < infos.size(); ++i){
                            infos.get(i).setNumber(i + "");
                        }adapter.notifyDataSetChanged();
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
        listMoney.setXListViewListener(this);
        listMoney.setPullRefreshEnable(false);
        listMoney.setPullLoadEnable(false);
        MoneyStatisticsInfo localMoneyStatisticsInfo = new MoneyStatisticsInfo();
        localMoneyStatisticsInfo.setFee("年费");
        localMoneyStatisticsInfo.setCustomerName("店铺名称");
        localMoneyStatisticsInfo.setNumber("序号");
        localMoneyStatisticsInfo.setCreatorName("业务员");
        localMoneyStatisticsInfo.setCreateYmd("开通日期");
        localMoneyStatisticsInfo.setPreEndYmd("上次到期日期");
        localMoneyStatisticsInfo.setNextEndYmd("续费后到期日期");
        localMoneyStatisticsInfo.setPlatformName("业务系统");
        infos.add(localMoneyStatisticsInfo);
        adapter = new ListMoneyStatisticsAdapter(context, infos);
        listMoney.setAdapter(adapter);
        
        listMoney.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                if (StringUtils.isEmpty(infos.get(paramInt - 1).getCustomerId())) return;
                Intent localIntent = new Intent(context, CustomerDetailActivity.class);
                localIntent.putExtra("customerId", infos.get(paramInt - 1).getCustomerId());
                startActivity(localIntent);
            }
        });
        getRenew(TimeUtil.getFirstOrEndDate(0), TimeUtil.getTodayDate(), "");
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
                String str3 = storeId.getText().toString();
                if ((str2.equals("")) || (str1.equals(""))) {
                    Toast.makeText(context, "请输入起止日期", Toast.LENGTH_LONG).show();
                    return;
                }
                getRenew(str1, str2, str3);
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

   /* protected void setGatherSpinner() {
        String[] arrayOfString = getResources().getStringArray(2131427328);
        spinnerAdapter = new ArrayAdapter(context, 2130903083, arrayOfString);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editGather.setAdapter(spinnerAdapter);
        editGather.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
            }

            public void onNothingSelected(AdapterView<?> paramAdapterView) {
            }
        });
    }*/
}
