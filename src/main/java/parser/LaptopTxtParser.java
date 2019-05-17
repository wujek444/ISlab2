package parser;

import model.Laptop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class LaptopTxtParser {

    public List<String> split(String input){
        return new ArrayList<>(Arrays.asList(input.split(";")));
    }

    public Laptop parse(String input){
        List<String> splitted = split(input);
        if(splitted.size() < 15){
            splitted.add("");
        }
        return new Laptop(
                splitted.get(0),
                splitted.get(1),
                splitted.get(2),
                splitted.get(3),
                splitted.get(4),
                splitted.get(5),
                splitted.get(6),
                splitted.get(7),
                splitted.get(8),
                splitted.get(9),
                splitted.get(10),
                splitted.get(11),
                splitted.get(12),
                splitted.get(13),
                splitted.get(14)
        );
    }

    public Laptop parse(Vector laptopVector){
        return new Laptop(
                (String)laptopVector.get(0),
                (String)laptopVector.get(1),
                (String)laptopVector.get(2),
                (String)laptopVector.get(3),
                (String)laptopVector.get(4),
                (String)laptopVector.get(5),
                (String)laptopVector.get(6),
                (String)laptopVector.get(7),
                (String)laptopVector.get(8),
                (String)laptopVector.get(9),
                (String)laptopVector.get(10),
                (String)laptopVector.get(11),
                (String)laptopVector.get(12),
                (String)laptopVector.get(13),
                (String)laptopVector.get(14)
        );
    }

    public List<Laptop> parseVector(Vector laptopsVector){
        List<Laptop> laptops = new ArrayList<>();
        for(Object laptopVector : laptopsVector) {
            laptops.add(parse((Vector) laptopVector));
        }
        return laptops;
    }

    public List<Laptop> parseList(List<String> lines){
        List<Laptop> laptops = new ArrayList<>();
        for(String line : lines){
            laptops.add(parse(line));
        }
        return laptops;
    }
}
