package view;

import client.LaptopClient;
import com.google.common.base.Joiner;
import db.DBConnector;
import model.Laptop;
import model.LaptopList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import parser.LaptopDBMapper;
import parser.LaptopTxtParser;
import reader.TextFileReader;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
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
    private JButton clearBtn;
    private JButton raportWgNazwyProducentaButton;
    private JButton raportWgProporcjiEkranuButton;
    private JButton niestandardowyExportDoXMLButton;

    private DefaultTableModel laptopTableModel;

    final private JFileChooser fileChooser = new JFileChooser();
    final private FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("*.txt", "txt");
    final private FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("*.xml", "xml");

    final private TextFileReader textFileReader = new TextFileReader();
    final private LaptopTxtParser txtParser = new LaptopTxtParser();
    final private LaptopDBMapper laptopDBMapper = new LaptopDBMapper();

    final private Logger logger = Logger.getAnonymousLogger();

    private LaptopClient laptopClient;
    private static JFrame frame;

    private final Integer EDITED_HIDDEN_COLIDX = 15;
    private final Boolean DEFAULT_EDITED = false;
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
            "Napęd optyczny",
            "edited_hidden"
    };

    public LaptopView() {
        laptopTableModel = (DefaultTableModel) laptopTable.getModel();
        laptopClient = new LaptopClient();
        createColumns();
        initGUIComponents();
    }

    public static void main(String[] args) {
        DBConnector.loadODBCDriverClass();
        frame = new JFrame("LaptopView");
        frame.setContentPane(new LaptopView().laptopViewPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

    }

    private List<Laptop> findLaptopDuplicates(Laptop laptopToCheck) {
        List<Laptop> duplicates = new ArrayList<>();
        List<Laptop> laptopsFromTableModel = txtParser.parseVector(laptopTableModel.getDataVector());
        for (Laptop laptopFromTableModel : laptopsFromTableModel) {
            if (laptopFromTableModel.equals(laptopToCheck)) {
                duplicates.add(laptopFromTableModel);
            }
        }
        return duplicates;
    }

    private void exportToDB() {
        try {
            List<Laptop> laptopsFromModel = txtParser.parseVector(laptopTableModel.getDataVector());
            if(laptopsFromModel.size() > 0) {
                DBConnector.insertLaptops(laptopsFromModel);
                JOptionPane.showMessageDialog(null, "Wyeksportowano " + laptopsFromModel.size() + " rekordów!");
            } else {
                JOptionPane.showMessageDialog(null, "Brak danych do wyeksportowania!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void importFromDB() {
        try {
            ResultSet resultSet = DBConnector.executeQuery("select * from Laptop");

            List<Laptop> foundLaptops = laptopDBMapper.mapResultSetToLaptops(resultSet);
            Integer rowsCount = foundLaptops.size();
            if (rowsCount > 0) {
                fillTableModelWithLaptopData(foundLaptops);
            }
            showRowsCountDialogWithLog(foundLaptops);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showRowsCountDialogWithLog(List<Laptop> downloadedLaptops) {
        JOptionPane.showMessageDialog(null, "Znaleziono " + downloadedLaptops.size() + " rekordów, " +
                "a w tym " + getTotalDuplicatesCount(downloadedLaptops) + " duplikatów!");
        logger.log(Level.INFO, "Laptop rows count: " + downloadedLaptops.size());
    }

    private Integer getTotalDuplicatesCount(List<Laptop> downloadedLaptops) {
        Integer duplicatesCount = 0;
        for (Laptop downloadedLaptop : downloadedLaptops) {
            if (findLaptopDuplicates(downloadedLaptop).size() > 1)
                duplicatesCount++;
        }
        return duplicatesCount;
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
        showRowsCountDialogWithLog(laptops);
    }

    private void loadDataFromXmlFile(File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LaptopList.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            LaptopList laptopList = (LaptopList) jaxbUnmarshaller.unmarshal(file);
            System.out.println(laptopList);

            if (laptopList.getLaptop().size() > 0) {
                fillTableModelWithLaptopData(laptopList.getLaptop());
            }
            showRowsCountDialogWithLog(laptopList.getLaptop());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void fillTableModelWithLaptopData(List<Laptop> laptops) {
        Object[] rowData = new Object[16];
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
            rowData[15] = DEFAULT_EDITED;
            laptopTableModel.addRow(rowData);
        }
    }

    private void initGUIComponents() {
        laptopTable.removeColumn(laptopTable.getColumnModel().getColumn(EDITED_HIDDEN_COLIDX));

        laptopTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    if (!(Boolean) laptopTableModel.getValueAt(e.getFirstRow(), EDITED_HIDDEN_COLIDX)) {
                        laptopTableModel.setValueAt(true, e.getFirstRow(), EDITED_HIDDEN_COLIDX);
                    }
                    laptopTable.repaint();
                }
                logger.log(Level.INFO, e.toString());
            }
        });

        laptopTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Laptop rowLaptop = txtParser.parse((Vector) laptopTableModel.getDataVector().get(row));

                if ((Boolean) laptopTableModel.getValueAt(row, EDITED_HIDDEN_COLIDX)) {
                    cellComponent.setBackground(new Color(255, 255, 224));
                } else if (findLaptopDuplicates(rowLaptop).size() > 1) {
                    cellComponent.setBackground(new java.awt.Color(255, 72, 72));
                } else {
                    cellComponent.setBackground(new Color(185, 185, 158));
                }
                return cellComponent;
            }
        });

        readTextFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readFromTxtFile();
            }
        });

        saveToTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToTextFile();
            }
        });

        saveToXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToXMLFile();
            }
        });

        loadXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readFromXMLFile();
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
                exportToDB();
            }
        });

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                laptopTableModel.setRowCount(0);
            }
        });

        raportWgNazwyProducentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String manufacturer = (String)JOptionPane.showInputDialog(
                        frame,
                        "Podaj nazwę producenta:",
                        "Liczba wg nazwy producenta",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "Samsung");

                if ((manufacturer != null) && (manufacturer.length() > 0)) {
                    Integer laptopCountByManufacturer = laptopClient.getLaptopCountByManufacturer(manufacturer);
                    JOptionPane.showMessageDialog(frame,
                            "Znaleziono " + laptopCountByManufacturer + " laptopów o nazwie producenta: " + manufacturer + ".");
                }
            }
        });

        raportWgProporcjiEkranuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] possibilities = {"16:9", "16:10"};
                String aspectRatio = (String)JOptionPane.showInputDialog(
                        frame,
                        "Wybierz proporcję ekranu",
                        "Liczba wg proporcji ekranu",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        "16:9");

                if ((aspectRatio != null) && (aspectRatio.length() > 0)) {
                    Integer laptopCountByAspectRatio = laptopClient.getLaptopCountByScreenAspectRatio(aspectRatio);
                    JOptionPane.showMessageDialog(frame,
                            "Znaleziono " + laptopCountByAspectRatio + " laptopów o skali ekranu: " + aspectRatio + ".");
                }
            }
        });
    }

    private void saveToTextFile() {
        Vector dataVector = laptopTableModel.getDataVector();
        List<String> lines = new ArrayList<>();

        for (Object element : dataVector) {
            lines.add(Joiner.on(";").join((List<String>) element) + ";");
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

    private void saveToXMLFile() {
        JAXBContext jaxbContext;
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

    private void readFromTxtFile() {
        fileChooser.setFileFilter(txtFilter);
        int returnVal = fileChooser.showOpenDialog(laptopViewPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File txtFile = fileChooser.getSelectedFile();
            LaptopView.this.loadDataFromTxtFile(txtFile);
            logger.log(Level.INFO, "Opening: " + txtFile.getName() + ".\n");
        } else {
            logger.log(Level.INFO, "Open command cancelled by user.\n");
        }
    }

    private void readFromXMLFile() {
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
}
