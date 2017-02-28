package fr.systemx.mic.resource;

import com.artelys.mic.gen.calibration.GenerationProjection;
import com.artelys.mic.gen.module.GenProjectExtension;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.documents.Document;
import com.artelys.mic.project.documents.DocumentSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;

@Path("/hello")
public class HelloWorldService {
    @GET
    @Path("/projections")
    public Response getProj() {
        String output = "";
        Collection<DocumentSet<?>> documentSets=Project.get().getDocumentSets();

        Map<String, Document<GenerationProjection>> docs = GenProjectExtension.getGenerationProjections(Project.get());

        for (Map.Entry entry : docs.entrySet()) {
            output += entry.getKey() + "\n";
        }
        for(DocumentSet<?> document :documentSets){
            output+=document.getNameDocumentSet()+"\n";
        }
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/projection/{param}")
    public Response getProj(@PathParam("param") String msg) {
        String output = "";

        Map<String, Document<GenerationProjection>> docs = GenProjectExtension.getGenerationProjections(Project.get());

        GenerationProjection obj = docs.get(msg).get();

        ObjectMapper mapper = new ObjectMapper();

       // Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

        //String jsonInString="";
        //jsonInString = gson.toJson(obj);

        GeoJSONWriter w = new GeoJSONWriter();
        Geometry j = w.write(obj.getZoning().getRegions().iterator().next().getPolygon());

        String jsonInString=j.toString();

        GeoJSONReader r = new GeoJSONReader();
        com.vividsolutions.jts.geom.Geometry j2 =r.read(jsonInString);
        return Response.status(200).entity(jsonInString).build();

    }
}