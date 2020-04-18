package JavaClasses;

public class Disjoint {

    public static void main(String[] args){
        validateDisjointClasses(new String[] {NS.DBO_NS+"Agent", NS.DBO_NS+"Place"});
    }
    public static boolean validateDisjointClasses(String[] classesURI){
        try{
            if (classesURI.length == 1 ){
                return true;
            }
            for (int j=1; j< classesURI.length;j++) {
                    String query = String.format(
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                            "ASK {<%s> owl:disjointWith <%s> } ", classesURI[0], classesURI[j]);
                    boolean result = SPARQL.askQuery(classesURI[j], query);
                    if (result) {
                        return false;
                    }
                }
            return true;
        }
        catch(Exception e){
            return true;
        }
    }

}
