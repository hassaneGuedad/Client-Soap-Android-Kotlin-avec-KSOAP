package ma.projet.server;

import javax.xml.ws.Endpoint;
import java.util.logging.Logger;

public class ServerJWS {
    private static final Logger LOG = Logger.getLogger(ServerJWS.class.getName());

    public static void main(String[] args) {
        String url = "http://0.0.0.0:8082/services/ws";
        CompteServiceImpl implementor = new CompteServiceImpl();
        Endpoint endpoint = Endpoint.publish(url, implementor);
        if (endpoint.isPublished()) {
            LOG.info("Service deployed at " + url + "?wsdl");
            System.out.println("Service deployed at " + url + "?wsdl");
        } else {
            LOG.severe("Failed to publish service");
            System.err.println("Failed to publish service");
        }

        // Keep main thread alive
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

