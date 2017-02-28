package fr.systemx.mic.resource.generation;

import com.artelys.mic.gen.ControlGeneration;
import com.artelys.mic.gen.calibration.GenerationCalibration;
import com.artelys.mic.gen.calibration.GenerationProjection;
import com.artelys.mic.gen.module.GenProjectExtension;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;
import com.artelys.mic.util.TDMException;
import fr.systemx.mic.data.tree.DataStatic;
import fr.systemx.mic.resource.utils.ProjectionUtil;
import fr.systemx.mic.resource.utils.ZoningUtil;
import org.apache.log4j.Logger;
import org.wololo.geojson.Feature;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ba-linh.nguyen on 27/10/2016.
 */
public class GenerationResourceImpl implements GenerationResource{
    private static final transient Logger LOG = Logger.getLogger(GenerationResourceImpl.class);

    /*name:         projection's name;
      emisAttrType: emission ou attraction;
      purposeIndex: index of arraylist purpose*/
    public Response getProjection(String name, String emisAttrType, Integer purposeIndex) {
        LOG.info("getProjection()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();
        Map<String, Document<GenerationProjection>> generationProjectionMap = GenProjectExtension.getGenerationProjections(Project.get());
        GenerationProjection projection = generationProjectionMap.get(name).get();

        List<Feature> features = ProjectionUtil.projectionToGeoJSON(projection,emisAttrType, purposeIndex);
        outputMap.put("features", features);
        if(emisAttrType==null){
            outputMap.put("purposes", projection.getPurposes());
        }
        return Response.ok(outputMap).build();
    }

    public static Map<String, Object> getGenerationProjection(Map<String, Object> outputMap) {
        List<String> generationProjections = new ArrayList<String>();
        Map<String, Document<GenerationProjection>> generationProjectionMap = GenProjectExtension.getGenerationProjections(Project.get());

        for (Map.Entry<String, Document<GenerationProjection>> entry : generationProjectionMap.entrySet()) {
            generationProjections.add(entry.getKey());
        }

        outputMap.put("generationProjections", generationProjections);

        return outputMap;
    }

    public static Map<String, Object> getGenerationCalibrations(Map<String, Object> outputMap) {
        List<String> generationCalibrations = new ArrayList<String>();
        Map<String, Document<GenerationCalibration>> generationCalibrationMap = GenProjectExtension.getGenerationCalibrations(Project.get());

        for (Map.Entry<String, Document<GenerationCalibration>> entry : generationCalibrationMap.entrySet()) {
            generationCalibrations.add(entry.getKey());
        }

        outputMap.put("generationCalibrations", generationCalibrations);

        return outputMap;
    }

    public Response generationProjectionCalibration(String name, String zoningId, String zonalDataName, String calibrationName) {
        LOG.info("generationProjectionCalibration()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        Project project = Project.get();
        try {
            ControlGeneration.projection(project, name, DataStatic.mapZoning.get(zoningId), project.getZonalData(zonalDataName),
                    GenProjectExtension.getGenerationCalibration(project, calibrationName), false);
        } catch (TDMException e) {
            e.printStackTrace();
            outputMap.put("operation", "failed");
            return Response.ok(outputMap).build();
        }
        outputMap.put("operation", "sucess");
        return Response.ok(outputMap).build();
    }


}
