package JavaClasses;

import org.apache.jena.base.Sys;

public class Disjoint {

    public static void main(String[] args){
        String[] classes = new String[]{"http://dbpedia.org/ontology/Person", "http://dbpedia.org/ontology/Building", "http://dbpedia.org/ontology/Test", "http://dbpedia.org/ontology/Test"};
        System.out.println(validateDisjointClasses(classes));
    }
    public static boolean validateDisjointClasses(String[] classesURI){
        // return false if any classes defined are disjoint
        try{
            for (int i=0; i<classesURI.length-1;i++) {
                for (int j=i+1; j<classesURI.length;j++) {
                    String query = String.format("ASK {<%s> <http://www.w3.org/2002/07/owl#disjointWith> <%s> } ", classesURI[j], classesURI[i]);
                    System.out.println(query);
                    boolean result = SPARQL.askQuery(classesURI[j], query);
                    System.out.println(result);
                    if (result) {
                        return false;
                    }

                }
            }

            return true;
        }
        catch(Exception e){
            System.out.println("DISJOINT CLASSES ERROR " + e);
        }
        return false;

    }


}
