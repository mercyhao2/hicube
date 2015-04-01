package cloudteam.hicube.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import cloudteam.hicube.AppConfig;

import java.io.*;
import java.text.DecimalFormat;

/**
 * 文件操作的工具类
 * @author summer
 */
public class FileUtil {
	/**
	 * 保存文件
	 */
	public static boolean writeLocalFile(Context context, String file,
			byte[] msg) throws IOException {
		boolean res = false;

		FileOutputStream stream = context.openFileOutput(file,
				Context.MODE_WORLD_WRITEABLE);
		stream.write(msg);
		stream.flush();
		stream.close();
		res = true;
		return res;
	}
	/**
	 * 读取文件
	 */

	public static byte[] readLocalFile(Context context, String file)
			throws IOException {
		byte[] datas = null;
		if (checkFileIsExists(context, file)) {
			FileInputStream stream = context.openFileInput(file);
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((ch = stream.read()) != -1) {
				out.write(ch);
			}
			datas = out.toByteArray();
			stream.close();
			out.close();
		}
		return datas;
	}

	/**
	 * 从流内容读取文件
	 */
	public static byte[] readStreamFile(InputStream is) throws IOException {
		byte[] datas = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int ch = 0;
		while ((ch = is.read()) != -1) {
			baos.write(ch);
		}
		datas = baos.toByteArray();
		baos.close();
		baos = null;
		is.close();
		is = null;

		return datas;
	}
	/**
	 * 判断文件是否存在
	 */
	public static boolean checkFileIsExists(Context mContext, String fileName) {
		File fileDir = mContext.getFilesDir();
		String sFileName = fileDir.getParent() + File.separator
				+ fileDir.getName() + File.separator + fileName;
		File file = new File(sFileName);
		return file.exists();
	}

	/**
	 * 取得文件最后修改日期
	 */
	public static String getFileDatetime(Context mContext, String fileName) {
		String dt = "";
		File fileDir = mContext.getFilesDir();
		String sFileName = fileDir.getParent() + File.separator
				+ fileDir.getName() + File.separator + fileName;
		File file = new File(sFileName);
		if (file.isFile()) {
			dt = TimeUtil.getTime2String(file.lastModified(), "");
		}
		return dt;
	}

	/**
	 * 判断目录是否存在
	 */

	public static boolean checkDirectoryIsExists(Context mContext,
			String dirName) {
		File fileDir = mContext.getFilesDir();
		String sFileName = fileDir.getParent() + File.separator
				+ fileDir.getName() + File.separator + dirName;
		File file = new File(sFileName);
		return file.isDirectory();
	}

	/**
	 * 创建新的目录
	 */

	public static boolean createDirectory(Context mContext, String dirName) {
		File fileDir = mContext.getFilesDir();
		String sFileName = fileDir.getParent() + File.separator
				+ fileDir.getName() + File.separator + dirName;
		File file = new File(sFileName);
		boolean success = file.mkdir();
		return success;
	}

	public static Bitmap loadBitmapFromFile(Context context, String fileName,
			int defaultImage) {
		Bitmap bm = null;
		String bmpPath = context.getFilesDir() + File.separator
				+ fileName;
		bm = BitmapFactory.decodeFile(bmpPath);
		if (bm == null) {
			bm = BitmapFactory.decodeResource(context.getResources(),
                    defaultImage);
		}
		return bm;
	}

	/**
	 * 检查sd卡是否存在如果存在
	 *
	 * @return 返回sd File卡路径
	 */
	public static File getSDRootPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir;

	}

	/**
	 * 清除文件
	 *
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				for (File files :file.listFiles()) {
					deleteFile(files);
				}
			}
			file.delete();
		}
	}

	/**
	 * 删除小于给定时间的缓存数据
	 * @param dir
	 * @param numDays
	 * @return 清除文件数量
	 */
	public static int clearCacheFolder(File dir, long numDays) {
	    int deletedFiles = 0;
	    if (dir!= null && dir.isDirectory()) {
	        try {
	            for (File child:dir.listFiles()) {
	                if (child.isDirectory()) {
	                    deletedFiles += clearCacheFolder(child, numDays);
	                }
	                if (child.lastModified() < numDays) {
	                    if (child.delete()) {
	                        deletedFiles++;
	                    }
	                }
	            }
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return deletedFiles;
	}

	/**
	 * 获取缓存文件大小
	 * 格式化字符串
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getCacheSize(Context context) {
		File fileCache = context.getCacheDir();
		File temp = new File(FileUtil.getSDRootPath(), AppConfig.CACHE_ROOT);

		long size = 0;
		try {
			size = getFileSize(fileCache);
			size += getFileSize(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FormetFileSize(size);
	}

	/**
	 * 取文件大小
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		if (f.exists()) {
			File flist[] = f.listFiles();
			int length = flist.length;
			for (int i = 0; i < length; i++) {
				if (flist[i].isDirectory()) {
					size += getFileSize(flist[i]);
				} else if(flist[i].isFile()){
					size += flist[i].length();
				}
			}
		}
		return size;
	}

	/**
	 * 格式化文件大小
	 *
	 * @param file
	 * @return
	 */
	public static String FormetFileSize(long file) {
		DecimalFormat df = new DecimalFormat("0.00");
		String fileSizeString = "";
		if (file < 1024) {
			fileSizeString = df.format((double) file) + "B";
		} else if (file < 1048576) {
			fileSizeString = df.format((double) file / 1024) + "KB";
		} else if (file < 1073741824) {
			fileSizeString = df.format((double) file / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) file / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 递归求取目录文件个数
	 *
	 * @param f
	 * @return
	 */
	public static long getFileNum(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		int length = flist.length;
		for (int i = 0; i < length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileNum(flist[i]);
				size--;
			}
		}
		return size;
	}

	/**
	 * 获取本地图片路径
	 * @param cachedir 缓存根目录
	 * @param httpUrl 网络地址
	 * @return 本地缓存文件
	 */
	public static File getLocalFilePath(Context context,String cachedir,String httpUrl){

		String bitmapName =getFileName(httpUrl);
		File cachefile = new File(getCachePath(context,cachedir),bitmapName);
		return cachefile;
	}

	/**
	 * 返回缓存文件目录对象
	 * @param context
	 * @param path 文件目录
	 * @return file sdcard 或cache 目录dir
	 */
	public static File getCachePath(Context context,String path) {
		File picfile = null;
		if (null == FileUtil.getSDRootPath()) {
			picfile = new File(context.getCacheDir() + path);
		} else {
			picfile = new File(FileUtil.getSDRootPath() + path);
		}
		if (!picfile.exists())
			picfile.mkdirs();
		return picfile;
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName( String filePath )
	{
		if( StringUtils.isEmpty(filePath) )	return "";
		return filePath.substring( filePath.lastIndexOf( File.separator )+1 );
	}

	/**
	 * 写图片文件到存储卡中
	 * @param filePath 全路径名
	 * @param bitmap 图片数据
	 * @param quality 压缩比例
	 * @throws java.io.IOException
	 */
	public static void saveImageToStorage(String filePath, Bitmap bitmap, int quality) throws IOException
	{
		if(bitmap != null) {
			File bitmapFile=new File(filePath);
			FileOutputStream fos = new FileOutputStream(bitmapFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes); 			
			fos.close();
		}
	}
}
