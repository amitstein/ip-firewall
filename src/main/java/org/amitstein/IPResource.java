package org.amitstein;

import org.jboss.resteasy.reactive.RestQuery;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/check-ip")
public class IPResource {

    @Inject
    IPService ipService; 

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ip(@RestQuery String ip) {
        return String.valueOf(ipService.exists(ip));
    }
}
