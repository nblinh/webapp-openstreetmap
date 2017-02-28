package fr.systemx.mic.resource.utils;

import com.artelys.mic.data.matrices.ZonalVector;
import com.artelys.mic.data.zones.Region;
import com.artelys.mic.data.zones.Zoning;
import com.artelys.mic.gen.calibration.GenerationProjection;
import com.artelys.mic.gen.module.GenProjectExtension;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.wololo.geojson.Feature;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ba-linh.nguyen on 04/11/2016.
 */
public class ProjectionUtil {

    //emisAttrType: emission ou attraction; purpose: motif de deplacement
    public static List<Feature> projectionToGeoJSON(GenerationProjection projection, String emisAttrType, Integer purposeIndex){
        GeoJSONWriter w = new GeoJSONWriter();
        List<Feature> features = new ArrayList<Feature>();

        //get all polygons of root zoning
        Map<String, Region> regions= projection.getZoning().getRegionsMap();

        ZonalVector emissionAttraction=null;
        if("attraction".equals(emisAttrType)) {
            emissionAttraction=projection.getAttraction(purposeIndex);
        }else if("emission".equals(emisAttrType)||emisAttrType==null){
            emissionAttraction = projection.getEmission(purposeIndex);
        }

        for (Map.Entry<String, Region> entry:regions.entrySet()){
            try {
                Region region = entry.getValue();
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put("name", region.getName());
                properties.put("id", region.getID());
                properties.put("superZone", region.getSuperZone());
                properties.put("emissionAttraction", Math.round(emissionAttraction.get(region)));

                org.wololo.geojson.Geometry geometry=null;

                if(emisAttrType==null) {//first time call this projection
                    CoordinateReferenceSystem source = CRS.decode("EPSG:2154");
                    CoordinateReferenceSystem target = CRS.decode("EPSG:4326",true);

                    MathTransform transform = CRS.findMathTransform(source, target, true);
                    com.vividsolutions.jts.geom.Geometry targetPolygon = JTS.transform(region.getPolygon(), transform);
                    geometry=w.write(targetPolygon);
                }
                features.add(new Feature(geometry, properties));

            } catch (FactoryException e) {
                e.printStackTrace();
            } catch (TransformException e) {
                e.printStackTrace();
            }
        }
        return features;
    }
}
