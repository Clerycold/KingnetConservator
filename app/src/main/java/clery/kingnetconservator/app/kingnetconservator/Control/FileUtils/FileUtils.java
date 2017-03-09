package clery.kingnetconservator.app.kingnetconservator.Control.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import clery.kingnetconservator.app.kingnetconservator.Control.MediaScanner;
import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;

/**
 * Created by clery on 2017/2/23.
 */

public class FileUtils {

    private static String TAG = "Kingnet_picture";

    private static FileUtils mInstance;
    private String pictureUrl;
    private Context context;

    public static void initManager(Context context){
        if(mInstance == null){
            mInstance = new FileUtils(context);
        }
    }

    public static FileUtils getManager(){
        return mInstance;
    }

    private FileUtils(Context context){
        this.context = context;
    }

    public void selectDeleteFiled(String filename){

        deleteFile(new File(filename));
    }

    /**
     * 判斷資料夾是否存在後，進行掃描
     */

    public void GalleryAddPic(){

        File pictureFile =  new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), TAG);
        //      判断SDK版本是不是4.4或者高于4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScanner mediaScanner = new MediaScanner(context);
            mediaScanner.scanFile(new File(pictureFile.getPath()), null);
        } else {
            if (pictureFile.isDirectory()) {    //// 判断e盘是否是目录
                pictureFile = new File(pictureFile.getPath());
                Uri contentUri = Uri.fromFile(pictureFile);
                Intent allmediaScanIntent = new Intent(Intent.ACTION_MEDIA_MOUNTED, contentUri);
                context.sendBroadcast(allmediaScanIntent);
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(pictureFile.getPath())));
                context.sendBroadcast(mediaScanIntent);
            }
        }
    }

    /**
     * 製作資料夾
     * 檔案路徑
     * @return
     */
    public File getDataFilePath(){
        File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), TAG);

        if (!pictureFile.exists()) {
            RemindToast.showText(context,"建立資料夾Kingnet_picture");

            if (!pictureFile.mkdirs()) {
                RemindToast.showText(context,"建立資料夾失敗，請確認是否有儲存空間");
                return null;
            }
        }
        String TIMESTAMP= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        pictureUrl = pictureFile.getPath() + File.separator +
                "IMG_" + TIMESTAMP + ".jpg" ;

        return new File(pictureUrl);
    }

    public String getPictureUrl(){
        return pictureUrl;
    }

    private void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                // 设置属性:让文件可执行，可读，可写
                file.setExecutable(true, false);
                file.setReadable(true, false);
                file.setWritable(true, false);
                file.delete(); // delete()方法
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.setExecutable(true, false);
            file.setReadable(true, false);
            file.setWritable(true, false);
            file.delete();
            Log.i("deleteFile", file.getName() + "成功删除！！");
        } else {
            Log.i("deleteFile", file.getName() + "不存在！！！");
        }
    }
}
