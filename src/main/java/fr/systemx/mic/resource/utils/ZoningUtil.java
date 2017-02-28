package fr.systemx.mic.resource.utils;

import com.artelys.mic.data.zones.Region;
import com.artelys.mic.data.zones.Zoning;
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
 * Created by ba-linh.nguyen on 28/10/2016.
 */
public class ZoningUtil {
    /*public static void initAttributesFromProjection(){
        Map<String, Document<ZonalData>> mapZonalDatas = Project.get().getZonalDatas();
        //ZonalData zonalData = mapZonalDatas.entrySet().iterator().next().getValue().get();
        ZonalData zonalData = mapZonalDatas.get("zonalData_2010").get();

        Map<String, Map<String, Double>> attributeMap = new HashMap<String, Map<String, Double>>();//Map<nameAtrribute, Map<zoneId, value>>
        List<String> projectionAttributeNames = zonalData.getAttributeNames();
        List<ZonalVector> projectionAttributes = zonalData.getAttributes();

        for(int i=0;i<projectionAttributeNames.size();i++){
            Map<String, Double> attributes = new HashMap<String, Double>();
            Map<Zone, Double> atts = (Map<Zone, Double>) projectionAttributes.get(i).getVector();
            for(Map.Entry<Zone, Double> entry : atts.entrySet()){
                attributes.put(entry.getKey().getID(), entry.getValue());
            }
            attributeMap.put(projectionAttributeNames.get(i), attributes);
        }
        DataStatic.mapAttribute=attributeMap;
    }*/

    public static List<Feature> zoningToGeoJSON(Zoning zoning, String zonalDataName, Integer attributeNameIndex){
        //get all polygons of root zoning
        Map<String, Region> regions= zoning.getRegionsMap();
        GeoJSONWriter w = new GeoJSONWriter();
        List<Feature> features = new ArrayList<Feature>();

        Map<String, Double> attribute = new HashMap<String, Double>();

        if(zonalDataName!=null&&!"".equals(zonalDataName)){
            attribute = ZonalDataUtil.getZonalData(zoning, zonalDataName, attributeNameIndex);
        }

        for (Map.Entry<String, Region> entry:regions.entrySet()){
            try {
                Region region = entry.getValue();
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put("name", region.getName());
                properties.put("id", region.getID());
                properties.put("superZone", region.getSuperZone());
                properties.put("attribute", attribute.get(region.getID()));

                /*CoordinateReferenceSystem source = CRS.decode("EPSG:2154");
                CoordinateReferenceSystem target = CRS.decode("EPSG:4326",true);

                MathTransform transform = CRS.findMathTransform(source, target, true);
                com.vividsolutions.jts.geom.Geometry targetPolygon = JTS.transform(region.getPolygon(), transform);

                features.add(new Feature(w.write(targetPolygon), properties));*/

                org.wololo.geojson.Geometry geometry=null;

                if(attributeNameIndex==null||attributeNameIndex==-1) {//first time call this projection
                    CoordinateReferenceSystem source = CRS.decode("EPSG:2154");
                    CoordinateReferenceSystem target = CRS.decode("EPSG:4326",true);

                    MathTransform transform = CRS.findMathTransform(source, target, true);
                    com.vividsolutions.jts.geom.Geometry targetPolygon = JTS.transform(region.getPolygon(), transform);
                    properties.put("XY", new Double[]{targetPolygon.getInteriorPoint().getX(), targetPolygon.getInteriorPoint().getY()});

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

    public static List<Feature> zoningToGeoJSON(Zoning zoning){
        //get all polygons of root zoning
        Map<String, Region> regions= zoning.getRegionsMap();
        GeoJSONWriter w = new GeoJSONWriter();
        List<Feature> features = new ArrayList<Feature>();

        //for test get first attribute
        //Map<String, Double> attribute = DataStatic.mapAttribute.entrySet().iterator().next().getValue();

        for (Map.Entry<String, Region> entry:regions.entrySet()){
            try {
                Region region = entry.getValue();
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put("name", region.getName());
                properties.put("id", region.getID());
                properties.put("superZone", region.getSuperZone());
                //properties.put("population", attribute.get(region.getID()));

                CoordinateReferenceSystem source = CRS.decode("EPSG:2154");
                CoordinateReferenceSystem target = CRS.decode("EPSG:4326",true);

                MathTransform transform = CRS.findMathTransform(source, target, true);
                com.vividsolutions.jts.geom.Geometry targetPolygon = JTS.transform(region.getPolygon(), transform);

                features.add(new Feature(w.write(targetPolygon), properties));

            } catch (FactoryException e) {
                e.printStackTrace();
            } catch (TransformException e) {
                e.printStackTrace();
            }
        }
        return features;
    }
}
