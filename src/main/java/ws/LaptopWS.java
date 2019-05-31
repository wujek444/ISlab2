package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface LaptopWS  {
    @WebMethod Integer getLaptopCountByManufacturer(String producer);
    @WebMethod Integer getLaptopCountByScreenResolution(String producer);
}
