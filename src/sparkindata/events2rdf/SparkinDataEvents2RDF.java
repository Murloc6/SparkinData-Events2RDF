    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparkindata.events2rdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabien
 */
public class SparkinDataEvents2RDF {

    
    public static void exportClimateEvents(String fileName){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<Object> userData = mapper.readValue(new File("in/"+fileName), ArrayList.class);
            System.out.println("Export Climate Events :");
            StringBuilder export = new StringBuilder();
            int idEvent = 0;
            int nbNotGeo = 0;
            
            
            if(userData != null){
                for(Object e : userData){
                    HashMap<String, Object> elem = (HashMap<String, Object>) e;
                    if(elem.containsKey("title")){
                        System.out.println("TEST : "+elem.get("title").toString());
                        System.out.println("\t "+elem.get("startDate").toString());
                        System.out.println("\t "+elem.get("endDate").toString());
                        
                        Boolean exportElem = false;
                        StringBuilder elemExport = new StringBuilder();
                        idEvent++;
                        String descr = elem.get("description").toString();
                        descr = descr.replaceAll("\"","\\\\\"");
                        System.out.println(descr);
                        elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_"+idEvent+"> a <http://www.irit.fr/sparkindata#WeatherAlert>. \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_time_"+idEvent+"> a <http://purl.org/dc/terms/PeriodOfTime>. \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_time_"+idEvent+"> <http://www.w3.org/2006/time#hasBeginning> \""+elem.get("startDate").toString()+"\". \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_time_"+idEvent+"> <http://www.w3.org/2006/time#hasEnd> \""+elem.get("endDate").toString()+"\". \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_"+idEvent+"> <http://purl.org/dc/terms/description> \""+descr+"\". \n");
                        
                        ArrayList<Object> places = (ArrayList<Object>)elem.get("places");
                        for(Object elemPlace : places){
                            HashMap<String, Object> place = (HashMap<String, Object>) elemPlace;
                            if(place.containsKey("name") && place.containsKey("geometry")){
                                System.out.println("\t PLACE : "+place.get("name"));
                                HashMap<String, Object> geometry = (HashMap<String, Object>)place.get("geometry");
                                if(geometry != null){
                                    elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_geometry_"+idEvent+"> a <http://www.opengis.net/ont/geosparql#Geometry>. \n");
                                    if(geometry.containsKey("location")){
                                        exportElem = true;
                                        System.out.println("\t\t GEO : "+geometry.get("location"));
                                        HashMap<String, Object> loc = (HashMap<String, Object>) geometry.get("location");
                                        elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_geometry_"+idEvent+"> <http://www.opengis.net/ont/geosparql#asWKT> \"POINT ("+loc.get("lon").toString()+" "+loc.get("lat").toString()+")\". \n");
                                    }
                                    else if(geometry.containsKey("viewport")){
                                        exportElem = false;
                                        System.out.println("\t\t GEO view : "+geometry.get("viewport"));
                                    }
                                }
                                else{
                                    nbNotGeo ++;
                                    System.out.println("ERROR GEOMETRY : "+place);
                                }
                                if(exportElem){
                                    export.append(elemExport);
                                }
                            }
                        }
                    }
                }
                File file = new File("dataWeatherAlert.ttl");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(export.toString());
                }
                System.out.println("ALERT : "+idEvent);
                System.out.println("NOT GEO : "+nbNotGeo);
                System.out.println("TOTAL EXPORTED : "+(idEvent - nbNotGeo));
            }
        } catch (IOException ex) {
            System.err.println("ERROR FILE NOT FOUND !!"+ex);
        }
    }
    
    
     public static void exportAgriculturalEvents(String fileName){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<Object> userData = mapper.readValue(new File("in/"+fileName), ArrayList.class);
            System.out.println("Export Agricultural Events :");
            StringBuilder export = new StringBuilder();
            int idEvent = 0;
            int nbNotGeo = 0;
            
            
            if(userData != null){
                for(Object e : userData){
                    HashMap<String, Object> elem = (HashMap<String, Object>) e;
                    if(elem.containsKey("diseases")){
                        
                        Boolean exportElem = false;
                        StringBuilder elemExport = new StringBuilder();
                        idEvent++;
                        String descr = "";
                        ArrayList<Object> diseases = (ArrayList<Object>) elem.get("diseases");
                        for(Object d : diseases){
                            descr += d.toString();
                        }
                        descr = descr.replaceAll("\"","\\\\\"");
                        elemExport.append("<http://www.irit.fr/sparkindata#PestEvent_ind_"+idEvent+"> a <http://www.irit.fr/sparkindata#PestEvent>. \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#PestEvent_ind_time_"+idEvent+"> a <http://purl.org/dc/terms/PeriodOfTime>. \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#PestEvent_ind_time_"+idEvent+"> <http://www.w3.org/2006/time#hasBeginning> \""+elem.get("date").toString()+"\". \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#PestEvent_ind_time_"+idEvent+"> <http://www.w3.org/2006/time#hasEnd> \""+elem.get("date").toString()+"\". \n");
                        elemExport.append("<http://www.irit.fr/sparkindata#PestEvent_ind_"+idEvent+"> <http://purl.org/dc/terms/description> \""+descr+"\". \n");
                        
                        HashMap<String, Object> place = (HashMap<String, Object>) elem.get("place");
                        if(place.containsKey("name") && place.containsKey("geometry")){
                            System.out.println("\t PLACE : "+place.get("name"));
                            HashMap<String, Object> geometry = (HashMap<String, Object>)place.get("geometry");
                            if(geometry != null){
                                elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_geometry_"+idEvent+"> a <http://www.opengis.net/ont/geosparql#Geometry>. \n");
                                if(geometry.containsKey("location")){
                                    exportElem = true;
                                    HashMap<String, Object> loc = (HashMap<String, Object>) geometry.get("location");
                                    elemExport.append("<http://www.irit.fr/sparkindata#WeatherAlert_ind_geometry_"+idEvent+"> <http://www.opengis.net/ont/geosparql#asWKT> \"POINT ("+loc.get("lon").toString()+" "+loc.get("lat").toString()+")\". \n");
                                }
                                else if(geometry.containsKey("viewport")){
                                    exportElem = false;
                                    System.out.println("\t\t GEO view : "+geometry.get("viewport"));
                                }
                            }
                            else{
                                nbNotGeo ++;
                                System.out.println("ERROR GEOMETRY : "+place);
                            }
                            if(exportElem){
                                export.append(elemExport);
                            }
                        }
                    }
                }
                File file = new File("dataPestAlert.ttl");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(export.toString());
                }
                System.out.println("ALERT : "+idEvent);
                System.out.println("NOT GEO : "+nbNotGeo);
                System.out.println("TOTAL EXPORTED : "+(idEvent - nbNotGeo));
            }
        } catch (IOException ex) {
            System.err.println("ERROR FILE NOT FOUND !!"+ex);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        exportClimateEvents("climateEvent.json");
        exportAgriculturalEvents("pestEvent.json");
    }
    
}
