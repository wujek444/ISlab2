package ws;

import javax.xml.ws.Endpoint;

public class LaptopPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/laptops", new LaptopWSImpl());
    }
}
