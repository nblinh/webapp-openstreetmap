package fr.systemx.mic.resource.travelTime;

import com.artelys.mic.data.containers.InterZonalData;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;
import fr.systemx.mic.resource.generation.GenerationResourceImpl;
import fr.systemx.mic.resource.utils.TravelTimeUtil;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ba-linh.nguyen on 14/11/2016.
 */
public class TravelTimeResourceImpl implements TravelTimeResource {
    private static final transient Logger LOG = Logger.getLogger(GenerationResourceImpl.class);

    public static Map<String, Object> getListInterZonalDatas(Map<String, Object> outputMap) {
        Map<String, Document<InterZonalData>> mapInterZonalDatas = Project.get().getInterZonalData();

        List<String> interZonalDatas = new ArrayList<String>();

        for (Map.Entry<String, Document<InterZonalData>> entry : mapInterZonalDatas.entrySet()) {
            interZonalDatas.add(entry.getKey());
        }

        outputMap.put("interZonalDatas", interZonalDatas);

        return outputMap;
    }

    /*name:         travelTime's name;
      segementIndex: index of arraylist segement list*/
    public Response getTravelTime(String name, Integer segementIndex) {
        LOG.info("getTravelTime()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();
        InterZonalData interZonalData = Project.get().getInterZonalData(name);

        TravelTimeUtil.interZonalDataToGeoJSON(interZonalData, segementIndex, outputMap);
        return Response.ok(outputMap).build();
    }
}
