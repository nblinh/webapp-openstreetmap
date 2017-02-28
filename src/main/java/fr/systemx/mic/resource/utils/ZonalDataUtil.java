package fr.systemx.mic.resource.utils;

import com.artelys.mic.data.ZoningUtils;
import com.artelys.mic.data.containers.ZonalData;
import com.artelys.mic.data.matrices.ZonalVector;
import com.artelys.mic.data.zones.Zone;
import com.artelys.mic.data.zones.Zoning;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;
import com.artelys.mic.view.ProjectWindow;
import fr.systemx.mic.data.tree.DataStatic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ba-linh.nguyen on 09/11/2016.
 */
public class ZonalDataUtil {
    public static Map<String, Double> getZonalData(Zoning zoning, String zonalDataName, Integer attributeNameIndex) {
        Map<String, Double> attributes = new HashMap<String, Double>();

        Map<String, Document<ZonalData>> mapZonalDatas = Project.get().getZonalDatas();
        ZonalData zonalData = mapZonalDatas.get(zonalDataName).get();

        if (!zoning.isAnAggregationOf(zonalData.getZoning())) {
            return attributes;
        } else {
            if(null==attributeNameIndex||attributeNameIndex==-1){
                attributeNameIndex=0;
            }
            Map<Zone, Double> atts = (Map<Zone, Double>)zonalData.projectAttributeOnZoning(
                    zonalData.getAttributeNames().get(attributeNameIndex), zoning).getVector();
            for(Map.Entry<Zone, Double> entry : atts.entrySet()){
                attributes.put(entry.getKey().getID(), entry.getValue());
            }
        }
        return attributes;
    }

    public static List<String> getAttributeList(String zonalDataName) {
        Map<String, Document<ZonalData>> mapZonalDatas = Project.get().getZonalDatas();
        ZonalData zonalData = mapZonalDatas.get(zonalDataName).get();

        return zonalData.getAttributeNames();
    }
}

