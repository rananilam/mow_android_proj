package libraries.image.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.mow.cash.android.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import libraries.image.helper.models.MediaResult;
import libraries.image.helper.models.MediaType;
import libraries.image.helper.utils.GalleryUriReader;
import libraries.image.helper.utils.StorageUtility;
import libraries.logger.Logger;


public class ImageHelper {


    private static final int REQUEST_FILE_PICK = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_PICK_IMAGE_FROM_GALLERY = 102;
    private static final int REQUEST_VIDEO_CAPTURE = 103;
    private static final int REQUEST_PICK_VIDEO_FROM_GALLERY = 104;
    private static final int EXTRA_DURATION_LIMIT = 60;
    private static ImageHelper imageHelper = null;
    private Activity activity = null;
    private Fragment fragment = null;
    private boolean crop = false;
    private boolean square = false;
    private int maxWidth = 512;
    private int maxHeight = 512;
    private File sourceFile = null;
    private File destinationFile = null;
    private IImage iImage = null;

    private ImageHelper() {

    }

    public static ImageHelper getInstance() {
        if (imageHelper == null)
            imageHelper = new ImageHelper();

        return imageHelper;
    }

    public ImageHelper with(Activity activity) {
        reset();
        this.activity = activity;
        return imageHelper;
    }

    public ImageHelper with(Fragment fragment) {
        reset();
        this.fragment = fragment;
        return imageHelper;
    }

    public ImageHelper setIImage(IImage iImage) {
        this.iImage = iImage;
        return imageHelper;
    }

    public ImageHelper setFile(File iImage) {
        this.sourceFile = iImage;
        return imageHelper;
    }

    public ImageHelper setCrop(boolean crop) {
        this.crop = crop;
        return imageHelper;
    }

    public ImageHelper setSquare(boolean square) {
        this.square = square;
        return imageHelper;
    }

    public ImageHelper setMaxResultSize(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        return imageHelper;
    }

    public void start(Option option) {
        Log.e("Nilam", "start capturing start function----" + option);
        Intent intent = null;
        switch (option) {
            case OTHER:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");  // Allows all file types
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*", "application/pdf"});
                if (activity != null) {
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    activity.startActivityForResult(intent, REQUEST_FILE_PICK);
                } else {
                    Log.e("Nilam", "start capturing start function---fragment clickedddddd-");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    fragment.startActivityForResult(intent, REQUEST_FILE_PICK);
                }

                break;
            case IMAGE_CAPTURE:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                sourceFile = createFile("" + Calendar.getInstance().getTimeInMillis(), ".jpg", StorageUtility.getInstance().getPhotosDir());
                if (activity != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile(activity, sourceFile));
                    activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile(fragment.getContext(), sourceFile));
                    fragment.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }

                break;
            case IMAGE_PICK:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                if (activity != null) {
                    activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.complete_action_using)), REQUEST_PICK_IMAGE_FROM_GALLERY);
                } else {
                    fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.complete_action_using)), REQUEST_PICK_IMAGE_FROM_GALLERY);


                }
                break;
            case VIDEO_RECORD:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, EXTRA_DURATION_LIMIT);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                sourceFile = createFile("" + Calendar.getInstance().getTimeInMillis(), ".mp4", StorageUtility.getInstance().getVideosDir());
                if (activity != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile(activity, sourceFile));
                    activity.startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile(fragment.getContext(), sourceFile));
                    fragment.startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                }

                break;
            case VIDEO_PICK:
                intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                if (activity != null)
                    activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.complete_action_using)), REQUEST_PICK_VIDEO_FROM_GALLERY);
                else
                    fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.complete_action_using)), REQUEST_PICK_VIDEO_FROM_GALLERY);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        MediaResult mediaResult = null;
        Logger.trace("onActivityResult Image Capture Util");
        Logger.trace(String.valueOf(resultCode));
        Logger.trace(String.valueOf(requestCode));
        Logger.trace(String.valueOf(Activity.RESULT_OK));
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FILE_PICK: {
                    mediaResult = new MediaResult();
                    Uri selectedFileUri = intent.getData();
                    Pair<String, Long> fileInfo  = getFileSizeAndName(selectedFileUri); // Get file size
                    long   fileSizeRequired = 5 * 1024 * 1024;
                    Log.d("FilePicker", "File Size: " + fileInfo.second + " bytes");
                    Log.d("FilePicker", "File Name: " + fileInfo.first );

                    // Check if file size is within limits (e.g., 10MB)
                    if (fileInfo.second > fileSizeRequired) { // 10MB limit
                        Toast.makeText(fragment.getContext(), "File size should be less than 5 MB.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        // Process the file
                        Log.d("FilePicker", "Valid file selected: " + selectedFileUri.toString());
                    }

                    String mimeType = fragment.getActivity().getContentResolver().getType(selectedFileUri);
                    String fileExtension = ".jpg"; // Default to image
                    Log.e("Nilam mimeType -->", mimeType);



                    if (mimeType != null) {
                        mediaResult.setMimeType(mimeType);
                        if (mimeType.equals("image/jpeg")) {
                            fileExtension = ".jpg";
                            mediaResult.setMediaType(MediaType.PHOTO);
                            mediaResult.setResId(R.drawable.ic_mow_img);
                        } else if (mimeType.equals("image/png")) {
                            fileExtension = ".png";
                            mediaResult.setMediaType(MediaType.PHOTO);
                            mediaResult.setResId(R.drawable.ic_mow_img);
                        } else if (mimeType.startsWith("video")) {
                            fileExtension = ".mp4";
                            mediaResult.setResId(R.drawable.ic_mow_video);
                            mediaResult.setMediaType(MediaType.VIDEO);
                        } else if (mimeType.equals("application/pdf")) {
                            mediaResult.setResId(R.drawable.ic_mow_document);
                            fileExtension = ".pdf";
                            mediaResult.setMediaType(MediaType.PDF);
                        } else {
                            mediaResult.setResId(R.drawable.ic_mow_document);
                            fileExtension = ".txt"; // Default for unknown types
                            mediaResult.setMediaType(MediaType.UNKNOWN);
                        }
                    }
                    Log.e("Nilam", selectedFileUri.getPath());


                    // Now sourceFile exists and can be used
                    sourceFile = copyFileFromUri(fragment.getContext(), selectedFileUri,fileInfo.first,fileExtension);
                    mediaResult.setPath(sourceFile.getAbsolutePath());
                    mediaResult.setExtension(StorageUtility.getInstance().getFileExtension(sourceFile.getAbsolutePath()));

                    if (iImage != null)
                        iImage.onImageReady(mediaResult);
                    break;
                }
                case REQUEST_IMAGE_CAPTURE: {

                    if (sourceFile != null) {

                        if (crop) {
                            destinationFile = createFile("" + Calendar.getInstance().getTimeInMillis(), ".jpg", StorageUtility.getInstance().getPhotosDir());
                            startCrop(sourceFile, destinationFile);
                        } else {
                            mediaResult = new MediaResult();
                            mediaResult.setMimeType("image/*");
                            mediaResult.setPath(sourceFile.getAbsolutePath());
                            mediaResult.setExtension(StorageUtility.getInstance().getFileExtension(sourceFile.getAbsolutePath()));
                            mediaResult.setMediaType(MediaType.PHOTO);
                            if (iImage != null)
                                iImage.onImageReady(mediaResult);
                        }
                    } else {
                        Log.e("Nilam", "image not geted");
                    }

                    break;
                }
                case REQUEST_VIDEO_CAPTURE: {

                    if (sourceFile != null) {
                        mediaResult = new MediaResult();
                        mediaResult.setMimeType("video/*");
                        mediaResult.setPath(sourceFile.getAbsolutePath());
                        mediaResult.setExtension(StorageUtility.getInstance().getFileExtension(sourceFile.getAbsolutePath()));

                        String thumbPath = StorageUtility.getInstance().saveBitmap(ThumbnailUtils.createVideoThumbnail(sourceFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND));
                        mediaResult.setThumb(thumbPath);

                        mediaResult.setMediaType(MediaType.VIDEO);
                        if (iImage != null)
                            iImage.onImageReady(mediaResult);
                    }


                    break;
                }
                case REQUEST_PICK_VIDEO_FROM_GALLERY:

                    mediaResult = new MediaResult();
                    mediaResult.setMimeType("video/*");
                    mediaResult.setPath(getImagePath(intent));
                    mediaResult.setExtension(StorageUtility.getInstance().getFileExtension(getImagePath(intent)));
                    mediaResult.setMediaType(MediaType.VIDEO);

                    String thumbPath = StorageUtility.getInstance().saveBitmap(ThumbnailUtils.createVideoThumbnail(getImagePath(intent), MediaStore.Video.Thumbnails.MINI_KIND));
                    mediaResult.setThumb(thumbPath);

                    if (iImage != null)
                        iImage.onImageReady(mediaResult);

                    break;
                case REQUEST_PICK_IMAGE_FROM_GALLERY: {
                    Log.e("Nilam", "REQUEST_PICK_IMAGE_FROM_GALLERY in");
                    if (crop) {
                        Log.e("Nilam", "REQUEST_PICK_IMAGE_FROM_GALLERY in if condition");

                        destinationFile = createFile("" + Calendar.getInstance().getTimeInMillis(), ".jpg", StorageUtility.getInstance().getPhotosDir());
                        startCrop(new File(getImagePath(intent)), destinationFile);
                    } else {
                        Log.e("Nilam", "REQUEST_PICK_IMAGE_FROM_GALLERY in else condition condition");

                        mediaResult = new MediaResult();
                        mediaResult.setMimeType("image/*");
                        mediaResult.setPath(getImagePath(intent));
                        mediaResult.setExtension(StorageUtility.getInstance().getFileExtension(getImagePath(intent)));
                        mediaResult.setMediaType(MediaType.PHOTO);
                        if (iImage != null)
                            iImage.onImageReady(mediaResult);
                    }
                    break;
                }
                case UCrop.REQUEST_CROP: {
                    Logger.trace("Image Crop Success");
                    mediaResult = new MediaResult();
                    mediaResult.setMimeType("image/*");
                    mediaResult.setPath(destinationFile.getAbsolutePath());
                    mediaResult.setExtension(StorageUtility.getInstance().getFileExtension(destinationFile.getAbsolutePath()));
                    mediaResult.setMediaType(MediaType.PHOTO);
                    if (iImage != null)
                        iImage.onImageReady(mediaResult);
                    break;
                }
            }
        }
    }

//    private void copyFileFromUri(Context context, Uri uri_, File destinationFile,String fileName) {
//        // Ensure the destination folder exists
//
//        try (InputStream inputStream = context.getContentResolver().openInputStream(uri_);
//             OutputStream outputStream = new FileOutputStream(destinationFile)) {
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//            Log.d("Nilam", "File copied successfully!");
//        } catch (IOException e) {
//            Log.e("Nilam", "Error copying file: " + e.getMessage());
//        }
//    }

    private File copyFileFromUri(Context context, Uri uri, String fileName, String fileExtension) {
        File destinationFile_ = new File(StorageUtility.getInstance().getPhotosDir(), fileName);
//        File destinationFile_ = createFile(fileName, fileExtension, StorageUtility.getInstance().getPhotosDir());

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(destinationFile_)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            Log.d("Nilam", "File copied successfully to: " + destinationFile_.getAbsolutePath());
            // Return the copied file
            return destinationFile_;
        } catch (IOException e) {
            Log.e("Nilam", "Error copying file: " + e.getMessage());
            return null;
        }

    }


    private File createFile(String fileName, String extension, String dir) {
        File imageFile = null;
        try {
            imageFile = File.createTempFile(fileName, extension, new File(dir));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getImagePath(Intent intent) {
        if (intent.getData().getScheme().equals("file")) {
            return intent.getData().getPath();
        } else if (intent.getData().getScheme().equals("content")) {
            if (activity != null)
                return GalleryUriReader.getPath(activity, intent.getData());
            else
                return GalleryUriReader.getPath(fragment.getContext(), intent.getData());
        } else {
            return intent.getData().toString();
        }
    }

    private void startCrop(File sourceFile, File destinationFile) {
        Log.e("Nilam", "startCrop -------- >>>>>");

        UCrop uCrop = UCrop.of(Uri.fromFile(sourceFile), Uri.fromFile(destinationFile))
                .withMaxResultSize(maxWidth, maxHeight);

        if (square)
            uCrop.withAspectRatio(1, 1);

        Log.i("Nilam", "isAdded: " + fragment.isAdded() + "," + fragment.isDetached());
        if (activity != null)
            uCrop.start(activity);
        else
            uCrop.start(fragment.requireContext(), fragment);
    }

    private Uri getUriFromFile(Context context, File file) {
        return FileProvider.getUriForFile(context,
                "com.mow.cash.android.fileprovider", file);
    }

    private void reset() {
        crop = false;
        square = false;
        maxWidth = 512;
        maxHeight = 512;
        sourceFile = null;
        destinationFile = null;
    }

    public enum Option {
        IMAGE_CAPTURE, IMAGE_PICK, VIDEO_RECORD, VIDEO_PICK, OTHER
    }

    public interface IImage {
        void onImageReady(MediaResult mediaResult);
    }
    private Pair<String, Long> getFileSizeAndName(Uri uri) {
        String fileName = null;
        long fileSize = 0;

        Cursor cursor = fragment.getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

            if (cursor.moveToFirst()) {
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
                if (sizeIndex != -1) {
                    fileSize = cursor.getLong(sizeIndex);
                }
            }
            cursor.close();
        }

        if (fileName == null) {
            fileName = uri.getPath();
            int cut = fileName.lastIndexOf('/');
            if (cut != -1) {
                fileName = fileName.substring(cut + 1);
            }
        }

        return new Pair<>(fileName, fileSize);
    }


}

