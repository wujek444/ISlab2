package parser;

import model.Laptop;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LaptopTxtParserTest {

    private final static String line1 = "MSI;17\";1600x900;blyszczaca;tak;intel i7;4;9999;8GB;60GB;SSD;AMD Radeon Pro 455;1GB;Windows 7 Profesional;;";
    private final static String line2 = "Huawei;13\";1366x768;matowa;nie;intel i7;4;3000;;24GB;HDD;NVIDIA GeForce GTX 1050;;Windows 10 Home;brak;";
    private final static String line3 = "Huawei;13\";;;nie;intel i7;4;3000;;24GB;HDD;NVIDIA GeForce GTX 1050;;Windows 10 Home;brak;";
    private static LaptopTxtParser parser;

    @BeforeClass
    public static void init(){
        parser = new LaptopTxtParser();
    }

    @Test
    public void split() {
        List<String> actual = parser.split(line2);
        List<String> expected = new ArrayList<>(Arrays.asList("Huawei", "13\"", "1366x768", "matowa", "nie", "intel i7"));

    }

    @Test
    public void parse() {
        Laptop laptop = parser.parse(line2);
        assertNotNull(laptop);
    }
}