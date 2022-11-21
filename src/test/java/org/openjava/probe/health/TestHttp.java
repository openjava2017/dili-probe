package org.openjava.probe.health;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestHttp {
    private static final String HEALTH_CHECK_PATH = "/api/health/heartbeat";

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (response.getStatus() == 404) {
            String path = (String)request.getAttribute("javax.servlet.error.request_uri");
            if (path != null && path.contains(HEALTH_CHECK_PATH)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
        }
        System.out.println("-----");
    }
}
