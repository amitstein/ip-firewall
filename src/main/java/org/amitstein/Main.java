package org.amitstein;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(IPFirewallApp.class, args);
    }

    public static class IPFirewallApp implements QuarkusApplication {

        @Inject
        IPService ipService;

        @Override
        public int run(String... args) throws Exception {
            if (elementExists(args, "fetch")) {
                ipService.retrieveData();
            }
            else {
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
}
