package view;

import model.Laptop;
import model.LaptopList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import parser.LaptopTxtParser;
import reader.TextFileReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class LaptopView {
    private JButton readTextFileButton;
    private JButton saveToTextButton;
    private JButton loadXMLButton;
    private JButton saveToXMLButton;
    private JTable laptopTable;
    private JPanel laptopViewPanel;
    private JScrollPane laptopViewScrollPane;

    final private JFileChooser fileChooser = new JFileChooser();
    final private FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("*.txt", "txt");
    final private FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("*.xml", "xml");

    final private TextFileReader textFileReader = new TextFileReader();
    final private LaptopTxtParser txtParser = new LaptopTxtParser();


    private DefaultTableModel laptopTableModel;
    private static final String[] COLUMN_NAMES = {
            "Producent",
            "Wielkość matrycy",
            "Rozdzielczość",
            "Powłoka matrycy",
            "Ekran dotykowy",
            "Seria procesora",
            "Liczba rdzeni",
            "Taktowanie bazowe",
            "Wielkość pamięci RAM",
            "Pojemność dysku",
            "Typ dysku",
            "Karta graficzna",
            "Pamięć karty graficznej",
            "System operacyjny",
            "Napęd optyczny"

    };

    public LaptopView() {
        laptopTableModel = (DefaultTableModel) laptopTable.getModel();
        createColumns();


        readTextFileButton.addActionListener(e -> {
            fileChooser.setFileFilter(txtFilter);
            int returnVal = fileChooser.showOpenDialog(laptopViewPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File txtFile = fileChooser.getSelectedFile();
                loadDataFromTxtFile(txtFile);
                log.append("Opening: " + txtFile.getName() + ".\n");
            } else {
                log.append("Open command cancelled by user.\n");
            }
        });

        saveToTextButton.addActionListener(e -> {
            Vector dataVector = laptopTableModel.getDataVector();
            List<String> lines = new ArrayList<>();
            dataVector.forEach(element -> lines.add(String.join(";", (List<String>) element) + ";"));

            fileChooser.setFileFilter(txtFilter);
            int returnVal = fileChooser.showSaveDialog(laptopViewPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File txtFileToSave = fileChooser.getSelectedFile();
                if (FilenameUtils.getExtension(txtFileToSave.getName()).equalsIgnoreCase("txt")) {
                    // dobre rozszerzenie
                } else {
                    txtFileToSave = new File(txtFileToSave.toString() + ".txt");
                    txtFileToSave = new File(txtFileToSave.getParentFile(),
                            FilenameUtils.getBaseName(txtFileToSave.getName())+".txt");
                }
                try {
                    FileUtils.writeStringToFile(txtFileToSave, String.join("\n", lines));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                log.append("Saving: " + txtFileToSave.getName() + ".\n");
            } else {
                log.append("Save command cancelled by user.\n");
            }
        });

        saveToXMLButton.addActionListener(e -> {
            JAXBContext jaxbContext = null;
            try {
                LaptopList laptopList = new LaptopList(txtParser.parseVector(laptopTableModel.getDataVector()));

                fileChooser.setFileFilter(xmlFilter);
                int returnVal = fileChooser.showSaveDialog(laptopViewPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File xmlFileToSave = fileChooser.getSelectedFile();
                    if (FilenameUtils.getExtension(xmlFileToSave.getName()).equalsIgnoreCase("xml")) {
                        // dobre rozszerzenie
                    } else {
                        xmlFileToSave = new File(xmlFileToSave.toString() + ".xml");
                        xmlFileToSave = new File(xmlFileToSave.getParentFile(),
                                FilenameUtils.getBaseName(xmlFileToSave.getName())+".xml");
                    }

                    jaxbContext = JAXBContext.newInstance(LaptopList.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    jaxbMarshaller.marshal(laptopList, xmlFileToSave);
                    jaxbMarshaller.marshal(laptopList, System.out);

                    log.append("Saving: " + xmlFileToSave.getName() + ".\n");
                } else {
                    log.append("Save command cancelled by user.\n");
                }
            } catch (JAXBException e1) {
                e1.printStackTrace();
            }

        });

        loadXMLButton.addActionListener(e -> {
            fileChooser.setFileFilter(xmlFilter);
            int returnVal = fileChooser.showOpenDialog(laptopViewPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File xmlFile = fileChooser.getSelectedFile();
                loadDataFromXmlFile(xmlFile);
                log.append("Opening: " + xmlFile.getName() + ".\n");
            } else {
                log.append("Open command cancelled by user.\n");
            }
        });
    }

    private void createColumns() {
        for (String columnName : COLUMN_NAMES) {
            laptopTableModel.addColumn(columnName);
        }
    }

    private void loadDataFromTxtFile(File file) {
        List<String> fileLines = textFileReader.readLines(file);
        List<Laptop> laptops = txtParser.parseList(fileLines);

        if (laptops.size() > 0) {
            fillTableModelWithLaptopData(laptops);
        }
    }

    private void loadDataFromXmlFile(File file){
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(LaptopList.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            LaptopList laptopList = (LaptopList) jaxbUnmarshaller.unmarshal(file);
            System.out.println(laptopList);

            if(laptopList.getLaptop().size() > 0){
                fillTableModelWithLaptopData(laptopList.getLaptop());
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void fillTableModelWithLaptopData(List<Laptop> laptops) {
        Object[] rowData = new Object[15];
        for (Laptop laptop : laptops) {
            rowData[0] = laptop.getManufacturer();
            rowData[1] = laptop.getMatrixSize();
            rowData[2] = laptop.getResolution();
            rowData[3] = laptop.getMatrixCoating();
            rowData[4] = laptop.getTouchPad();
            rowData[5] = laptop.getCpuFamily();
            rowData[6] = laptop.getCoresCount();
            rowData[7] = laptop.getClockSpeed();
            rowData[8] = laptop.getRam();
            rowData[9] = laptop.getDriveCapacity();
            rowData[10] = laptop.getDriveType();
            rowData[11] = laptop.getGpu();
            rowData[12] = laptop.getGpuMemory();
            rowData[13] = laptop.getOs();
            rowData[14] = laptop.getOpticalDrive();
            laptopTableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LaptopView");
        frame.setContentPane(new LaptopView().laptopViewPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

    }
}
