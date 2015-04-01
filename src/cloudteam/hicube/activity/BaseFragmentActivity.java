package cloudteam.hicube.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import net.tsz.afinal.FinalActivity;

public class BaseFragmentActivity extends FragmentActivity {

	protected Context context;

	private String currentTag;// 当前tag


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置竖屏
		context = this;

	/*	// 注册结束activity广播监听
		finishReceiver = new FinishReceiver(this);
		finishReceiver.register(context, FinishReceiver.ACTION_FINISHACTIVITY);*/

		initData();
		initView();
		initListener();
	}

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		FinalActivity.initInjectedView(this);
	}

	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		FinalActivity.initInjectedView(this);
	}

	public void setContentView(View view) {
		super.setContentView(view);
		FinalActivity.initInjectedView(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	//	finishReceiver.unregister(context);
	}


	/**
	 * 初始化数据
	 */
	protected void initData() {
	}

	/**
	 * 初始化控件显示内容
	 */
	protected void initView() {

	}

	/**
	 * 初始化FinalActivity未支持的监听事件
	 */
	protected void initListener() {
	}

	/**
	 * 点击事件，调用（需在@ViewJect里设置）
	 * 
	 * @param v
	 */
	public void onFClick(View v) {
	}

	/**
	 * 子项点击事件，调用（需在@ViewJect里设置）
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void onFItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	/**
	 * 取消接收结束Activity广播
	 *//*
	protected void cancelFinishReciver() {
		finishReceiver.unregister(context);
	}*/

	public String getCurrentTag() {
		return currentTag;
	}

	public void setCurrentTag(String currentTag) {
		this.currentTag = currentTag;
	}

}
