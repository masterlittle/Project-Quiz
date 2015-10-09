package com.project.quiz.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

/**
 * Created by amandeep on 11/20/14.
 */
public class VolleySingleton
{
    private static VolleySingleton mInstance;
    private static Context mCtx;
    //private static LruBitmapCache lruBitmapCache;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleySingleton(Context context)
    {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        //lruBitmapCache = new LruBitmapCache(LruBitmapCache.getCacheSize(mCtx));
    }

    public static synchronized VolleySingleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public ImageLoader getImageLoader()
    {
        getRequestQueue();
        if (mImageLoader == null)
        {
            //mImageLoader = new ImageLoader(this.mRequestQueue, lruBitmapCache);
        }
        return this.mImageLoader;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new OkHttpClient());
        }
        return mRequestQueue;
    }

    public void clearLruCache()
    {
        Logging.logError("Out of Memory", "Cache Out Of Memory Catch", CommonLibs.Priority.MEDIUM);
        //lruBitmapCache.evictAll();
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
