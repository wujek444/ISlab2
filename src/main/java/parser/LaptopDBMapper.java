package parser;

import model.Laptop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LaptopDBMapper {

    public List<Laptop> mapResultSetToLaptops(ResultSet resultSet) throws SQLException {
        List<Laptop> foundLaptops = new ArrayList<>();
        while (resultSet.next()) {
            Laptop laptop = new Laptop();
            laptop.setManufacturer(resultSet.getString("manufacturer"));
            laptop.setMatrixSize(resultSet.getString("matrixSize"));
            laptop.setResolution(resultSet.getString("resolution"));
            laptop.setMatrixCoating(resultSet.getString("matrixCoating"));
            laptop.setTouchPad(resultSet.getString("touchPad"));
            laptop.setCpuFamily(resultSet.getString("cpuFamily"));
            laptop.setCoresCount(resultSet.getString("coresCount"));
            laptop.setClockSpeed(resultSet.getString("clockSpeed"));
            laptop.setRam(resultSet.getString("ram"));
            laptop.setDriveCapacity(resultSet.getString("driveCapacity"));
            laptop.setDriveType(resultSet.getString("driveType"));
            laptop.setGpu(resultSet.getString("gpu"));
            laptop.setGpuMemory(resultSet.getString("gpuMemory"));
            laptop.setOs(resultSet.getString("os"));
            laptop.setOpticalDrive(resultSet.getString("opticalDrive"));
            foundLaptops.add(laptop);
        }
        return foundLaptops;
    }

}
