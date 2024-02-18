package org.amitstein;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ip")
public class IPResource {

    @Inject
    IPService ipService; 

    @GET
    @Path("/{ip}")
    @Produces(MediaType.TEXT_PLAIN)
    public String ip(String ip) {
        return String.valueOf(ipService.exists(ip));
    }
}
