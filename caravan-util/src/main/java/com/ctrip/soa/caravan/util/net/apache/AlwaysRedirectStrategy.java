package com.ctrip.soa.caravan.util.net.apache;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class AlwaysRedirectStrategy extends DefaultRedirectStrategy {

    public static final AlwaysRedirectStrategy DEFAULT = new AlwaysRedirectStrategy();

    @Override
    public boolean isRedirected(final HttpRequest request, final HttpResponse response, final HttpContext context) throws ProtocolException {
        if (super.isRedirected(request, response, context))
            return true;

        int statusCode = response.getStatusLine().getStatusCode();
        return isRedirected(statusCode);
    }

    protected boolean isRedirected(int statusCode) {
        return statusCode == HttpStatus.SC_TEMPORARY_REDIRECT;
    }

}
