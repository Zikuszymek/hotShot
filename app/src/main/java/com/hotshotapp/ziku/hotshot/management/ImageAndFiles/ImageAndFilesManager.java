package com.hotshotapp.ziku.hotshot.management.ImageAndFiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hotshotapp.ziku.hotshot.jsonservices.KeyItems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ziku on 2017-02-22.
 */

public class ImageAndFilesManager {

    private Context context;

    public ImageAndFilesManager(Context context) {
        this.context = context;
    }

    public void downloadImageForNewHotShotAndSaveIt(String webSiteName, String hotShotId, String imageUrl){
        String imageName = createImageName(webSiteName,hotShotId);
        deleteImage(imageName);
        Bitmap bitmap = downloadImage(imageUrl);
        if(bitmap!=null){
            saveBitmapToFile(bitmap,imageName);
        }
    }

    public String createImageName(String webSiteName, String hotShotId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(webSiteName);
        stringBuilder.append(hotShotId);
        return stringBuilder.toString();
    }

    public void deleteImage(String imgName){
        String fileName = imgName + ".png";
        File file = context.getFileStreamPath(fileName);
        if(file.exists()){
            file.delete();
        }
    }

    public void saveBitmapToFile(Bitmap bitmap, String fileName){
        long MAX_BITMAT_SIZE = 1_000_000;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(fileName + ".png" , Context.MODE_PRIVATE);
//            if(bitmap.getByteCount() > (int) MAX_BITMAT_SIZE){
//                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), true);
//            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap downloadImage(String url){
        Bitmap bitmap = null;
        if(url != null && !url.equals(KeyItems.EMPTY)) {
            try {
                InputStream inputStream = (InputStream) new URL(url).getContent();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public boolean checkIfHotShotHaveImageDownloaded(String webSiteName, String hotShotId){
        String imageFileName = createImageName(webSiteName,hotShotId) + ".png";
        if(doesFileExists(imageFileName)){
            return true;
        }
        return  false;
    }

    public boolean doesFileExists(String fileName){
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }
}
