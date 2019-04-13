package view;

import reader.TextFileReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class LaptopView {
    private JButton readTextFileButton;
    private JButton saveToTextButton;
    private JButton loadXMLButton;
    private JButton saveToXMLButton;
    private JTable laptopTable;
    private JPanel laptopViewPanel;
    private JScrollPane laptopViewScrollPane;

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
        Object[][] data = {{"MSI", "17\"", "1600x900", "blyszczaca", "tak", "intel i7", "4", "9999", "8GB", "60GB", "SSD",
                "AMD Radeon Pro 445", "1GB", "Windows 7 Professional", "brak"}};

        TextFileReader textFileReader = new TextFileReader();
        List<String> fileLines =  textFileReader.readLines("src/main/resources/dane.txt");

        for(String line : fileLines){

        }

        //todo: Object[][] data = danePobraneZPliku

        laptopTable.setModel(new DefaultTableModel(data , COLUMN_NAMES ){
            public boolean isCellEditable(int row, int col){
                return false;
            }
        });
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
