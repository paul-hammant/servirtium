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

public class ServiceResponse {

    public final String[] headers;
    public final Object body;
    public final String contentType;
    public final int statusCode;

    public ServiceResponse(Object body, String contentType, int statusCode, String... headers) {
        this.headers = headers;
        this.body = body;
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    public ServiceResponse withRevisedHeaders(String[] headers) {
        return new ServiceResponse(this.body, this.contentType, this.statusCode, headers);
    }
    public ServiceResponse withRevisedBody(String body) {
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            if (header.startsWith("Content-Length")) {
                headers[i] = "Content-Length: " + body.length();
                break;
            }
            if (header.startsWith("content-length")) {
                headers[i] = "content-length: " + body.length();
                break;
            }
        }
        return new ServiceResponse(body, this.contentType, this.statusCode, this.headers);
    }
}
