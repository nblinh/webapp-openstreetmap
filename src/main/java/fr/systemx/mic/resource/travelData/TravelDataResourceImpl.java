package fr.systemx.mic.resource.travelData;

import com.artelys.mic.Shortcuts;
import com.artelys.mic.data.ZoningUtils;
import com.artelys.mic.data.containers.TravelData;
import com.artelys.mic.data.matrices.CompleteODMatrix;
import com.artelys.mic.data.matrices.ZonalVector;
import com.artelys.mic.data.zones.Zoning;
import com.artelys.mic.gen.calibration.GenerationProjection;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;
import com.artelys.mic.uc.ControlDistribution;
import com.artelys.mic.uc.data.DistributionCalibration;
import com.artelys.mic.uc.data.UtilityFormulation;
import com.artelys.mic.util.TDMException;
import fr.systemx.mic.data.tree.DataStatic;
import fr.systemx.mic.resource.utils.TravelDataUtil;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ba-linh.nguyen on 14/11/2016.
 */
public class TravelDataResourceImpl implements TravelDataResource {
    private static final transient Logger LOG = Logger.getLogger(TravelDataResourceImpl.class);

    public Response getZoningTravelData(String id, String travelDataName, Integer purposeIndex){
        LOG.info("getZoningTravelData()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        TravelDataUtil.travelDataToGeoJSON(DataStatic.mapZoning.get(id), travelDataName, purposeIndex, outputMap);

        return Response.ok(outputMap).build();
    }

    //OD matrix
    public static Map<String, Object> getListTravelDatas(Map<String, Object> outputMap) {
        Map<String, Document<TravelData>> mapTravelDatas = Project.get().getTravelStudies();
        List<String> travelDatas = new ArrayList<String>();

        for (Map.Entry<String, Document<TravelData>> entry : mapTravelDatas.entrySet()) {
            travelDatas.add(entry.getKey());
        }

        outputMap.put("travelDatas", travelDatas);

        return outputMap;
    }

    public Response projectionODMatrixDistribution(String name, String zoningId, String travelDataName, String generation,
            String projectionName, String calibrationName, String utilityName) {
        LOG.info("projectionODMatrixDistribution()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        Zoning zoningVal = Shortcuts.getObject(Zoning.class, zoningId.substring(zoningId.indexOf('_')+1));
        //Zoning zoningVal = DataStatic.mapZoning.get(zoningId);
        List<ZonalVector> em = new ArrayList<ZonalVector>(), at = new ArrayList<ZonalVector>();
        TravelData odInitialZoning = Shortcuts.getObject(TravelData.class, travelDataName);
        TravelData odNewZoning = odInitialZoning.projectOnZoning(zoningVal);
        CompleteODMatrix od = odNewZoning.getOD();

        if("originalOD".equals(generation)) {
            em = od.getEmissionsPerPurpose();
            at= od.getAttractionsPerPurpose();
        }
        else {
            GenerationProjection proj = Shortcuts.getObject(GenerationProjection.class, projectionName);
            for(ZonalVector emission : proj.getEmissions()) {
                em.add(ZoningUtils.project(emission, proj.getZoning(), zoningVal));
            }
            for(ZonalVector attraction : proj.getAttractions()) {
                at.add(ZoningUtils.project(attraction, proj.getZoning(), zoningVal));
            }
        }

        DistributionCalibration calibration = Shortcuts.getObject(DistributionCalibration.class,calibrationName);
        UtilityFormulation formulation = Shortcuts.getObject(UtilityFormulation.class,utilityName);

        try {
            ControlDistribution.projectionDemand(name, em, at, odNewZoning, calibration, formulation);
        } catch (TDMException e) {
            e.printStackTrace();
            outputMap.put("operation", "failed");
            return Response.ok(outputMap).build();
        }
        outputMap.put("operation", "sucess");
        return Response.ok(outputMap).build();
    }

}
