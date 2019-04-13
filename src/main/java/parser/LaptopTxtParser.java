package parser;

import model.Laptop;

import java.util.ArrayList;
import java.util.List;

public class LaptopTxtParser {

    public String[] split(String input){
        return input.split(";");
    }

    public Laptop parse(String input){
        String[] splitted = split(input);
        return new Laptop(
                splitted[0],
                splitted[1],
                splitted[2],
                splitted[3],
                splitted[4],
                splitted[5],
                splitted[6],
                splitted[7],
                splitted[8],
                splitted[9],
                splitted[10],
                splitted[11],
                splitted[12],
                splitted[13],
                splitted[14]
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
