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
 *
 */

package com.project.quiz.networking;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
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


/**
 * Created by amandeep on 18/01/15.
 */
public class VolleyRequest extends StringRequest
{
    private static final String LOG_TAG = VolleyRequest.class.getSimpleName();
    private final Map<String, String> headers;
    private final Listener<String> listener;
    private final Map<String, String> params;
    private final String body;

    private static final String HEADER_ENCODING = "Content-Encoding";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private boolean mGzipEnabled = true;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param method for request such as GET, POST, PUT, DELETE etc
     * @param url to which request is to be sent
     * @param headers to be sent in request
     * @param params to be sent in request
     * @param body to the request to be sent
     * @param listener is the response callback
     * @param errorListener is the error callback
     */
    public VolleyRequest(int method, String url, Map<String, String> headers, Map<String, String> params, String body, Listener<String> listener, ErrorListener errorListener)
    {
        super(method, url, listener, errorListener);
        Logging.logMessage(LOG_TAG, url, CommonLibs.Priority.VERY_LOW);
        this.headers = headers;
        this.listener = listener;
        this.params = params;
        this.body = body;
    }

    /**
     *
     * @param url to which request is to be sent
     * @param listener is the response callback
     * @param headers to be sent in request
     * @param params to be sent in request
     * @param body to the request to be sent
     * @param errorListener is the error callback
     */
    public VolleyRequest(String url, Listener<String> listener, Map<String, String> headers, Map<String, String> params, String body, ErrorListener errorListener)
    {
        super(url, listener, errorListener);
        this.listener = listener;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }

    /**
     *
     * @param method for request such as GET, POST, PUT, DELETE etc
     * @param url to which request is to be sent
     * @param headers to be sent in request
     * @param params to be sent in request
     * @param listener is the response callback
     * @param errorListener is the error callback
     */
    public VolleyRequest(int method, String url, Map<String, String> headers, Map<String, String> params, Listener<String> listener, ErrorListener errorListener)
    {
        this(method, url, headers, params, null, listener, errorListener);
    }

    /**
     *
     * @param method for request such as GET, POST, PUT, DELETE etc
     * @param url to which request is to be sent
     * @param headers to be sent in request
     * @param listener is the response callback
     * @param errorListener is the error callback
     */
    public VolleyRequest(int method, String url, Map<String, String> headers, Listener<String> listener, ErrorListener errorListener)
    {
        this(method, url, headers, null, listener, errorListener);
    }

    /**
     *
     * @param method for request such as GET, POST, PUT, DELETE etc
     * @param url to which request is to be sent
     * @param listener is the response callback
     * @param errorListener is the error callback
     */
    public VolleyRequest(int method, String url, Listener<String> listener, ErrorListener errorListener)
    {
        this(method, url, null, listener, errorListener);
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        if(body != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzos = null;
            try {
                gzos = new GZIPOutputStream(baos);
                gzos.write(body.getBytes("UTF-8"));
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
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse)
    {
        try
        {
            final byte[] data;
            if (mGzipEnabled && isGzipped(networkResponse))
                data = decompressResponse(networkResponse.data);
            else
                data = networkResponse.data;

            String json = new String(data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(json, HttpHeaderParser.parseCacheHeaders(networkResponse));
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
        catch (JsonSyntaxException e)
        {
            return Response.error(new ParseError(e));
        }
        catch (IOException exception)
        {
            return Response.error(new ParseError(exception));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return params != null ? params : super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        if(headers != null && mGzipEnabled) {
            headers.put(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
            headers.put(HEADER_ENCODING, ENCODING_GZIP);
        }

        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(String response)
    {
        Logging.logMessage(LOG_TAG, response, CommonLibs.Priority.VERY_LOW);
        listener.onResponse(response);
    }

    private boolean isGzipped(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        return headers != null && !headers.isEmpty() && headers.containsKey(HEADER_ENCODING) &&
                headers.get(HEADER_ENCODING).equalsIgnoreCase(ENCODING_GZIP);
    }

    private byte[] decompressResponse(byte[] compressed) throws IOException {
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

    /**
     * Disables GZIP compressing (enabled by default)
     */
    public void disableGzip() {
        mGzipEnabled = false;
    }
}