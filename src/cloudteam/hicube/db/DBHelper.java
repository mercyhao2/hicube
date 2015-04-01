package cloudteam.hicube.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import net.tsz.afinal.FinalDb;


public class DBHelper implements FinalDb.DbUpdateListener {

    private int Version = 2;
    FinalDb db;

    public DBHelper(Context context) {
        db   = FinalDb.create(context, "yunti", true, Version, this);
    }


    /*public void delAll() {
        if (db.findAll(CustomerInfo.class).size() <= 0)
            return;
        db.deleteAll(CustomerInfo.class);
    }*/

    /*public List<CityInfo> getCityListByProvince(int paramInt) {
        List localList = db.findAllByWhere(CityInfo.class, "provinceCode = " + paramInt);
        if (localList.size() > 0)
            return localList;
        return new ArrayList();
    }*/

/*
    public void saveCity(CityInfo paramCityInfo) {
        if (db.findAllByWhere(CityInfo.class, "code =" + paramCityInfo.getCode()).size() != 0)
            return;
        db.save(paramCityInfo);
    }*/



   /* public boolean upDateCollete(CustomerInfo paramCustomerInfo) {
        List localList = db.findAllByWhere(CustomerInfo.class, "id = '" + paramCustomerInfo.getId() + "'");
        if (localList.size() > 0) {
            if (paramCustomerInfo.getCreateTime() > ((CustomerInfo) localList.get(0)).getCreateTime()) {
                updateCustomer(paramCustomerInfo);
                return true;
            }
            return false;
        }
        return false;
    }*/

    public void onUpgrade(SQLiteDatabase database, int paramInt1, int paramInt2) {
        database.beginTransaction();
        try {
            upgradeDatabaseToVersion1(database);
            database.endTransaction();
            Log.i("1234", "0000=" + paramInt1);
            return;
        } catch (Throwable localThrowable) {
            Log.e("DatabaseHelper", localThrowable.getMessage(), localThrowable);
        } finally {
            database.endTransaction();
        }
    }

    private void upgradeDatabaseToVersion1(SQLiteDatabase database) {
        String[] arrayOfString = {"CustomerInfo", "OrgInfo", "PlatformOpenRecordInfo", "PlatformInfo"};
        for (int i = 0; i < arrayOfString.length; ++i) {
            String str = arrayOfString[i] + "_temp";
            database.execSQL("drop table if exists " + str);
            database.execSQL("ALTER TABLE if exists " + arrayOfString[i] + " RENAME TO " + str);
        }
        database.setTransactionSuccessful();
    }
}
