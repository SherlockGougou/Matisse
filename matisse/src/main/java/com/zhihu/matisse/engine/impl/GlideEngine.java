/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhihu.matisse.engine.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zhihu.matisse.engine.ImageEngine;
import java.io.File;

/**
 * {@link ImageEngine} implementation using Glide.
 */

public class GlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, final int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context)
            .load(uri)
            .asBitmap()  // some .jpeg files are actually gif
            .placeholder(placeholder)
            .override(resize, resize)
            .centerCrop()
            .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView,
        Uri uri) {
        Glide.with(context)
            .load(uri)
            .asBitmap()
            .placeholder(placeholder)
            .override(resize, resize)
            .centerCrop()
            .into(imageView);
    }

    @Override
    public void loadImage(Context context, final int resizeX, int resizeY, final SubsamplingScaleImageView imageView, final Uri uri) {
        Glide.with(context)
            .load(uri)
            .downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    imageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                }
            });
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
            .load(uri)
            .asGif()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .priority(Priority.HIGH)
            .into(imageView);
    }

    @Override
    public void pauseLoad(Context context, String tag) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resumeLoad(Context context, String tag) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
