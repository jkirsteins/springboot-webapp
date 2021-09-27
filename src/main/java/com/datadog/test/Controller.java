package com.datadog.test;
import io.opentracing.Span;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.opentracing.util.GlobalTracer;

@RestController
public class Controller {

    @RequestMapping(value = "/**")
    public String homePage(@RequestBody(required = false) String req) {
        final Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("appsec.event", true);
        }
        return "hello world " + req;
    }
}
