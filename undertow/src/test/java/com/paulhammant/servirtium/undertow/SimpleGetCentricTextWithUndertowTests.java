package com.paulhammant.servirtium.undertow;

import com.paulhammant.servirtium.InteractionMonitor;
import com.paulhammant.servirtium.ServiceMonitor;
import com.paulhammant.servirtium.ServirtiumServer;
import com.paulhammant.servirtium.SimpleGetCentricTextTests;
import com.paulhammant.servirtium.SimpleInteractionManipulations;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleGetCentricTextWithUndertowTests extends SimpleGetCentricTextTests {

    public ServirtiumServer makeServirtiumServer(ServiceMonitor.Console serverMonitor, SimpleInteractionManipulations interactionManipulations, InteractionMonitor interactionMonitor, int port) {
        return new UndertowServirtiumServer(serverMonitor,
                port, interactionManipulations, interactionMonitor);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Override @Test
    public void canRecordASimpleGetFromApachesSubversionViaOkHttp() throws Exception {
        super.canRecordASimpleGetFromApachesSubversionViaOkHttp();
    }

    @Override @Test
    public void canRecordASequenceThenBarfInPlaybackWithClearMessagingIfUnplayedInteractions() throws Exception {
        super.canRecordASequenceThenBarfInPlaybackWithClearMessagingIfUnplayedInteractions();
    }

    @Override @Test
    public void canRecordASimpleGetOfARedditJsonDocumentAndPrettify() throws Exception {
        super.canRecordASimpleGetOfARedditJsonDocumentAndPrettify();
    }

    @Override @Test
    public void canRecordASimpleQueryStringGet() throws Exception {
        super.canRecordASimpleQueryStringGet();
    }

    @Override @Test
    public void canPassThroughASimpleQueryStringGet() throws Exception {
        super.canPassThroughASimpleQueryStringGet();
    }

    @Override @Test
    public void canPlaybackASimpleQueryStringGet() throws Exception {
        super.canPlaybackASimpleQueryStringGet();
    }

    @Override @Test @Ignore
    public void canSupplyDebugInformationOnRedditJsonGet() throws Exception {
        super.canSupplyDebugInformationOnRedditJsonGet();
    }

    @Override @Test
    public void worksThroughAproxyServer() throws Exception {
        super.worksThroughAproxyServer();
    }

    @Override @Test @Ignore
    public void worksThroughAproxyServer2() throws Exception {
        super.worksThroughAproxyServer2();
    }

    @Override @Test
    public void canRecordASimpleGetOfARedditJsonDocumentAndPrettifyAndRedactPartOfTheRecordingOnly() throws Exception {
        super.canRecordASimpleGetOfARedditJsonDocumentAndPrettifyAndRedactPartOfTheRecordingOnly();
    }

    @Override @Test
    public void canReplayASimpleGetOfARedditJsonDocumentAndPrettifyAndRedactPartOfTheRecordingOnly() throws Exception {
        super.canReplayASimpleGetOfARedditJsonDocumentAndPrettifyAndRedactPartOfTheRecordingOnly();
    }

    @Override @Test
    public void canReplayWithAReplacementInTheURLtoo() throws Exception {
        super.canReplayWithAReplacementInTheURLtoo();
    }

    @Override @Test
    public void canReplayASimpleGetFromApachesSubversion() throws Exception {
        super.canReplayASimpleGetFromApachesSubversion();
    }
}
