package cloudteam.hicube.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import cloudteam.hicube.R;
import cloudteam.hicube.e.EnumTabInfo;
import cloudteam.hicube.fragment.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity  implements OnTabChangeListener{
    private Context context;
    private Intent intent = new Intent();

    @ViewInject(R.id.tabhost)
    private TabHost tabHost;

    @ViewInject(R.id.title)
    private TextView titleTv;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        ViewUtils.inject(this);
        context = this;
      //  initSlidingMenu();
        intent.setAction("cloudteam.hicube.UpLoadService");
       // startService(intent);
        setupTabs();
        setTabByTag(EnumTabInfo.主页.getTag());
    }


    /*private void initSlidingMenu() {
        leftFragment = new LeftFragment();
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindWidthRes(R.dimen.left_menu_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setShadowWidth(10);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        setBehindContentView(R.layout.left_menu_layout);

        slidingMenu.setSecondaryMenu(R.layout.right_menu_layout); // 设置右菜单默认VIEW
        getSupportFragmentManager().beginTransaction().replace(R.id.leftMenu, leftFragment).commit();
    }*/

    /**
     * 通过tag设置当前Tab
     *
     * @param tag
     */
    private void setTabByTag(String tag) {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        BaseFragment fra = (BaseFragment) fragmentManager
                .findFragmentByTag(tag);

        if (fra == null) {

            try {
                fra = (BaseFragment) EnumTabInfo.getTabInfoByTag(tag).getClss()
                        .newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.replace(EnumTabInfo.getTabInfoByTag(tag).getContentViewId(),
                    fra, tag);
            trans.commit();

        } else {
            fra.onFraResume();
        }

        // 设置Tab的item选中
        setTabItemSelectedShow(EnumTabInfo.getTabInfoByTag(tag).getIndex());
    }

    private void setTabItemSelectedShow(int index) {

        for (int i = 0; i < 3; i++) {
            View itemView = tabHost.getTabWidget().getChildAt(i);
            ImageView imgIcon = (ImageView) itemView
                    .findViewById(R.id.tabitem_img_icon);
            TextView txtName = (TextView) itemView
                    .findViewById(R.id.tabitem_txt_name);
            if (i == index) {
                // 选中的item
                imgIcon.setImageResource(EnumTabInfo.getTabInfoByIndex(i)
                        .getIconPressId());
                txtName.setTextColor(getResources().getColor(R.color.white));
                itemView.setBackgroundResource(R.color.main_light_green);
            } else {
                // 选中以外的item
                imgIcon.setImageResource(EnumTabInfo.getTabInfoByIndex(i)
                        .getIconId());
                txtName.setTextColor(getResources().getColor(R.color.main_light_green));
                itemView.setBackgroundResource(R.color.white);
            }
        }

    }

    // 初始化标签按钮
    private void setupTabs() {
        tabHost.setup();
        // 生成底部自定义样式的按钮
        for (int i = 0; i < 3; i++) {
            EnumTabInfo eTabInfo = EnumTabInfo.getTabInfoByIndex(i);
            View itemView = View.inflate(context, R.layout.ll_tab_item, null);
            TextView txtName = (TextView) itemView
                    .findViewById(R.id.tabitem_txt_name);

            txtName.setText(eTabInfo.toString());
            itemView.setBackgroundResource(R.color.white);
            tabHost.addTab(tabHost.newTabSpec(eTabInfo.getTag())
                    .setIndicator(itemView)
                    .setContent(eTabInfo.getContentViewId()));
        }

        tabHost.setOnTabChangedListener(this);
    }


    public void onBackPressed() {
        finish();
    }

   /* @OnClick({R.id.left_button})
    public void onClick(View paramView) {
        switch (paramView.getId()) {

            case R.id.left_button:
                if (!(slidingMenu.isMenuShowing())) {
                    slidingMenu.showMenu();
                    return;
                }
                slidingMenu.showContent();
                break;
        }

    }*/



    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }

    public void onTabChanged(String paramString) {
        titleTv.setText(paramString);
        setTabByTag(paramString);
    }

   /* public void switchContent(int paramInt) {
        switch (paramInt) {
            case 1:
                slidingMenu.showContent();
                break;
        }

    }*/
}

