package libraries.image.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;

import java.util.ArrayList;
import java.util.List;

public class ImageLoaderRequest {

    private Context context;
    private int resourceId;
    private String url;
    private int placeHolder;
    private int errorHolder;
    private boolean isCenterCrop;
    private int width=512, height=512;
    private ImageView imageView;
    private List<Transformation> transformations = null;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public int getErrorHolder() {
        return errorHolder;
    }

    public void setErrorHolder(int errorHolder) {
        this.errorHolder = errorHolder;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public void setCenterCrop(boolean centerCrop) {
        isCenterCrop = centerCrop;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public List<Transformation> getTransformations() {
        return transformations;
    }

    public void addTransformation(Transformation transformation) {
        if(this.transformations == null)
            this.transformations = new ArrayList<>();

        this.transformations.add(transformation);
    }
}
