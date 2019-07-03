/*
        Servirtium: Service Virtualized HTTP

        Copyright (c) 2018, Paul Hammant
        All rights reserved.

        Redistribution and use in source and binary forms, with or without
        modification, are permitted provided that the following conditions are met:

        1. Redistributions of source code must retain the above copyright notice, this
        list of conditions and the following disclaimer.
        2. Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the documentation
        and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
        ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
        WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
        DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
        ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
        (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
        LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
        ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
        (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
        SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

        The views and conclusions contained in the software and documentation are those
        of the authors and should not be interpreted as representing official policies,
        either expressed or implied, of the Servirtium project.
*/

package com.paulhammant.servirtium;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.paulhammant.servirtium.ServirtiumServer.isText;

/**
 * Invoke remote HTTP services using Square's OkHttp library.
 */
public class ServiceInteropViaOkHttp implements ServiceInteroperation {

    private OkHttpClient okHttpClient;
    private int readTimeout = 10; // secs
    private int writeTimeout = 10; // secs
    private int connectionTimeout = 10; // secs

    /**
     * Change client to have a write timeout that's no the default for OkHttp
     * @param writeTimeout in milliseconds
     * @return this
     */
    public ServiceInteropViaOkHttp withWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    /**
     * Change client to have a read timeout that's no the default for OkHttp
     * @param readTimeout in milliseconds
     * @return this
     */
    public ServiceInteropViaOkHttp withReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * Change client to have a connection timeout that's no the default for OkHttp
     * @param connectionTimeout in milliseconds
     * @return this
     */
    public ServiceInteropViaOkHttp withConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    private OkHttpClient makeOkHttpClient() {
        return  new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public ServiceResponse invokeServiceEndpoint(String method,
                                                 Object clientRequestBody,
                                                 String clientRequestContentType,
                                                 String url, List<String> clientRequestHeaders,
                                                 InteractionManipulations interactionManipulations,
                                                 boolean forceHeadersToLowerCase) throws ServiceInteroperationFailed {

        if (okHttpClient == null) {
            okHttpClient = makeOkHttpClient();
        }

        RequestBody nonGetBody = null;
        if (!method.equals("GET")) {
            MediaType mediaType = MediaType.parse(clientRequestContentType);
            if (clientRequestBody != null) {
                if (clientRequestBody instanceof String) {
                    nonGetBody = RequestBody.create(mediaType, (String) clientRequestBody);
                } else {
                    nonGetBody = RequestBody.create(mediaType, (byte[]) clientRequestBody);
                }
            }
        }

        Response response = null;
        try {
            Request.Builder reqBuilder = null;

            Headers.Builder hb = new Headers.Builder();

            for (String h : clientRequestHeaders) {
                hb.add(h);
            }

            final Headers headerForOkHttp = hb.build();

            if (method.equalsIgnoreCase("POST")) {
                reqBuilder = new Request.Builder().url(url).post(nonGetBody).headers(headerForOkHttp);
            } else {
                reqBuilder = new Request.Builder().url(url).method(method, nonGetBody).headers(headerForOkHttp);
            }

            try {
                response = okHttpClient.newCall(reqBuilder.build()).execute();
            } catch (SocketTimeoutException e) {
                throw new ServiceInteroperationFailed("OkHttp "  +  method + " to " + url + " timed out. See ServiceInteropViaOkHttp.withReadTimeout(), withWriteTimeout(), and withConnectionTimeout()", e);
            }

            ResponseBody body = response.body();
            Object responseBody = null;
            String contentType = null;
            if (body.contentType() == null) {
                contentType = "";
            } else {
                contentType = body.contentType().toString();
                if (contentType == null) {
                    contentType = "";
                }
            }
            if (isText(contentType)) {
                responseBody = body.string();
            } else {
                responseBody = body.bytes();
            }
            String responseContentType = response.header("Content-Type");
            int statusCode = response.code();
            String[] responseHeaders = response.headers().toString().split("\n");
            ArrayList<String> responseHeaders2 = new ArrayList<>();
            for (String hdrLine : responseHeaders) {
                int ix = hdrLine.indexOf(": ");
                String hdrName = hdrLine.substring(0, ix);
                String hdrVal = hdrLine.substring(ix + 2);
                String hdrKey = forceHeadersToLowerCase ? hdrName.toLowerCase() : hdrName; // HTTP 2.0 says lower-case header keys.
                responseHeaders2.add(hdrKey + ": " + interactionManipulations.headerReplacement(hdrKey, hdrVal));
            }
            final String[] headers = responseHeaders2.toArray(new String[responseHeaders.length]);
            return new ServiceResponse(responseBody, responseContentType, statusCode, headers);

        } catch (IOException e) {
            throw new ServiceInteroperationFailed("OkHttp " + method + " to " + url + " failed with an IOException", e);
        }
    }

}
