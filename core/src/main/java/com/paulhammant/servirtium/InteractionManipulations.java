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

import java.util.List;

public interface InteractionManipulations {

    default void changeSingleHeaderForRequestToRealService(String currentHeader, List<String> clientRequestHeaders) {
    }

    default String headerReplacement(String hdrKey, String hdrVal) {
        return hdrVal;
    }

    default String changeUrlForRequestToRealService(String url) {
        return url;
    }

    default String changeSingleHeaderReturnedBackFromRealServiceForRecording(int ix, String headerBackFromService) {
        return headerBackFromService;
    }

    default void changeAnyHeadersReturnedBackFromRealServiceForRecording(List<String> serviceResponseHeaders) {
    }

    /**
     * Change things in the body returned from the server before: a) making a recording, and b) playing back a recording.
     * If you're using the same InteractionManipulations instance for record and playback, there could be some
     * potentially double changing going on here, but you could view that as harmless.  This is called before any
     * pretty printing happens.
     *
     * @param bodyFromService the string representation of the body returned from the server
     * @return the modified (or not) string representation of the body returned from the server
     */
    default String changeBodyReturnedBackFromRealServiceForRecording(String bodyFromService) {
        return bodyFromService;
    }

    /**
     * Change things in the body returned from the server as it was recorded (and potentially changed
     * in changeBodyReturnedBackFromRealServiceForRecording() but before responding to the client.
     * This is called after any pretty printing happened (because that's in the recording too).
     *
     * @param bodyAsRecorded the string representation of the body as recorded.
     * @return the modified (or not) string representation of the body as recorded
     */
    default String changeBodyForClientResponseAfterRecording(String bodyAsRecorded) {
        return bodyAsRecorded;
    }

    default String[] changeHeadersForClientResponseAfterRecording(String[] headers) {
        return headers;
    }

    default void changeAnyHeadersForRequestToRealService(List<String> clientRequestHeaders) {
    }

    /** This may be Base84 encoded binary, but you're seldom going to want to change that */
    default String changeBodyForRequestToRealService(String body) {
        return body;
    }

    class NullObject implements InteractionManipulations {

    }

}
