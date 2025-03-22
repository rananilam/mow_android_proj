package libraries.image.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;

/**
 * Created by admin on 12/25/2017.
 */

public class ImageLoaderRequestBuilder {

    private enum ImageLoaderOption
    {
        PICASSO, GLIDE
    }

    private ImageLoaderOption imageLoaderOption = ImageLoaderOption.GLIDE;

    private ImageLoaderRequest request = null;

    public ImageLoaderRequestBuilder(Context context)
    {
        this.request = new ImageLoaderRequest();
        this.request.setContext(context);
    }
    public ImageLoaderRequestBuilder load(int resourceId)
    {
        this.request.setResourceId(resourceId);
        return this;
    }
    public ImageLoaderRequestBuilder load(String url)
    {
        this.request.setUrl(url);
        return this;
    }

    public ImageLoaderRequestBuilder placeHolder(int placeHolder)
    {
        this.request.setPlaceHolder(placeHolder);
        return this;
    }

    public ImageLoaderRequestBuilder errorHolder(int errorHolder)
    {
        this.request.setErrorHolder(errorHolder);
        return this;
    }

    public ImageLoaderRequestBuilder centerCrop()
    {
        this.request.setCenterCrop(true);
        return this;
    }
    public ImageLoaderRequestBuilder resize(int width, int height)
    {
        this.request.setWidth(width);
        this.request.setHeight(height);
        return this;
    }

    public < E > ImageLoaderRequestBuilder addTransformation(E transformation)
    {
        switch (imageLoaderOption)
        {
            case GLIDE:
                this.request.addTransformation((Transformation) transformation);
                break;
            case PICASSO:
                break;
        }
        return this;
    }

    public void into(ImageView imageView)
    {
        this.request.setImageView(imageView);

        if(imageLoaderOption == ImageLoaderOption.PICASSO)
        {
            new PicassoLoader().load(this.request);
        }
        else
        {
            new GlideLoader().load(this.request);
        }
    }

}
