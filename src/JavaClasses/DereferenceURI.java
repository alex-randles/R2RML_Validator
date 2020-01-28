package JavaClasses;

import java.net.*;


public class DereferenceURI{

    public static void main(String[] args){
        // main method for testing purposes
        System.out.println(getResponseCode("http://www.example.com/"));
    }

    public static boolean getResponseCode(String URL){
        try{
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println(URL + " produces error code " + code);
            // if URI is dereferenceable return true
            if (code == 200) {
                return true;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }


        // if URI cant be dereferenced return false
        return false;
    }


}