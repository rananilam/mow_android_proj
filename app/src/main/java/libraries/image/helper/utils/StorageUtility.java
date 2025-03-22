package libraries.image.helper.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import com.application.BaseApplication;
import com.mow.cash.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nikul on 6/1/2016.
 */
public class StorageUtility {

    private static StorageUtility storageUtility = null;

    private String rootDir = BaseApplication.appContext.getExternalCacheDir().getPath() + File.separator + getAppName();
    private String mediaDir = rootDir + File.separator + "Media";


    private String documentDir = rootDir + File.separator + "Document";

    private String mediaPhotosDir = mediaDir + File.separator + "Photos";
    private String mediaVideosDir = mediaDir + File.separator + "Videos";
    private String thumbVideosDir = mediaVideosDir + File.separator + "Thumb";
    public static StorageUtility getInstance()
    {
        if(storageUtility == null)
        {
            storageUtility = new StorageUtility();
        }
        return storageUtility;
    }

    private StorageUtility()
    {
        createDirs();
    }
    public String getRootDir()
    {
        return rootDir;
    }
    public String getMediaDir()
    {
        return mediaDir;
    }

    public String getDocumentDir()
    {
        return documentDir;
    }

    public String getPhotosDir()
    {
        return mediaPhotosDir;
    }
    public String getVideosDir()
    {
        return mediaVideosDir;
    }


    public void createDirs() {


        File rootDr = new File(rootDir);
        File mediaDr = new File(mediaDir);
        File documentDr = new File(documentDir);
        File photosDir = new File(mediaPhotosDir);
        File videosDir = new File(mediaVideosDir);
        File thumbDir = new File(thumbVideosDir);

        rootDr.setExecutable(true);
        rootDr.setWritable(true);
        rootDr.setReadable(true);
        rootDr.mkdirs();

        mediaDr.setExecutable(true);
        mediaDr.setWritable(true);
        mediaDr.setReadable(true);
        mediaDr.mkdirs();

        documentDr.setExecutable(true);
        documentDr.setWritable(true);
        documentDr.setReadable(true);
        documentDr.mkdirs();

        photosDir.setExecutable(true);
        photosDir.setWritable(true);
        photosDir.setReadable(true);
        photosDir.mkdirs();

        videosDir.setExecutable(true);
        videosDir.setWritable(true);
        videosDir.setReadable(true);
        videosDir.mkdirs();

        thumbDir.setExecutable(true);
        thumbDir.setWritable(true);
        thumbDir.setReadable(true);
        thumbDir.mkdirs();
    }

    public String getFileNameWithoutExtension(String fileName)
    {
        if(fileName!=null && !fileName.isEmpty() && fileName.contains("."))
        {
            String fileNameSplit[] = fileName.split("\\.");
            if(fileNameSplit!=null && fileNameSplit.length>=1)
            {
                return fileNameSplit[0];
            }
        }
        return "";
    }
    public String getFileNameWithExtenstion(String url)
    {
        if(url!=null && !url.isEmpty() && url.contains("."))
        {
            String[] splitUrl = url.split("/");
            if(splitUrl!=null && splitUrl.length>=1)
            {
                return splitUrl[splitUrl.length-1];
            }
        }
        return null;
    }

    public String getFileExtension(String url)
    {
        if(url!=null && !url.isEmpty() && url.contains("."))
        {
            String fileNameWithExtension = getFileNameWithExtenstion(url);
            if(fileNameWithExtension!=null && !fileNameWithExtension.isEmpty() && fileNameWithExtension.contains("."))
            {
                String fileNameSplit[] = fileNameWithExtension.split("\\.");
                if(fileNameSplit!=null && fileNameSplit.length>=2)
                {
                    return fileNameSplit[1];
                }
            }
        }

        return "";
    }
    public String saveBitmap(Bitmap bitmap)
    {
        FileOutputStream out = null;
        String filePath = thumbVideosDir + File.separator + System.currentTimeMillis()+".jpg";
        try {
            out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private String getAppName()
    {
        return BaseApplication.appContext.getString(R.string.app_name).trim().replace(" ","");
    }

    public File createFile(String filePath)
    {
        File imageFile = null;
        try
        {
            imageFile = new File(filePath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return imageFile;
    }

}
