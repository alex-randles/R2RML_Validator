package JavaClasses;

public class Disjoint {

    public static boolean validateDisjointClasses(String[] classesURI){
        try{
            for (int i=0; i< classesURI.length-1;i++) {
                for (int j=i+1; j< classesURI.length;j++) {
                    String query = String.format(
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                            "ASK {<%s> owl:disjointWith <%s> } ", classesURI[j], classesURI[i]);
                    boolean result = SPARQL.askQuery(classesURI[j], query);
                    if (result) {
                        return false;
                    }
                }
            }
            return true;
        }
        catch(Exception e){
            return true;
        }
    }

}
