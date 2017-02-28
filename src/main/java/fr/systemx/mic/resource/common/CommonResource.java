package fr.systemx.mic.resource.common;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ba-linh.nguyen on 27/10/2016.
 */
@Path("/")
@Api(value = "/common", description = "get common REST")
public interface CommonResource {
    @GET
    @Path("/getMenu")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get information pour menu of application",
            notes="menu include:<br>list of generation projections" +
                    "<br>list of interzonalData" +
                    "<br>zoning tree" +
                    "<br>list of travelData(matrix OD)" +
                    "<br>list of zonalData")
    public Response getMenu();

    @GET
    @Path("/getGenerationProjectionInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get informations to generate projection calibration",
            notes="include:<br>list of generation calibration" +
                    "<br>list of interzonalData" +
                    "<br>list of zoning" +
                    "<br>list of travelData(matrix OD)" +
                    "<br>list of zonalData")
    public Response getGenerationProjectionInfo();

    @GET
    @Path("/getProjectionODMatrixInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get informations to generate matrix OD",
            notes="include:<br>list of distribution calibration" +
                    "<br>list of generation projection" +
                    "<br>list of utilities formations" +
                    "<br>list of travelData(matrix OD)" +
                    "<br>list of zoning")
    public Response getProjectionODMatrixInfo();


}