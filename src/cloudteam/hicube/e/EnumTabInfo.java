package cloudteam.hicube.e;

import cloudteam.hicube.R;
import cloudteam.hicube.fragment.MainFragment;
import cloudteam.hicube.fragment.StoreFragment;
import cloudteam.hicube.fragment.MineFragment;

public enum EnumTabInfo {

    主页(0, "主页", MineFragment.class,
            R.id.tab1, R.drawable.tab_icon_main, R.drawable.tab_icon_main_press),
    商城(1, "商城", StoreFragment.class,
            R.id.tab2, R.drawable.tab_icon_order, R.drawable.tab_icon_order_press),
    我的(2, "我的", MainFragment.class,
            R.id.tab3, R.drawable.tab_icon_mine, R.drawable.tab_icon_mine_press);

    private int index;
    private String tag;
    private Class<?> clss;
    private int contentViewId;
    private int iconId;
    private int iconPressId;

    private EnumTabInfo(int index, String tag, Class<?> clss,
                        int contentViewId, int iconId, int iconPressId) {
        this.index = index;
        this.tag = tag;
        this.clss = clss;
        this.contentViewId = contentViewId;
        this.iconId = iconId;
        this.iconPressId = iconPressId;
    }


    public static String[] getNames() {
        EnumTabInfo[] arrayOfEnumTabInfo = values();
        String[] arrayOfString = new String[arrayOfEnumTabInfo.length];
        for (int i = 0; i < arrayOfEnumTabInfo.length; ++i)
            arrayOfString[i] = arrayOfEnumTabInfo[i].toString();
        return null;
    }

    public static EnumTabInfo getTabInfoByIndex(int paramInt) {
        EnumTabInfo[] arrayOfEnumTabInfo = values();
        for (int i = 0; i < arrayOfEnumTabInfo.length; ++i)
            if (paramInt == arrayOfEnumTabInfo[i].index)
                return arrayOfEnumTabInfo[i];
        return null;
    }

    public static EnumTabInfo getTabInfoByTag(String paramString) {
        EnumTabInfo[] arrayOfEnumTabInfo = values();
        for (int i = 0; i < arrayOfEnumTabInfo.length; ++i)
            if (paramString.equals(arrayOfEnumTabInfo[i].tag))
                return arrayOfEnumTabInfo[i];
        return null;
    }

    public static String[] getTags() {
        EnumTabInfo[] arrayOfEnumTabInfo = values();
        String[] arrayOfString = new String[arrayOfEnumTabInfo.length];
        for (int i = 0; i < arrayOfEnumTabInfo.length; ++i)
            arrayOfString[i] = arrayOfEnumTabInfo[i].getTag();
        return null;
    }

    public Class<?> getClss() {
        return this.clss;
    }

    public int getContentViewId() {
        return this.contentViewId;
    }

    public int getIconId() {
        return this.iconId;
    }

    public int getIconPressId() {
        return this.iconPressId;
    }

    public int getIndex() {
        return this.index;
    }

    public String getTag() {
        return this.tag;
    }

    public void setClss(Class<?> paramClass) {
        this.clss = paramClass;
    }

    public void setContentViewId(int paramInt) {
        this.contentViewId = paramInt;
    }

    public void setIconId(int paramInt) {
        this.iconId = paramInt;
    }

    public void setIconPressId(int paramInt) {
        this.iconPressId = paramInt;
    }

    public void setIndex(int paramInt) {
        this.index = paramInt;
    }

    public void setTag(String paramString) {
        this.tag = paramString;
    }
}
