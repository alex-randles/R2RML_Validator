package JavaClasses;

public class Disjoint {

    public static boolean validateDisjointClasses(String[] classesURI){
        // return false if any classes defined are disjoint
        try{
            boolean disjointClasses = false;
            for (String currentClassURI : classesURI) {
                for (String comparisonURI : classesURI) {
                    String query = String.format("ASK {<%s> <http://www.w3.org/2002/07/owl#disjointWith> <%s> } ", comparisonURI, currentClassURI);
                    boolean result = SPARQL.askQuery(comparisonURI, query);
                    if (result) {
                        disjointClasses = true;
                    }

                }
            }

            return disjointClasses;
        }
        catch(Exception e){
            System.out.println("DISJOINT CLASSES ERROR " + e);
        }
        return false;

    }


}
