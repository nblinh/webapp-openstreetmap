package fr.systemx.mic.resource.travelTime;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ba-linh.nguyen on 14/11/2016.
 */
@Path("/")
@Api(value = "/traveltime", description = "get traveltime REST")
public interface TravelTimeResource {
    @GET
    @Path("/getTravelTime")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get time from one zone to another zone", notes="example:<br> name=road; segmentIndex=1")
    public Response getTravelTime(
            @ApiParam(value = "name of traveltime calculated by application", required = true)@QueryParam("name") String name,
            @ApiParam(value = "index of segement", required = true)@QueryParam("segmentIndex") Integer segementIndex);
}
