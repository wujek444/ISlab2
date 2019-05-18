package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class Laptop {
    private String manufacturer;
    private String matrixSize;
    private String resolution;
    private String matrixCoating;
    private String touchPad;
    private String cpuFamily;
    private String coresCount;
    private String clockSpeed;
    private String ram;
    private String driveCapacity;
    private String driveType;
    private String gpu;
    private String gpuMemory;
    private String os;
    private String opticalDrive;

    public Laptop() {
    }

    public Laptop(String manufacturer, String matrixSize, String resolution, String matrixCoating, String touchPad, String cpuFamily, String coresCount, String clockSpeed, String ram, String driveCapacity, String driveType, String gpu, String gpuMemory, String os, String opticalDrive) {
        this.manufacturer = manufacturer;
        this.matrixSize = matrixSize;
        this.resolution = resolution;
        this.matrixCoating = matrixCoating;
        this.touchPad = touchPad;
        this.cpuFamily = cpuFamily;
        this.coresCount = coresCount;
        this.clockSpeed = clockSpeed;
        this.ram = ram;
        this.driveCapacity = driveCapacity;
        this.driveType = driveType;
        this.gpu = gpu;
        this.gpuMemory = gpuMemory;
        this.os = os;
        this.opticalDrive = opticalDrive;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMatrixSize() {
        return matrixSize;
    }

    public void setMatrixSize(String matrixSize) {
        this.matrixSize = matrixSize;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getMatrixCoating() {
        return matrixCoating;
    }

    public void setMatrixCoating(String matrixCoating) {
        this.matrixCoating = matrixCoating;
    }

    public String getTouchPad() {
        return touchPad;
    }

    public void setTouchPad(String touchPad) {
        this.touchPad = touchPad;
    }

    public String getCpuFamily() {
        return cpuFamily;
    }

    public void setCpuFamily(String cpuFamily) {
        this.cpuFamily = cpuFamily;
    }

    public String getCoresCount() {
        return coresCount;
    }

    public void setCoresCount(String coresCount) {
        this.coresCount = coresCount;
    }

    public String getClockSpeed() {
        return clockSpeed;
    }

    public void setClockSpeed(String clockSpeed) {
        this.clockSpeed = clockSpeed;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getDriveCapacity() {
        return driveCapacity;
    }

    public void setDriveCapacity(String driveCapacity) {
        this.driveCapacity = driveCapacity;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getGpuMemory() {
        return gpuMemory;
    }

    public void setGpuMemory(String gpuMemory) {
        this.gpuMemory = gpuMemory;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOpticalDrive() {
        return opticalDrive;
    }

    public void setOpticalDrive(String opticalDrive) {
        this.opticalDrive = opticalDrive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laptop laptop = (Laptop) o;
        return Objects.equals(manufacturer, laptop.manufacturer) &&
                Objects.equals(matrixSize, laptop.matrixSize) &&
                Objects.equals(resolution, laptop.resolution) &&
                Objects.equals(matrixCoating, laptop.matrixCoating) &&
                Objects.equals(touchPad, laptop.touchPad) &&
                Objects.equals(cpuFamily, laptop.cpuFamily) &&
                Objects.equals(coresCount, laptop.coresCount) &&
                Objects.equals(clockSpeed, laptop.clockSpeed) &&
                Objects.equals(ram, laptop.ram) &&
                Objects.equals(driveCapacity, laptop.driveCapacity) &&
                Objects.equals(driveType, laptop.driveType) &&
                Objects.equals(gpu, laptop.gpu) &&
                Objects.equals(gpuMemory, laptop.gpuMemory) &&
                Objects.equals(os, laptop.os) &&
                Objects.equals(opticalDrive, laptop.opticalDrive);
    }

    @Override
    public int hashCode() {

        return Objects.hash(manufacturer, matrixSize, resolution, matrixCoating, touchPad, cpuFamily, coresCount, clockSpeed, ram, driveCapacity, driveType, gpu, gpuMemory, os, opticalDrive);
    }
}
