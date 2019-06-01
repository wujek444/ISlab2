package dictonary;

import java.util.ArrayList;
import java.util.List;

public class ScreenResolutionDictionary {

    private static final String SIXTEEN_TO_NINE_ASPECT_RATIO = "16:9";
    private static final String SIXTEEN_TO_TEN_ASPECT_RATIO = "16:10";

    public static List<String> getDictionaryByAspectRatio(String aspectRatio) {
        List<String> dictionary = new ArrayList<>();
        switch (aspectRatio){
            case SIXTEEN_TO_NINE_ASPECT_RATIO:
                dictionary.add("256x144");
                dictionary.add("426x240");
                dictionary.add("640x360");
                dictionary.add("768x432");
                dictionary.add("800x450");
                dictionary.add("848x480");
                dictionary.add("960x540");
                dictionary.add("1024x576");
                dictionary.add("1280x720");
                dictionary.add("1366x768");
                dictionary.add("1600x900");
                dictionary.add("1920x1080");
                dictionary.add("2048x1152");
                dictionary.add("2560x1440");
                dictionary.add("2560x1440");
                dictionary.add("3200x1800");
                break;
            case SIXTEEN_TO_TEN_ASPECT_RATIO:
                dictionary.add("1280x800");
                dictionary.add("1440x900");
                dictionary.add("1680x1050");
                dictionary.add("1920x1200");
                dictionary.add("2560x1600");
        }
        return dictionary;
    }
}
