//Objective: to get geo-coordinates of a String Address.
package com.easyfix.geocode.latlong;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;



public class AddressConverter {
 /* Google maps web server returns JSON or XML as response which we can 
  * parse to get desired result.  I have used Jackson API to convert json response in java Objects ,
  *  and apache commons IO to make Http calls to geocoding api .
  * Geocode request URL- Here see we are passing "json" it means we will get
  * the output in JSON format. we can also pass "xml" instead of "json" for
  * XML output. For XML output URL will be
  * "http://maps.googleapis.com/maps/api/geocode/xml";
  * geocoding details: https://developers.google.com/maps/documentation/geocoding/intro
  */

 private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";
 private static final String KEY = "AIzaSyDG_spqmqd2drl1vchLrqAP-dl6dL6Y_ng";

 /*
  * Here the fullAddress String is in format like
  * "address,city,state,zipcode". Here address means "street number + route"
  * .
  */
 public GoogleResponse convertToLatLong(String fullAddress) throws IOException {

  /*
   * Create an java.net.URL object by passing the request URL in
   * constructor. Here  I am converting the fullAddress String
   * in UTF-8 format. You will get Exception if you don't convert your
   * address in UTF-8 format. Perhaps google loves UTF-8 format. :) In
   * parameter we also need to pass "sensor" parameter. sensor (required
   * parameter) � Indicates whether or not the geocoding request comes
   * from a device with a location sensor. This value must be either true
   * or false.It was leter depricated.
   */
  URL url = new URL(URL + "?address="
    + URLEncoder.encode(fullAddress, "UTF-8") + "&key=" + KEY);
  // Open the Connection http://docs.oracle.com/javase/7/docs/api/overview-summary.html
  URLConnection conn = url.openConnection();

  InputStream in = conn.getInputStream() ;
  ObjectMapper mapper = new ObjectMapper();
  /*public class ObjectMapper 
   * extends com.fasterxml.jackson.core.ObjectCodec 
   * implements com.fasterxml.jackson.core.Versioned
  This mapper (or, data binder, or codec) provides functionality for converting between Java objects
  (instances of JDK provided core classes, beans), and matching JSON constructs.
  It will use instances of JsonParser and JsonGenerator for implementing actual reading/writing of JSON.
  http://fasterxml.github.io/jackson-databind/javadoc/2.0.0/overview-summary.html
  */
  GoogleResponse response = (GoogleResponse)mapper.readValue(in,GoogleResponse.class);
  in.close();
  return response;
  

 }
 
 public GoogleResponse convertFromLatLong(String latlongString) throws IOException {

  /*
   * Create an java.net.URL object by passing the request URL in
   * constructor. Here I am converting the fullAddress String
   * in UTF-8 format. we will get Exception if you don't convert your
   * address in UTF-8 format. Perhaps google loves UTF-8 format. :) In
   * parameter we also need to pass "sensor" parameter. sensor (required
   * parameter) � Indicates whether or not the geocoding request comes
   * from a device with a location sensor. This value must be either true
   * or false.
   */
  URL url = new URL(URL + "?latlng="
    + URLEncoder.encode(latlongString, "UTF-8") + "&key=" + KEY + "&sensor=false");
  // Open the Connection
  URLConnection conn = url.openConnection();

  InputStream in = conn.getInputStream() ;
  ObjectMapper mapper = new ObjectMapper();
  GoogleResponse response = (GoogleResponse)mapper.readValue(in,GoogleResponse.class);
  in.close();
  return response;
  

 }

 
 public static void main(String[] args) throws IOException {
  
  GoogleResponse res = new AddressConverter().convertToLatLong("1,delhi,india");
  if(res.getStatus().equals("OK"))
  {
   for(Result result : res.getResults())
   {
    System.out.println("Lattitude of address is :"  +result.getGeometry().getLocation().getLat());
    System.out.println("Longitude of address is :" + result.getGeometry().getLocation().getLng());
    System.out.println("Location is " + result.getGeometry().getLocation_type());
    System.out.println(result.getGeometry().getLocation().getLat()+","+result.getGeometry().getLocation().getLng());
    addressComponent[] addCompo= result.getAddress_components();
    for(int i=0; i<addCompo.length; i++){
    	String[] pin=addCompo[i].getTypes();
    	  for(int j=pin.length-1; j>=0; j--){
    	    	if(pin[j].equalsIgnoreCase("postal_code")){
    	    		System.out.println(addCompo[i].getLong_name());
    	    	//	System.out.println(addCompo[i].getShort_name());
    	    		break;
    	    	}
    	    	
    	    }
    }
   }
  }
  else
  {
   System.out.println(res.getStatus());
  }
  
  System.out.println("\n");
  GoogleResponse res1 = new AddressConverter().convertFromLatLong("28.5133929,77.07227589999999");
  if(res1.getStatus().equals("OK"))
  {
   for(Result result : res1.getResults())
   {
    System.out.println("address is :"  +result.getFormatted_address());
   }
  }
  else
  {
   System.out.println(res1.getStatus());
  }
  
 }
 

}