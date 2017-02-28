package fr.systemx.mic.data.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ba-linh.nguyen on 25/10/2016.
 */
public class ZoningNodeDto {
    private String parentName;
    private String name;
    private String id;
    private List<ZoningNodeDto> enfants = new ArrayList<ZoningNodeDto>();

    public ZoningNodeDto(String parentName, String name){
        this.parentName=parentName;
        this.name=name;
        this.id=parentName+"_"+name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ZoningNodeDto> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<ZoningNodeDto> enfants) {
        this.enfants = enfants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
