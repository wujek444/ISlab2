package reader;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class TextFileReaderTest {

    private static File testTextFile;
    private static TextFileReader textFileReader;

    @BeforeClass
    public static void init(){
        testTextFile = new File("src/test/resources/txtTestFile.txt");
        textFileReader = new TextFileReader();
    }

    @Test
    public void readLines() {
        String line1 = "1;", line2 = "AAAAA", line3 = "%^%";
        List<String> lines = textFileReader.readLines(testTextFile);

        assertEquals(line1, lines.get(0));
        assertEquals(line2, lines.get(1));
        assertEquals(line3, lines.get(2));
    }
}