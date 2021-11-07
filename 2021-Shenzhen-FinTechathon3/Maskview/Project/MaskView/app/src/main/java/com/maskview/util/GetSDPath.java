package com.maskview.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据API的不同获取SDCard路径
 */

public class GetSDPath {
    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= 29) {
                //Android10之后
                sdDir = context.getExternalFilesDir(null);
            } else {
                sdDir = Environment.getExternalStorageDirectory();// 获取SD卡根目录
            }
        } else {
            sdDir = Environment.getRootDirectory();// 获取根目录
        }
        return sdDir.toString();
    }

    // 保存图片到共享目录, 根据file获取私有目录的图片, 然后复制到共享目录DCIM, 成功后删除私有目录的图片
    public static void scanFile(File file, final Context context) {
        String mimeType = getMimeType(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String fileName = file.getName();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            //values.put(MediaStore.Files.FileColumns.TITLE, fileName);
            //values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            /*ContentResolver contentResolver = context.getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
            Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI; //内部存储的Download路径
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = contentResolver.insert(external, values); //使用ContentResolver创建需要操作的文件
            if (uri == null) {
                Log.e("-----", "图片保存失败: ");
                return;
            }
            try {
                OutputStream out = contentResolver.openOutputStream(uri);
                FileInputStream fis = new FileInputStream(file);
                FileUtils.copy(fis, out);
                fis.close();
                out.close();
                Log.e("-----", "图片保存共享目录成功: ");
                // 删除私有目录下的图片
                boolean qq = file.delete();
                if (qq) {
                    Log.e("-----", "私有目录删除成功: ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getMimeType(File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file.getName());
    }

    // 访问相册图片,Android10以上,获取图片信息(Uri,Path,Name,ID,Date)
    public static List<API29ImgBean> getAPI29ImgInfo(Context context, String filename) {
        List<API29ImgBean> beanList = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media._ID,  //图片ID
                MediaStore.Images.Media.DATA, //图片路径
                MediaStore.Images.Media.DATE_ADDED, //日期
                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.TITLE  //图片名称
        };
        API29ImgBean bean; // = new API29ImgBean();
        ContentResolver resolver = context.getContentResolver();
        // 根据日期降序查询
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        // 模糊查询 like 'con-手机号-%'
        String selection = MediaStore.Images.Media.TITLE + " like '" + filename + "%'";
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            // 媒体数据库中查询到的文件id
            int columnId = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            do {
                bean = new API29ImgBean();
                // 通过mediaId获取它的uri
                int mediaId = cursor.getInt(columnId);
                // 获取图片路径
                String tPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                // 获取图片Uri
                Uri itemUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + mediaId);
                // 获取图片名称
                String imgName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                // 获取图片日期
                long imgDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                bean.setImgID(mediaId);
                bean.setImgName(imgName);
                bean.setImgPath(tPath);
                bean.setImgUri(itemUri);
                bean.setImgDate(imgDate);
                beanList.add(bean);
            } while (cursor.moveToNext());
        }
        return beanList;
    }

    // 访问相册图片,Android10以上
    public static List<InputStream> getImageFile(Context context, String filename) {
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,  // 图片路径
                MediaStore.Images.Media.DATE_ADDED,  // 创建日期
                MediaStore.Images.Thumbnails.DATA
        };
        List<InputStream> insList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        // 根据日期降序查询
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        // 模糊查询 like 'con-手机号-%'
        String selection = MediaStore.Images.Media.TITLE + " like '" + filename + "%'";
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            // 媒体数据库中查询到的文件id
            int columnId = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            do {
                // 通过mediaId获取它的uri
                int mediaId = cursor.getInt(columnId);
                // String tPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)); //获取图片路径
                Uri itemUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + mediaId);
                try {
                    // 通过uri获取到inputStream
                    ContentResolver cr = context.getContentResolver();
                    InputStream ins = cr.openInputStream(itemUri);
                    insList.add(ins);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return insList;
    }

    // 根据相册的Uri,拷贝图片到私有目录,并返回绝对路径---Android10及以上
    public static String getPrivatePath(Context context, Uri uri, String imgName) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            File file = compressImage(context, bitmap, imgName);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // ---Android10及以上
    private static File compressImage(Context context, Bitmap bitmap, String imgName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final File[] dirs = context.getExternalFilesDirs("Documents");
        File primaryDir = null;
        if (dirs != null && dirs.length > 0) {
            primaryDir = dirs[0];
        }
        File file = new File(primaryDir.getAbsolutePath(), imgName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        return file;
    }
}
