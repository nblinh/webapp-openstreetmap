package fr.systemx.mic.resource.utils;

import com.artelys.mic.data.containers.InterZonalData;
import com.artelys.mic.data.matrices.ZonalMatrix;
import com.artelys.mic.data.matrices.ZonalVector;
import com.artelys.mic.data.zones.Region;
import com.artelys.mic.data.zones.Zone;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.coordinatesequence.PackedCSBuilder;
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
public class TravelTimeUtil {

    public static void interZonalDataToGeoJSON(InterZonalData interZonalData, Integer segmentIndex, Map<String, Object> outputMap){
        GeoJSONWriter w = new GeoJSONWriter();
        List<Feature> features = new ArrayList<Feature>();

        //get all polygons of root zoning
        Map<String, Region> regions= interZonalData.getZoning().getRegionsMap();

        ZonalMatrix zonalMatrix = null;
        if(segmentIndex==null){
             zonalMatrix = interZonalData.getAttributes().get(0);
        }else{
            zonalMatrix=interZonalData.getAttributes().get(segmentIndex);
        }

        //map<idzone, point)
        Map<String, Double[]> mapIdPoint = new HashMap<String, Double[]>();
        for (Map.Entry<String, Region> entry:regions.entrySet()){
            try {
                Region region = entry.getValue();
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put("name", region.getName());
                properties.put("id", region.getID());
                properties.put("superZone", region.getSuperZone());

                org.wololo.geojson.Geometry geometry=null;


                CoordinateReferenceSystem source = CRS.decode("EPSG:2154");
                CoordinateReferenceSystem target = CRS.decode("EPSG:4326",true);

                MathTransform transform = CRS.findMathTransform(source, target, true);
                com.vividsolutions.jts.geom.Geometry targetPolygon = JTS.transform(region.getPolygon(), transform);
                Double[] point =  new Double[]{targetPolygon.getInteriorPoint().getX(), targetPolygon.getInteriorPoint().getY()};
                properties.put("XY", point);
                properties.put("mapPointValue", getMapPointValue(region, zonalMatrix));
                mapIdPoint.put(region.getID(), point);
                if(segmentIndex==null) {//first time call this projection
                    geometry=w.write(targetPolygon);
                }
                features.add(new Feature(geometry, properties));

            } catch (FactoryException e) {
                e.printStackTrace();
            } catch (TransformException e) {
                e.printStackTrace();
            }
        }
        outputMap.put("features", features);
        outputMap.put("mapIdPoint", mapIdPoint);
        outputMap.put("segments", interZonalData.getAttributeNames());
    }

    //get value from zone to all others
    public static Map<String, Double> getMapPointValue(Zone zone, ZonalMatrix zonalMatrix){
        Map<String, Double> mapPointValue = new HashMap<String, Double>();
        Map<Zone, Double> zonalVector = (Map<Zone, Double>) zonalMatrix.getVector(zone).getVector();
        for(Map.Entry<Zone, Double> entry: zonalVector.entrySet()){
            mapPointValue.put(entry.getKey().getID(), entry.getValue());
        }
        return mapPointValue;
    }
}
