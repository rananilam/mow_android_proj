package libraries.image.loader;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class PicassoLoader implements ILoader
{

    private Picasso picasso = null;
    private RequestCreator requestCreator = null;

    @Override
    public void load(ImageLoaderRequest imageLoaderRequest) {
        picasso = Picasso.get();

        if(imageLoaderRequest.getResourceId()!=0)
        {
            requestCreator = picasso.load(imageLoaderRequest.getResourceId());
        }
        if(imageLoaderRequest.getUrl()!=null)
        {
            requestCreator = picasso.load(imageLoaderRequest.getUrl());
        }

        if(imageLoaderRequest.getPlaceHolder()!=0)
        {
            requestCreator.placeholder(imageLoaderRequest.getPlaceHolder());
        }

        if(imageLoaderRequest.getErrorHolder()!=0)
        {
            requestCreator.error(imageLoaderRequest.getErrorHolder());
        }

        if(imageLoaderRequest.isCenterCrop())
        {
            requestCreator.centerCrop();
        }
        if(imageLoaderRequest.getWidth()!=0 && imageLoaderRequest.getHeight()!=0)
        {
            requestCreator.resize(imageLoaderRequest.getWidth(),
                    imageLoaderRequest.getHeight());
        }

        if(imageLoaderRequest.getImageView()!=null)
        {
            requestCreator.into(imageLoaderRequest.getImageView());
        }
    }
}
