package view;

import db.DBConnector;
import dictonary.ScreenResolutionDictionary;
import model.Laptop;
import model.LaptopList;
import org.apache.commons.io.FilenameUtils;
import parser.LaptopDBMapper;
import ws.LaptopWS;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.LaptopUtils.COLUMN_NAMES;

public class LaptopClient {
    private JButton raportWgNazwyProducentaButton;
    private JButton raportWgProporcjiEkranuButton;
    private JButton niestandardowyExportDoXMLButton;
    private JPanel clientPanel;
    private static JFrame frame;

    LaptopWS client;

    final private LaptopDBMapper laptopDBMapper = new LaptopDBMapper();
    final private JFileChooser fileChooser = new JFileChooser();
    final private FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("*.xml", "xml");

    final private Logger logger = Logger.getAnonymousLogger();

    public LaptopClient() {
        initGuiComponents();
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

    public static void main(String[] args) {
        DBConnector.loadODBCDriverClass();
        frame = new JFrame("LaptopClient");
        frame.setContentPane(new LaptopClient().clientPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public Integer getLaptopCountByManufacturer(String manufacturer) {
        return client.getLaptopCountByManufacturer(manufacturer);
    }

    public Integer getLaptopCountByScreenAspectRatio(String ratio) {
        return client.getLaptopCountByScreenResolutions(ScreenResolutionDictionary.getDictionaryByAspectRatio(ratio));
    }

    private List<Laptop> getLaptopsOnlyWithSelectedFields(List<Laptop> laptops, List<JCheckBox> selectedCheckboxes) {
        List<Laptop> processedLaptops = new ArrayList<>();
        for(Laptop laptop : laptops) {
            Laptop processedLaptop = new Laptop();
            for(JCheckBox checkBox : selectedCheckboxes) {
                switch (checkBox.getText()) {
                    case "Producent":
                        processedLaptop.setManufacturer(laptop.getManufacturer());
                        break;
                    case "Wielkość matrycy":
                        processedLaptop.setMatrixSize(laptop.getMatrixSize());
                        break;
                    case "Powłoka matrycy":
                        processedLaptop.setMatrixCoating(laptop.getMatrixCoating());
                        break;
                    case "Ekran dotykowy":
                        processedLaptop.setTouchPad(laptop.getTouchPad());
                        break;
                    case "Seria procesora":
                        processedLaptop.setCpuFamily(laptop.getCpuFamily());
                        break;
                    case "Liczba rdzeni":
                        processedLaptop.setCoresCount(laptop.getCoresCount());
                        break;
                    case "Taktowanie bazowe":
                        processedLaptop.setClockSpeed(laptop.getClockSpeed());
                        break;
                    case "Wielkość pamięci RAM":
                        processedLaptop.setRam(laptop.getRam());
                        break;
                    case "Pojemność dysku":
                        processedLaptop.setDriveCapacity(laptop.getDriveCapacity());
                        break;
                    case "Typ dysku":
                        processedLaptop.setDriveType(laptop.getDriveType());
                        break;
                    case "Karta graficzna":
                        processedLaptop.setGpu(laptop.getGpu());
                        break;
                    case "Pamięć karty graficznej":
                        processedLaptop.setGpuMemory(laptop.getGpuMemory());
                        break;
                    case "System operacyjny":
                        processedLaptop.setOs(laptop.getOs());
                        break;
                    case "Napęd optyczny":
                        processedLaptop.setOpticalDrive(laptop.getOpticalDrive());
                        break;
                    case "Rozdzielczość":
                        processedLaptop.setResolution(laptop.getResolution());
                        break;

                }
            }
            processedLaptops.add(processedLaptop);
        }
        return processedLaptops;
    }

    private void saveToXMLFile(LaptopList laptopList) {
        JAXBContext jaxbContext;
        try {
            fileChooser.setFileFilter(xmlFilter);
            int returnVal = fileChooser.showSaveDialog(clientPanel);
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

    private void initGuiComponents() {
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
                    Integer laptopCountByManufacturer = getLaptopCountByManufacturer(manufacturer);
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
                    Integer laptopCountByAspectRatio = getLaptopCountByScreenAspectRatio(aspectRatio);
                    JOptionPane.showMessageDialog(frame,
                            "Znaleziono " + laptopCountByAspectRatio + " laptopów o skali ekranu: " + aspectRatio + ".");
                }
            }
        });

        niestandardowyExportDoXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JCheckBox> checkboxes = new ArrayList<>();
                for (String columnName : COLUMN_NAMES){
                    if(!columnName.equals("edited_hidden")) {
                        JCheckBox box = new JCheckBox(columnName);
                        checkboxes.add(box);
                    }
                }
                Object[] obj = checkboxes.toArray(new Object[checkboxes.size()]);
                int n = JOptionPane.showConfirmDialog(frame, obj, "Wybierz pola do wyeksportowania", JOptionPane.OK_CANCEL_OPTION);
                List<JCheckBox> selectedCheckboxes = new ArrayList<>();
                if(n==JOptionPane.YES_OPTION) {
                    for(JCheckBox checkbox : checkboxes) {
                        if(checkbox.isSelected()) {
                            selectedCheckboxes.add(checkbox);
                        }
                    }
                    if(selectedCheckboxes.size() > 5) {
                        JOptionPane.showMessageDialog(frame, "Można wybrać do 5 pól do wyeksportowania przy użyciu tej opcji!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        List<Laptop> foundLaptops = new ArrayList<>();
                        try {
                            foundLaptops = laptopDBMapper.mapResultSetToLaptops(DBConnector.executeQuery("select * from Laptop"));
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                        List<Laptop> laptopsOnlyWithSelectedFields = getLaptopsOnlyWithSelectedFields(foundLaptops, selectedCheckboxes);
                        saveToXMLFile(new LaptopList(laptopsOnlyWithSelectedFields));
                    }
                }
            }
        });
    }
}
