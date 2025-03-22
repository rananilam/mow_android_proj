package libraries.image.loader;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class GlideLoader implements ILoader
{

    @SuppressLint("CheckResult")
    @Override
    public void load(ImageLoaderRequest imageLoaderRequest) {



        RequestManager requestManager = Glide.with(imageLoaderRequest.getContext());
        RequestBuilder<Drawable> requestBuilder = null;

        if(imageLoaderRequest.getResourceId()!=0)
        {
            requestBuilder = requestManager.load(imageLoaderRequest.getResourceId());
        }
        if(imageLoaderRequest.getUrl()!=null)
        {
            requestBuilder = requestManager.load(imageLoaderRequest.getUrl());
        }


        if(requestBuilder!=null)
        {
            RequestOptions requestOptions = new RequestOptions();
            if(imageLoaderRequest.getPlaceHolder()!=0)
                requestOptions.placeholder(imageLoaderRequest.getPlaceHolder());

            if(imageLoaderRequest.getErrorHolder()!=0)
                requestOptions.error(imageLoaderRequest.getErrorHolder());

            if(imageLoaderRequest.isCenterCrop())
                requestOptions.centerCrop();

            if(imageLoaderRequest.getWidth()!=0 && imageLoaderRequest.getHeight()!=0)
                requestOptions.override(imageLoaderRequest.getWidth(),
                        imageLoaderRequest.getHeight());


            if(imageLoaderRequest.getTransformations()!=null && !imageLoaderRequest.getTransformations().isEmpty())
            {
                Transformation[] transformations = new Transformation[imageLoaderRequest.getTransformations().size()];
                transformations = imageLoaderRequest.getTransformations().toArray(transformations);

                requestOptions.transform(transformations);
            }

            if(imageLoaderRequest.getImageView()!=null)
            {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                requestBuilder.apply(requestOptions).into(imageLoaderRequest.getImageView());
            }
        }
    }
}
