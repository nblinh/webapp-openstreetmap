package fr.systemx.mic.resource.common;

import com.artelys.mic.Shortcuts;
import com.artelys.mic.data.containers.TravelData;
import com.artelys.mic.data.zones.Zoning;
import com.artelys.mic.gen.calibration.GenerationProjection;
import com.artelys.mic.uc.data.DistributionCalibration;
import com.artelys.mic.uc.data.UtilityFormulation;
import fr.systemx.mic.data.tree.DataStatic;
import fr.systemx.mic.resource.generation.GenerationResourceImpl;
import fr.systemx.mic.resource.travelData.TravelDataResourceImpl;
import fr.systemx.mic.resource.travelTime.TravelTimeResourceImpl;
import fr.systemx.mic.resource.zonalData.ZonalDataResourceImpl;
import fr.systemx.mic.resource.zoning.ZoningResourceImpl;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by ba-linh.nguyen on 27/10/2016.
 */
public class CommonResourceImpl implements CommonResource{
    private static final transient Logger LOG = Logger.getLogger(GenerationResourceImpl.class);

    public Response getMenu(){
        LOG.info("getMenu()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        ZoningResourceImpl.getTreeZoning(outputMap);
        GenerationResourceImpl.getGenerationProjection(outputMap);
        ZonalDataResourceImpl.getListZonalDatas(outputMap);
        TravelTimeResourceImpl.getListInterZonalDatas(outputMap);
        TravelDataResourceImpl.getListTravelDatas(outputMap);

        return Response.ok(outputMap).build();
    }

    public Response getGenerationProjectionInfo(){
        LOG.info("getGenerationProjectionInfo()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        outputMap.put("zoningList", DataStatic.mapZoningName);
        ZonalDataResourceImpl.getListZonalDatas(outputMap);
        GenerationResourceImpl.getGenerationCalibrations(outputMap);
        TravelDataResourceImpl.getListTravelDatas(outputMap);

        return Response.ok(outputMap).build();
    }

    public Response getProjectionODMatrixInfo(){
        LOG.info("getProjectionODMatrixInfo()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        outputMap.put("zoningList", DataStatic.mapZoningName);
        //TravelDataResourceImpl.getListTravelDatas(outputMap);
        //GenerationResourceImpl.getGenerationProjection(outputMap);
        //outputMap.put("zoningList", Shortcuts.getDocumentNames(Zoning.class));
        outputMap.put("travelDatas", Shortcuts.getDocumentNames(TravelData.class));
        outputMap.put("generationProjections", Shortcuts.getDocumentNames(GenerationProjection.class));
        outputMap.put("distributionCalibrations", Shortcuts.getDocumentNames(DistributionCalibration.class));
        outputMap.put("utilityFormulation", Shortcuts.getDocumentNames(UtilityFormulation.class));

        return Response.ok(outputMap).build();
    }
}
