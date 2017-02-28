package fr.systemx.mic.resource.travelData;

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
@Api(value = "/traveldata", description = "get TravelData REST")
public interface TravelDataResource {
    @GET
    @Path("/getZoningTravelData")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get zoning traveldata", notes="example:<br>id=id_30zones;purposeIndex=1;travelDataName=EGT-modeSequences")
    public Response getZoningTravelData(
            @ApiParam(value = "zoning id (parentName_enfantName)", required = true)@QueryParam("id") String id,
            @ApiParam(value = "name of matrix OD)", required = true)@QueryParam("travelDataName") String travelDataName,
            @ApiParam(value = "index of purpose", required = false)@QueryParam("purposeIndex") Integer purposeIndex);

    @GET
    @Path("/projectionODMatrixDistribution")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get projectionODMatrix distribution",
            notes="example:<br>calibrationName=distCali30zones(name in the list of distribution calibration);" +
                    "<br>generation=originalOD (if we generate from matrix OD origine) else generation=projectedGeneration;" +
                    "<br>name=NewProjectionODMatrix;zoningId=id_30zones" +
                    "<br>projectionName=genCaliTest30zones (name in the list of generation calibrationi);" +
                    "<br>travelDataName=EGT-modeSequences;utilityName=constant4(name in the list of utilities)")
    public Response projectionODMatrixDistribution(
            @ApiParam(value = "name of projection OD Matrix", required = true)@QueryParam("name") String name,
            @ApiParam(value = "zoning id (parentName_enfantName)", required = true)@QueryParam("zoningId") String zoningId,
            @ApiParam(value = "name of matrix OD", required = true)@QueryParam("travelDataName") String travelDataName,
            @ApiParam(value = "type of generation projection", required = true)@QueryParam("generation") String generation,
            @ApiParam(value = "name of projection", required = true)@QueryParam("projectionName") String projectionName,
            @ApiParam(value = "name of calibration", required = true)@QueryParam("calibrationName") String calibrationName,
            @ApiParam(value = "name of utility function", required = true)@QueryParam("utilityName") String utilityName);
}
