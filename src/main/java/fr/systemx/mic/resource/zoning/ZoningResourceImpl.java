package fr.systemx.mic.resource.zoning;

import com.artelys.mic.data.containers.ZonalData;
import com.artelys.mic.data.zones.Region;
import com.artelys.mic.data.zones.Zoning;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;
import fr.systemx.mic.data.tree.DataStatic;
import fr.systemx.mic.data.tree.ZoningNodeDto;
import fr.systemx.mic.resource.utils.ZonalDataUtil;
import fr.systemx.mic.resource.utils.ZoningUtil;
import org.apache.log4j.Logger;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.ws.rs.core.Response;
import java.util.*;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.geotools.referencing.CRS;
import org.geotools.geometry.jts.JTS;

/**
 * Created by ba-linh.nguyen on 21/10/2016.
 */
public class ZoningResourceImpl implements ZoningResource {
    private static final transient Logger LOG = Logger.getLogger(ZoningResourceImpl.class);

    public Response getZoning(String id) {
        LOG.info("getZoning");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        if("-1".equals(id)){
            Zoning rootZoning = Project.get().getRootZoning();
            id="null_"+Project.get().getRootZoning().getName();
            outputMap.put("id", id);
            DataStatic.mapZoning.put("null_"+rootZoning.getName(), rootZoning);
            DataStatic.mapZoningName.put("null_"+rootZoning.getName(), rootZoning.getName());
        }

        List<Feature> features = ZoningUtil.zoningToGeoJSON(DataStatic.mapZoning.get(id));
        outputMap.put("features", features);
        return Response.ok(outputMap).build();
    }

    public static Map<String, Object> getTreeZoning(Map<String, Object> outputMap){
        Zoning rootZoning = Project.get().getRootZoning();
        ZoningNodeDto rootNode = new ZoningNodeDto(null, rootZoning.getName());

        createTreeZoningNode(rootNode,rootZoning);
        outputMap.put("rootNode", rootNode);

        return outputMap;
    }

    public static void createTreeZoningNode(ZoningNodeDto zoningNodeDto, Zoning zoning){
        Map<String, Document<ZonalData>> mapZonalDatas = Project.get().getZonalDatas();
        if(Project.get().getZoningChildren(zoning).size()>0){
            for(Zoning zn: Project.get().getZoningChildren(zoning)){
                ZoningNodeDto zoningNodeDtoChild = new ZoningNodeDto(zoningNodeDto.getName(), zn.getName());
                zoningNodeDto.getEnfants().add(zoningNodeDtoChild);
                createTreeZoningNode(zoningNodeDtoChild, zn);

                //remplir datastatic mapZoning
                DataStatic.mapZoning.put(zoningNodeDto.getName()+"_"+zn.getName(), zn);
                DataStatic.mapZoningName.put(zoningNodeDto.getName()+"_"+zn.getName(), zn.getName());
            }
        }
    }

    public Response getZonningZonalData(String id, String zonalDataName, Integer attributeNameIndex){
        LOG.info("getZonning()");
        Map<String, Object> outputMap = new TreeMap<String, Object>();

        List<Feature> features = ZoningUtil.zoningToGeoJSON(DataStatic.mapZoning.get(id),zonalDataName, attributeNameIndex);
        outputMap.put("features", features);
        if(zonalDataName!=null&&attributeNameIndex==null){
            outputMap.put("attributeNames", ZonalDataUtil.getAttributeList(zonalDataName));
        }

        return Response.ok(outputMap).build();
    }

}