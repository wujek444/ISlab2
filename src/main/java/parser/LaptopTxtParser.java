package parser;

import model.Laptop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<Laptop> parseList(List<String> lines){
        List<Laptop> laptops = new ArrayList<>();
        for(String line : lines){
            laptops.add(parse(line));
        }
        return laptops;
    }
}
