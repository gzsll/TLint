package com.gzsll.hupu.components.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;

import org.apache.log4j.Logger;

/**
 * Created by sll on 2016/3/28.
 */
public class GifDrawableImageViewTarget extends ImageViewTarget<GifDrawable> {
    Logger logger = Logger.getLogger(GifDrawableImageViewTarget.class.getSimpleName());

    private GifDrawable resource;

    public GifDrawableImageViewTarget(ImageView view) {
        super(view);
    }

    @Override
    public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
        super.onResourceReady(resource, glideAnimation);
        this.resource = resource;
        onStart();
        logger.debug("onResourceReady");

    }

    @Override
    protected void setResource(GifDrawable resource) {
        view.setImageDrawable(resource);
    }

    @Override
    public void onStart() {
        if (resource != null) {
            resource.start();
        }
    }

    @Override
    public void onStop() {
        if (resource != null) {
            resource.stop();
        }
    }
}
