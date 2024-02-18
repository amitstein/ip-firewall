package org.amitstein;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class IPService {

    @ConfigProperty(name = "ip.file") 
    private String fileName;
    private final IPRepository ipRepository;

    public IPService () {
        ipRepository = new IPRepository();
    }

    public boolean exists(String ip) {
        return ipRepository.exists(ip);
    }
    
    @Scheduled(every="30s", concurrentExecution=ConcurrentExecution.SKIP) 
    public void updateIPRepository() {
        long startTime = System.nanoTime();
        readFileFromResources();
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
    }

    public void retrieveData() throws IOException, InterruptedException {
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
            FileOutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(fileContent);
            outputStream.close();
            System.out.println("File downloaded successfully.");
        } else {
            System.out.println("Failed to download file. Status code: " + downloadResponse.statusCode());
        }
    }
    
    private void readFileFromResources() {
        try (InputStreamReader inputStreamReader = new FileReader(fileName);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    String[] parts = line.split("\t");
                    ipRepository.add(parts[0]);   
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
