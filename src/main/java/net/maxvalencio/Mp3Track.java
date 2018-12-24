package net.maxvalencio;

import javafx.beans.property.SimpleStringProperty;
import net.maxvalencio.interfaces.Track;

import java.io.Serializable;

public class Mp3Track implements Track, Serializable {
    private SimpleStringProperty name;
    private SimpleStringProperty path;

    public Mp3Track(){}

    public Mp3Track(String name, String path){
        this.name = new SimpleStringProperty(name);
        this.path =  new SimpleStringProperty(path);
    }

    public String getName() {
        return name.get();
    }

    public String getPath() {
        return  path.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPath(String path){
        this.path.set(path);
    }

    public SimpleStringProperty nameProperty(){ return name; }

    public SimpleStringProperty pathProperty(){ return path; }

    public String toString(){
        return getName();
    }

}
