package org.amitstein;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.scheduler.Scheduler;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.inject.Inject;

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(IPFirewallApp.class, args);
    }

    public static class IPFirewallApp implements QuarkusApplication {
        @Inject
        Scheduler scheduler;

        @Inject
        IPService ipService; 

        @Override
        public int run(String... args) throws Exception {
            if (elementExists(args, "fetch")) {
                retrieveFile();
            }
            else {
                scheduler.newJob("updateIPRepository")
                .setConcurrentExecution(ConcurrentExecution.SKIP)
                .setCron("0/5 * * * * ?")
                .setTask(executionContext -> { 
                    ipService.updateIPRepository();
                })
                .schedule(); 
                Quarkus.waitForExit();
            }
            return 0;
        }
    }

    public static boolean elementExists(String[] array, String target) {
        for (String element : array) {
            System.out.println("arg: " + element );
            if (element.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static void retrieveFile() throws IOException, InterruptedException {
        String owner = "stamparm";
        String repo = "ipsum";
        String filePath = "master/ipsum.txt";

        // Construct the GitHub API URL to retrieve the file
        String apiUrl = String.format("https://raw.githubusercontent.com/%s/%s/%s", owner, repo, filePath);

        // Create an HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create a GET request to the GitHub API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        // Send the request and handle the response
        HttpResponse<byte[]> downloadResponse = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (downloadResponse.statusCode() == 200) {
            // If the download request was successful, write the content of the file to a local file
            byte[] fileContent = downloadResponse.body();
            FileOutputStream outputStream = new FileOutputStream("/Users/amit/ipsum.txt");
            outputStream.write(fileContent);
            outputStream.close();
            System.out.println("File downloaded successfully.");
        } else {
            System.out.println("Failed to download file. Status code: " + downloadResponse.statusCode());
        }
    }

}
