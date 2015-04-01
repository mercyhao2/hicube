package cloudteam.hicube.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import java.io.*;

public class CameraUtils {

    /**
     * 调用android自带裁剪图片功能模块
     *
     * @param c
     * @param path
     *            需要裁减的源图片路径
     * @param aspectX
     *            裁剪框宽度
     * @param aspectY
     *            裁剪框高度
     * @param outx
     *            输出的图片的宽高
     * @param outy
     */
    public static void cropPic(Activity c, String path, int aspectX,
                               int aspectY, int outx, int outy, int TAG) {
        try {
            File mfile = new File(path);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(mfile), "image/*");
            intent.putExtra("crop", "true");
            // 裁剪框的比例，1：1
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            // 设置输出的大小
            intent.putExtra("outputX", outx);
            intent.putExtra("outputY", outy);
            intent.putExtra("scale", true); // 设置是否允许拉伸
            // 如果要在给定的uri中获取图片，则必须设置为false，如果设置为true，那么便不会在给定的uri中获取到裁剪的图片
            intent.putExtra("return-data", true);
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.PNG.toString());// 设置输出格式
            intent.putExtra("noFaceDetection", true); // 无需人脸识别 默认不需要设置
            c.startActivityForResult(intent, TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void openCamera(Activity paramActivity, Uri paramUri, int paramInt) {
        try {
            Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            localIntent.putExtra("output", paramUri);
            localIntent.putExtra("return-data", true);
            paramActivity.startActivityForResult(localIntent, paramInt);
            return;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void openGallery(Activity paramActivity, Uri paramUri, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        try {
            Intent localIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            localIntent.setDataAndType(paramUri, "image/*");
            localIntent.putExtra("crop", "true");
            localIntent.putExtra("aspectX", paramInt1);
            localIntent.putExtra("aspectY", paramInt2);
            localIntent.putExtra("outputX", paramInt3);
            localIntent.putExtra("outputY", paramInt4);
            localIntent.putExtra("output", paramUri);
            localIntent.putExtra("scale", true);
            localIntent.putExtra("return-data", false);
            localIntent.putExtra("outputFormat", CompressFormat.PNG.toString());
            localIntent.putExtra("noFaceDetection", true);
            paramActivity.startActivityForResult(localIntent, paramInt5);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static Bitmap getBitmapByUri(Context paramContext, Uri paramUri) {
        try {
            Bitmap localBitmap = BitmapFactory.decodeStream(paramContext.getContentResolver().openInputStream(paramUri));
            return localBitmap;
        } catch (FileNotFoundException localFileNotFoundException) {
            localFileNotFoundException.printStackTrace();
        }
        return null;
    }


    public static Bitmap compress(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩
     */
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //图片压缩
    private Bitmap compBitmap(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }


    public static String getPicPath(Context paramContext, Intent paramIntent) {
        if (paramIntent != null) {
            Uri localUri = paramIntent.getData();
            String[] arrayOfString = {"_data"};
            try {
                Cursor localCursor = paramContext.getContentResolver().query(localUri, arrayOfString, null, null, null);
                localCursor.moveToFirst();
                String str = localCursor.getString(localCursor.getColumnIndex(arrayOfString[0]));
                localCursor.close();
                return str;
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        return "";
    }

    public static Bitmap getbitmap(Intent paramIntent) {
        if (paramIntent != null) {
            Bundle localBundle = paramIntent.getExtras();
            if (localBundle != null)
                return ((Bitmap) localBundle.getParcelable("data"));
        }
        return null;
    }

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param path
     */
    public static void savePhotoToSDCard(File path, Bitmap photoBitmap) {
        if (checkSDCardAvailable()) {
            /*File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }

            File photoFile = new File(path);*/
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(path);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
//						fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                path.delete();
                e.printStackTrace();
            } catch (IOException e) {
                path.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param path
     */
    public static void savePhotoToSDCardByPath(String path, Bitmap photoBitmap) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }

            File photoFile = new File(path);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
//						fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
