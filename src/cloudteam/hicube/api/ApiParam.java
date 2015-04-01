package cloudteam.hicube.api;


import cloudteam.hicube.AppConfig;
import cloudteam.hicube.bean.TaskInfo;

public class ApiParam {

    private static String CACHE_KEY = "yunti_";

    public static TaskInfo Login(String userName, String password) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.cacheOpened = false;
        taskInfo.addParam("userName", userName);
        taskInfo.addParam("password", password);
        taskInfo.addParam("platform", "YUNTI");
        taskInfo.addParam("platformVersion", "0.1");
        taskInfo.addParam("appVersion", "1.0");
        taskInfo.setRequestAction(AppConfig.LOGIN_URL);
        return taskInfo;
    }



    /*public static Part[] customerAdd(CustomerInfo info, String paramString) {
        Part[] arrayOfPart = new Part[18];
        arrayOfPart[0] = new StringPart(AppConfig.CUSTOMER_ADD, "uri", "UTF-8");
        arrayOfPart[1] = new StringPart("categoryId", info.getCategoryId(), "UTF-8");
        arrayOfPart[2] = new StringPart("status", info.getStatus(), "UTF-8");
        arrayOfPart[3] = new StringPart("name", info.getName(), "UTF-8");
        arrayOfPart[4] = new StringPart("bossName", info.getBossName(), "UTF-8");
        arrayOfPart[5] = new StringPart("bossPhone", info.getBossPhone() + "", "UTF-8");
        arrayOfPart[6] = new StringPart("handline", info.getHandline() + "", "UTF-8");
        arrayOfPart[7] = new StringPart("lat", info.getLat() + "", "UTF-8");
        arrayOfPart[8] = new StringPart("lng", info.getLng() + "", "UTF-8");
        arrayOfPart[9] = new StringPart("districtCode", info.getDistrictCode() + "", "UTF-8");
        arrayOfPart[10] = new StringPart("address", info.getAddress(), "UTF-8");
        arrayOfPart[11] = new StringPart("nextTrackYmd", info.getNextTrackYmd() + "", "UTF-8");
        arrayOfPart[12] = new StringPart("remark", info.getRemark(), "UTF-8");
        arrayOfPart[13] = new StringPart("description", info.getRecord(), "UTF-8");
        arrayOfPart[14] = new StringPart("token", paramString, "UTF-8");
        arrayOfPart[15] = new StringPart("pic01", "", "UTF-8");
        arrayOfPart[16] = new StringPart("pic02", "", "UTF-8");
        arrayOfPart[17] = new StringPart("pic03", "", "UTF-8");

        if (!StringUtils.isEmpty(info.getPic01())) {
            try {
                arrayOfPart[15] = new FilePart("pic01", new File(info.getPic01()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(info.getPic02())) {
            try {
                arrayOfPart[16] = new FilePart("pic02", new File(info.getPic02()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(info.getPic03())) {
            try {
                arrayOfPart[17] = new FilePart("pic03", new File(info.getPic03()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return arrayOfPart;
    }*/


}
