package view;

import model.Laptop;
import org.apache.commons.io.FileUtils;
import parser.LaptopTxtParser;
import reader.TextFileReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
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

    final JFileChooser fileChooser = new JFileChooser();
    final FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("*.txt", "txt", "text");

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
                loadDataFromFile(txtFile);
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
    }

    private void createColumns() {
        for (String columnName : COLUMN_NAMES) {
            laptopTableModel.addColumn(columnName);
        }
    }

    private void loadDataFromFile(File file) {
        TextFileReader textFileReader = new TextFileReader();
        List<String> fileLines = textFileReader.readLines(file);

        LaptopTxtParser txtParser = new LaptopTxtParser();
        List<Laptop> laptops = txtParser.parseList(fileLines);

        if (laptops.size() > 0) {
            fillTableModelWithLaptopData(laptops);
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
