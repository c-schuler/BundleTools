package example.bundle;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    /**
     * This method consolidates a directory (arg[0]) of transaction bundles into a single transaction bundle and outputs a new file to the specified output directory (arg[1]).
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Please provide the input dir where the bundles are located and the desired output directory");
        }

        FhirContext context = FhirContext.forDstu3();
        Bundle consolidatedBundle = new Bundle();
        consolidatedBundle.setType(Bundle.BundleType.TRANSACTION);

        boolean isJson = true;

        // read files from directory and build master bundle
        try {
            for (File file : new File(args[0]).listFiles()) {
                FileReader reader = new FileReader(file);
                IBaseResource resource;
                if (file.getName().endsWith(".xml")) {
                    isJson = false;
                    resource = context.newXmlParser().parseResource(reader);
                }
                else if (file.getName().endsWith(".json")) {
                    resource = context.newJsonParser().parseResource(reader);
                }
                else {
                    throw new IllegalArgumentException("Invalid file: " + file.getName() + " files must have either .json or .xml extensions");
                }

                if (resource instanceof Bundle) {
                    for (Bundle.BundleEntryComponent entry : ((Bundle) resource).getEntry()) {
                        consolidatedBundle.addEntry(entry);
                    }
                }
                else {
                    // skip resources that are not bundles
                    continue;
                }
            }
        } catch (NullPointerException npe) {
            throw new IllegalArgumentException("Error reading directory: " + npe.getMessage());
        }

        // print master bundle to output directory
        try {
            Path outputPath = Paths.get(args[1]);
            File output = new File(outputPath.toString(), isJson ? "master.json" : "master.xml");
            output.createNewFile();
            PrintWriter writer = new PrintWriter(new FileWriter(output));
            writer.println(isJson ? context.newJsonParser().encodeResourceToString(consolidatedBundle) : context.newXmlParser().encodeResourceToString(consolidatedBundle));
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Error creating output: " + ioe.getMessage());
        }
    }
}
