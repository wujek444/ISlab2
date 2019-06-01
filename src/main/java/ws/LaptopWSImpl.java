package ws;

import db.DBConnector;

import javax.jws.WebService;
import java.sql.SQLException;
import java.util.List;

@WebService(endpointInterface = "ws.LaptopWS")
public class LaptopWSImpl implements LaptopWS {
    @Override
    public Integer getLaptopCountByManufacturer(String producer) {
        Integer laptopsCount = 0;
        try {
            laptopsCount = DBConnector.getLaptopsCountByManufacturer(producer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laptopsCount;
    }

    @Override
    public Integer getLaptopCountByScreenResolution(String resolution) {
        Integer laptopsCount = 0;
        try {
            laptopsCount = DBConnector.getLaptopsCountByScreenResolution(resolution);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laptopsCount;
    }

    @Override
    public Integer getLaptopCountByScreenResolutions(List<String> resolutions) {
        Integer laptopsCount = 0;
        try {
            laptopsCount = DBConnector.getLaptopsCountByScreenResolutions(resolutions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laptopsCount;
    }
}
