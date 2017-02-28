package fr.systemx.mic.resource.zoning;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ba-linh.nguyen on 21/10/2016.
 */

@Path("/")
@Api(value = "/zoning", description = "get Zoning REST")
public interface ZoningResource {
    @GET
    @Path("/getZoning")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get zoning details par id", notes="example:<br> id = id_30zones")
    public Response getZoning( @ApiParam(value = "zoning id (parentName_enfantName)", required = true)@QueryParam("id") String id);

    @GET
    @Path("/getZonningZonalData")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get zoning information like: employs, populations, ...",
            notes="example:<br> id=id_30zones; zonalDataName=allData_comm_2010; attributeNameIndex=1")
    public Response getZonningZonalData(
            @ApiParam(value = "zoning id (parentName_enfantName)", required = true)@QueryParam("id") String id,
            @ApiParam(value = "zonal data name", required = true)@QueryParam("zonalDataName") String zonalDataName,
            @ApiParam(value = "index of list attributes", required = false)@QueryParam("attributeNameIndex") Integer attributeNameIndex);
}
