package JavaClasses;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Output {

    public static void printTriples(String subject, String predicate, String object){
        System.out.println(String.format("%s %s %s", subject, predicate, object));
    }

    public static void writeFile(String content, String outputFile) throws IOException {
        File file = new File(outputFile);
        FileWriter fr = new FileWriter(file, true);
        fr.write(content);
        fr.close();
    }

}
