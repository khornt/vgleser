package com.horntvedt.vgleser.camel.route;


import com.horntvedt.vgleser.config.RouteEndepunkter;
import com.horntvedt.vgleser.camel.processor.EtOgEtElement;
import com.horntvedt.vgleser.camel.processor.VgLeserRespons;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LesVgRoute extends RouteBuilder {


    @Inject
    VgLeserRespons vgLeserRespons;

    @Inject
    EtOgEtElement etOgEtElement;

    private static final String FEED_EN_OG_EN = "direct:FEED_EN_OG_EN";
    private static final String EN_OG_EN_ROUTE_ID = "route en og en";

    @Override
    public void configure() throws Exception {

           onException().id("bare feil")
            .handled(true)
            .logStackTrace(true)
            .stop();


        from(RouteEndepunkter.LESER.uri()).routeId(RouteEndepunkter.LESER.id())
                .log(LoggingLevel.INFO, "leser fra VG")
                .to(RouteEndepunkter.VG_LESER_KONSUMENT.uri())
                .process(vgLeserRespons)
                .split(body())
                .to(FEED_EN_OG_EN);

        from(FEED_EN_OG_EN).routeId(EN_OG_EN_ROUTE_ID)
                .process(etOgEtElement);


    }



}
