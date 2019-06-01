package client;

import dictonary.ScreenResolutionDictionary;
import ws.LaptopWS;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class LaptopClient {

    LaptopWS client;

    public LaptopClient() {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/laptops");
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        QName qName = new QName("http://ws/", "LaptopWSImplService");
        Service service = Service.create(url, qName);
        client = service.getPort(LaptopWS.class);
    }

    public Integer getLaptopCountByManufacturer(String manufacturer) {
        return client.getLaptopCountByManufacturer(manufacturer);
    }

    public Integer getLaptopCountByScreenAspectRatio(String ratio) {
        return client.getLaptopCountByScreenResolutions(ScreenResolutionDictionary.getDictionaryByAspectRatio(ratio));
    }
}
