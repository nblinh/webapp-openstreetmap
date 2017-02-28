package fr.systemx.mic.resource.zonalData;

import com.artelys.mic.data.containers.ZonalData;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ba-linh.nguyen on 08/11/2016.
 */
public class ZonalDataResourceImpl implements ZonalDataResource{


    public static Map<String, Object> getListZonalDatas(Map<String, Object> outputMap) {
        Map<String, Document<ZonalData>> mapZonalDatas = Project.get().getZonalDatas();

        List<String> zonalDatas = new ArrayList<String>();

        for (Map.Entry<String, Document<ZonalData>> entry : mapZonalDatas.entrySet()) {
            zonalDatas.add(entry.getKey());
        }

        outputMap.put("zonalDatas", zonalDatas);

        return outputMap;
    }

}
