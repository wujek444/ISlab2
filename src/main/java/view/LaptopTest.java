package view;

import com.google.common.base.Joiner;
import model.Laptop;
import org.apache.commons.io.FileUtils;
import util.RobotUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaptopTest {
    private JTextField manufacturerTxt;
    private JTextField matrixSizeTxt;
    private JTextField resolutionTxt;
    private JTextField matrixCoatingTxt;
    private JTextField touchScreenTxt;
    private JTextField cpuSeriesTxt;
    private JTextField coresCountTxt;
    private JTextField clockSpeedTxt;
    private JTextField ramTxt;
    private JTextField driveCapacityTxt;
    private JTextField driveTypeTxt;
    private JTextField gpuTxt;
    private JTextField gpuMemoryTxt;
    private JTextField osTxt;
    private JTextField opticalDriveTxt;
    private JTextField manufacturer4ReportTxt;
    private JCheckBox matrixSizeCB;
    private JCheckBox resolutionCB;
    private JCheckBox manufacturerCB;
    private JCheckBox driveCapacityCB;
    private JCheckBox ramCB;
    private JCheckBox touchPadCB;
    private JCheckBox cpuFamilyCB;
    private JCheckBox matrixCoatingCB;
    private JCheckBox clockSpeedCB;
    private JCheckBox coresCountCB;
    private JCheckBox driveTypeCB;
    private JCheckBox gpuCB;
    private JCheckBox gpuMemoryCB;
    private JCheckBox osCB;
    private JCheckBox opticalDriveCB;
    private JButton beginTestBtn;
    private JPanel laptopTestPanel;

    private static JFrame frame;
    final private Logger logger = Logger.getAnonymousLogger();
    private static final String SCREENSHOTS_PATH = "C:/Users/jrwoj/Pictures/bsi - screeny/";


    public LaptopTest() {
        initGuiComponents();
    }

    public static void main(String[] args) {
        frame = new JFrame("LaptopTest");
        frame.setContentPane(new LaptopTest().laptopTestPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void initGuiComponents() {
        beginTestBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                beginTest();
            }
        });
    }

    private void beginTest() {
        try {
            addLaptopTest();
        } catch (AWTException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addLaptopTest() throws AWTException, InterruptedException, IOException {
        saveInputToTextFile();
        focusToWindow("LaptopView");
        TimeUnit.SECONDS.sleep(1);
        clickReadFromTxt();
        TimeUnit.SECONDS.sleep(1);
        type("test.txt");
        TimeUnit.SECONDS.sleep(1);
        clickOpenInFileChooser();
        TimeUnit.SECONDS.sleep(1);
        RobotUtils.takeScreenshot(SCREENSHOTS_PATH + "addLaptopTest.bmp");
    }

    private void clickReadFromTxt() throws AWTException {
        RobotUtils.click(160,30);
    }

    private void clickFileNameInput() throws AWTException {
        RobotUtils.click(610,480);
    }

    private void clickOpenInFileChooser() throws AWTException {
        RobotUtils.click(830,545);
    }

    private void focusToWindow(String windowName) {
        RobotUtils.setFocusToWindow(windowName, 1);
    }

    private void type(String text) throws AWTException {
        RobotUtils.type(text);
    }

    private void pressEnter() throws AWTException {
        RobotUtils.pressEnter();
    }

    private void pressBackspace() throws AWTException {
        RobotUtils.pressBackspace();
    }


    private Laptop createLaptopFromInput() {
        Laptop laptop = new Laptop();
        laptop.setManufacturer(manufacturerTxt.getText());
        laptop.setMatrixSize(matrixCoatingTxt.getText());
        laptop.setResolution(resolutionTxt.getText());
        laptop.setMatrixCoating(matrixCoatingTxt.getText());
        laptop.setTouchPad(touchScreenTxt.getText());
        laptop.setCpuFamily(cpuSeriesTxt.getText());
        laptop.setCoresCount(coresCountTxt.getText());
        laptop.setClockSpeed(clockSpeedTxt.getText());
        laptop.setRam(ramTxt.getText());
        laptop.setDriveCapacity(driveCapacityTxt.getText());
        laptop.setDriveType(driveTypeTxt.getText());
        laptop.setGpu(gpuTxt.getText());
        laptop.setGpuMemory(gpuMemoryTxt.getText());
        laptop.setOs(osTxt.getText());
        laptop.setOpticalDrive(opticalDriveTxt.getText());
        return laptop;
    }

    private Vector<String> createLaptopVectorFromInput() {
        Vector laptopVector = new Vector();
        laptopVector.add(manufacturerTxt.getText());
        laptopVector.add(matrixCoatingTxt.getText());
        laptopVector.add(resolutionTxt.getText());
        laptopVector.add(matrixCoatingTxt.getText());
        laptopVector.add(touchScreenTxt.getText());
        laptopVector.add(cpuSeriesTxt.getText());
        laptopVector.add(coresCountTxt.getText());
        laptopVector.add(clockSpeedTxt.getText());
        laptopVector.add(ramTxt.getText());
        laptopVector.add(driveCapacityTxt.getText());
        laptopVector.add(driveTypeTxt.getText());
        laptopVector.add(gpuTxt.getText());
        laptopVector.add(gpuMemoryTxt.getText());
        laptopVector.add(osTxt.getText());
        laptopVector.add(opticalDriveTxt.getText());
        return laptopVector;
    }

    private void saveInputToTextFile() {
        Vector dataVector =createLaptopVectorFromInput();
        List<String> lines = new ArrayList<>();

        lines.add(Joiner.on(";").join(dataVector) + ";");

        try {
            FileUtils.writeStringToFile(new File("C:/Users/jrwoj/Documents/test.txt"), lines.get(0));
            logger.log(Level.INFO, "Saving: test.txt.\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.INFO, "Save unsuccesful.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
