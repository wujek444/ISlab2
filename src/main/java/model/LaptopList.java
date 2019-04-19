package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jrwoj
 * createdOn 19.04.2019
 */

@XmlRootElement
public class LaptopList {
    private List<Laptop> laptop;

    public LaptopList() {
        this.laptop = new ArrayList<>();
    }

    public LaptopList(List<Laptop> laptop) {
        this.laptop = laptop;
    }

    public List<Laptop> getLaptop() {
        return laptop;
    }

    public void setLaptop(List<Laptop> laptop) {
        this.laptop = laptop;
    }
}
