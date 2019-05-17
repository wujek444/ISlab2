package view;

import com.google.common.base.Joiner;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LaptopView {
    private JButton readTextFileButton;
    private JButton saveToTextButton;
    private JButton loadXMLButton;
    private JButton saveToXMLButton;
    private JTable laptopTable;
    private JPanel laptopViewPanel;
    private JScrollPane laptopViewScrollPane;
    private JButton dbImportBtn;
    private JButton dbExportBtn;

    final private JFileChooser fileChooser = new JFileChooser();
    final private FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("*.txt", "txt");
    final private FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("*.xml", "xml");

    final private TextFileReader textFileReader = new TextFileReader();
    final private LaptopTxtParser txtParser = new LaptopTxtParser();

    final private Logger logger = Logger.getAnonymousLogger();


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


        readTextFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(txtFilter);
                int returnVal = fileChooser.showOpenDialog(laptopViewPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File txtFile = fileChooser.getSelectedFile();
                    LaptopView.this.loadDataFromTxtFile(txtFile);
                    Logger.getAnonymousLogger().log(Level.INFO,"Opening: " + txtFile.getName() + ".\n");
                } else {
                    logger.log(Level.INFO, "Open command cancelled by user.\n");
                }
            }
        });

        saveToTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector dataVector = laptopTableModel.getDataVector();
                List<String> lines = new ArrayList<>();

                for(Object element : dataVector) {
                    lines.add( Joiner.on(";").join( (List<String>) element) + ";");
                }

                fileChooser.setFileFilter(txtFilter);
                int returnVal = fileChooser.showSaveDialog(laptopViewPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File txtFileToSave = fileChooser.getSelectedFile();
                    if (FilenameUtils.getExtension(txtFileToSave.getName()).equalsIgnoreCase("txt")) {
                        // dobre rozszerzenie
                    } else {
                        txtFileToSave = new File(txtFileToSave.toString() + ".txt");
                        txtFileToSave = new File(txtFileToSave.getParentFile(),
                                FilenameUtils.getBaseName(txtFileToSave.getName()) + ".txt");
                    }
                    try {
                        FileUtils.writeStringToFile(txtFileToSave, Joiner.on("\n").join(lines));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    logger.log(Level.INFO, "Saving: " + txtFileToSave.getName() + ".\n");
                } else {
                    logger.log(Level.INFO, "Save command cancelled by user.\n");
                }
            }
        });

        saveToXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                                    FilenameUtils.getBaseName(xmlFileToSave.getName()) + ".xml");
                        }

                        jaxbContext = JAXBContext.newInstance(LaptopList.class);
                        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                        jaxbMarshaller.marshal(laptopList, xmlFileToSave);
                        jaxbMarshaller.marshal(laptopList, System.out);

                        logger.log(Level.INFO, "Saving: " + xmlFileToSave.getName() + ".\n");
                    } else {
                        logger.log(Level.INFO, "Save command cancelled by user.\n");
                    }
                } catch (JAXBException e1) {
                    e1.printStackTrace();
                }
            }
        });

        loadXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(xmlFilter);
                int returnVal = fileChooser.showOpenDialog(laptopViewPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File xmlFile = fileChooser.getSelectedFile();
                    LaptopView.this.loadDataFromXmlFile(xmlFile);
                    logger.log(Level.INFO, "Opening: " + xmlFile.getName() + ".\n");
                } else {
                    logger.log(Level.INFO, "Open command cancelled by user.\n");
                }
            }
        });

        dbImportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LaptopView.this.importFromDB();
            }
        });

        dbExportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private List<Laptop> mapResultSetToLaptops(ResultSet resultSet) throws SQLException {
        List<Laptop> foundLaptops = new ArrayList<>();
        while(resultSet.next()) {
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

    private void importFromDB() {
        try {
            connection = DriverManager.getConnection(dbURL);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = "select count(id) from Laptop";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            Integer rowsCount = resultSet.getInt(1);
            logger.log(Level.INFO, "Laptop rows count: " + rowsCount);

            query = "select * from Laptop";
            resultSet = statement.executeQuery(query);

            List<Laptop> foundLaptops = mapResultSetToLaptops(resultSet);
            if(foundLaptops.size() > 0) {
                fillTableModelWithLaptopData(foundLaptops);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exportToDB() {

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

    String dbURL = "jdbc:odbc:auto";
    String query;
    Connection connection;
    Statement statement;
    ResultSet resultSet;

    public static void main(String[] args) {
        JFrame frame = new JFrame("LaptopView");
        frame.setContentPane(new LaptopView().laptopViewPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch(ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
