/*
 * Copyright 2015 Amandeep Anguralla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.quiz.networking;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GsonRequest<T> extends Request<T>
{
    private static final String LOG_TAG = GsonRequest.class.getSimpleName();

    protected final Gson gson = new Gson();

    protected final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;
    private final Map<String, String> params;

    private final String body;
    private ErrorListener mErrorListener;
    private static final String HEADER_ENCODING = "Content-Encoding";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";
    private boolean mGzipEnabled = true;


    /**
     *
     * @param method is type of method such as GET, POST, PUT, DELETE etc.
     * @param url is the url to hit
     * @param clazz is the resulting response class used by gson
     * @param headers are headers to be sent with request
     * @param params are the method parameters to be sent with request
     * @param body is data to be sent to server as body(mostly JSON)
     * @param listener is the response listener
     * @param errorListener is the error listener
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params, String body, Listener<T> listener, ErrorListener errorListener)
    {
        super(method, url, errorListener);
        Logging.logMessage(LOG_TAG, url, CommonLibs.Priority.VERY_LOW);

        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.params = params;
        this.body = body;
        this.mErrorListener = errorListener;
    }

    /**
     *
     * @param method is type of method such as GET, POST, PUT, DELETE etc.
     * @param url is the url to hit
     * @param clazz is the resulting response class used by gson
     * @param headers are headers to be sent with request
     * @param params are the method parameters to be sent with request
     * @param listener is the response listener
     * @param errorListener is the error listener
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params, Listener<T> listener, ErrorListener errorListener)
    {
        this(method, url, clazz, headers, params, null, listener, errorListener);
    }

    /**
     *
     * @param method is type of method such as GET, POST, PUT, DELETE etc.
     * @param url is the url to hit
     * @param clazz is the resulting response class used by gson
     * @param headers are headers to be sent with request
     * @param listener is the response listener
     * @param errorListener is the error listener
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener, ErrorListener errorListener)
    {
        this(method, url, clazz, headers, null, null, listener, errorListener);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if(body != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzos = null;
            try {
                gzos = new GZIPOutputStream(baos);
                gzos.write(body.getBytes("UTF-8"));
                gzos.flush();
            } catch (IOException exception) {
                Logging.logException(LOG_TAG, exception, CommonLibs.Priority.VERY_HIGH);
            } finally {
                if (gzos != null) try {
                    gzos.close();
                } catch (IOException exception) {
                    Logging.logException(LOG_TAG, exception, CommonLibs.Priority.LOW);
                }
            }
            return baos.toByteArray();
        }
        else
            return super.getBody();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(headers != null && mGzipEnabled) {
            headers.put(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
            headers.put(HEADER_ENCODING, ENCODING_GZIP);
        }
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError volleyError) {
        Logging.logException(LOG_TAG, volleyError, CommonLibs.Priority.VERY_LOW);
        mErrorListener.onErrorResponse  (volleyError);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            final byte[] data;
            if (mGzipEnabled && isGzipped(response))
                data = decompressResponse(response.data);
            else
                data = response.data;

            String json = new String(data, HttpHeaderParser.parseCharset(response.headers));
            Logging.logMessage(LOG_TAG, json, CommonLibs.Priority.LOW);
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException exception) {
            return Response.error(new ParseError(exception));
        }
        catch (JsonSyntaxException exception) {
            return Response.error(new ParseError(exception));
        }
        catch (IOException exception) {
            return Response.error(new ParseError(exception));
        }
    }

    protected boolean isGzipped(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        return headers != null && !headers.isEmpty() && headers.containsKey(HEADER_ENCODING) &&
                headers.get(HEADER_ENCODING).equalsIgnoreCase(ENCODING_GZIP);
    }

    protected byte[] decompressResponse(byte[] compressed) throws IOException {
        ByteArrayOutputStream baos = null;
        try {
            int size;
            ByteArrayInputStream memstream = new ByteArrayInputStream(compressed);
            GZIPInputStream gzip = new GZIPInputStream(memstream);
            final int buffSize = 8192;
            byte[] tempBuffer = new byte[buffSize];
            baos = new ByteArrayOutputStream();
            while ((size = gzip.read(tempBuffer, 0, buffSize)) != -1) {
                baos.write(tempBuffer, 0, size);
            }
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                baos.close();
            }
        }
    }

    protected boolean isGzipEnabled() {
        return mGzipEnabled;
    }

    /**
     * Disables GZIP compressing (enabled by default)
     */
    protected void disableGzip() {
        mGzipEnabled = false;
    }
}