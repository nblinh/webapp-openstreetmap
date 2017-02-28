package fr.systemx.mic.resource.utils;

import com.artelys.mic.data.containers.InterZonalData;
import com.artelys.mic.data.containers.TravelData;
import com.artelys.mic.data.containers.ZonalData;
import com.artelys.mic.data.matrices.ModalMatrix;
import com.artelys.mic.data.matrices.ZonalMatrix;
import com.artelys.mic.data.zones.Region;
import com.artelys.mic.data.zones.Zone;
import com.artelys.mic.data.zones.Zoning;
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
 * Created by ba-linh.nguyen on 16/11/2016.
 */
public class TravelDataUtil {

    public static List<Feature> travelDataToGeoJSON(Zoning zoning, String travelDataName, Integer purposeIndex, Map<String, Object> outputMap){
        //get all polygons of root zoning
        Map<String, Region> regions= zoning.getRegionsMap();
        GeoJSONWriter w = new GeoJSONWriter();
        List<Feature> features = new ArrayList<Feature>();

        //map<idzone, point)
        Map<String, Double[]> mapIdPoint = new HashMap<String, Double[]>();

        Map<String, Document<TravelData>> mapTravelDatas = Project.get().getTravelStudies();
        TravelData travelDataOrigine = mapTravelDatas.get(travelDataName).get();
        if (!zoning.isAnAggregationOf(travelDataOrigine.getZoning())) {
            return null;
        } else {
            TravelData travelData = Project.computeTravelData(zoning, travelDataOrigine);
            if (null == purposeIndex || purposeIndex == -1) {
                purposeIndex = 0;
            }
            List<ModalMatrix> modalMatrices = travelData.getOD().getMatrix(purposeIndex).getModalMatrices();

            for (Map.Entry<String, Region> entry : regions.entrySet()) {
                try {
                    Region region = entry.getValue();
                    Map<String, Object> properties = new HashMap<String, Object>();
                    properties.put("name", region.getName());
                    properties.put("id", region.getID());
                    properties.put("superZone", region.getSuperZone());

                    org.wololo.geojson.Geometry geometry = null;


                    CoordinateReferenceSystem source = CRS.decode("EPSG:2154");
                    CoordinateReferenceSystem target = CRS.decode("EPSG:4326", true);

                    MathTransform transform = CRS.findMathTransform(source, target, true);
                    com.vividsolutions.jts.geom.Geometry targetPolygon = JTS.transform(region.getPolygon(), transform);
                    Double[] point = new Double[]{targetPolygon.getInteriorPoint().getX(), targetPolygon.getInteriorPoint().getY()};
                    properties.put("XY", point);
                    properties.put("mapPointValue", getMapPointValue(modalMatrices, region));
                    //properties.put("mapPointValue2", getMapPointValue(modalMatrices, region, 1));
                    //properties.put("mapPointValue3", getMapPointValue(modalMatrices, region, 2));
                    mapIdPoint.put(region.getID(), point);
                    //if (purposeIndex == null) {//first time call this projection
                        geometry = w.write(targetPolygon);
                    //}
                    features.add(new Feature(geometry, properties));

                } catch (FactoryException e) {
                    e.printStackTrace();
                } catch (TransformException e) {
                    e.printStackTrace();
                }
            }
            outputMap.put("purposes", travelData.getPurposes());
            outputMap.put("modes", travelData.getModes());
        }

        outputMap.put("features", features);
        outputMap.put("mapIdPoint", mapIdPoint);
        return features;
    }


    public static Map<String, Double> getMapPointValue(List<ModalMatrix> modalMatrices, Zone zone, Integer moyenTransportIndex) {
        Map<String, Double> mapPointValue = new HashMap<String, Double>();

        ModalMatrix modalMatrix = modalMatrices.get(moyenTransportIndex);
        Map<Zone, Double> zonalVector = (Map<Zone, Double>) modalMatrix.getVector(zone).getVector();
        if(zonalVector!=null) {
            for (Map.Entry<Zone, Double> entry : zonalVector.entrySet()) {
                mapPointValue.put(entry.getKey().getID(), entry.getValue());
            }
        }

        return mapPointValue;
    }

    public static List<Map<String, Double>> getMapPointValue(List<ModalMatrix> modalMatrices, Zone zone) {
        List<Map<String, Double>> listMapPointValue = new ArrayList<Map<String, Double>>();

        for(int i=0;i<modalMatrices.size();i++){
            Map<String, Double> mapPointValue = new HashMap<String, Double>();
            ModalMatrix modalMatrix = modalMatrices.get(i);

            Map<Zone, Double> zonalVector = (Map<Zone, Double>) modalMatrix.getVector(zone).getVector();
            if(zonalVector!=null) {
                for (Map.Entry<Zone, Double> entry : zonalVector.entrySet()) {
                    mapPointValue.put(entry.getKey().getID(), entry.getValue());
                }
            }
            listMapPointValue.add(mapPointValue);
        }

        return listMapPointValue;
    }
}
