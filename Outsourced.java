/**
 *
 * @author Arthur J Amende
 */
package Classes;

/** Outsourced Part extended from Part Class.
 * Outsourced Parts include Company Name.
 * <p>
 *     FUTURE ENHANCEMENT: Add companies to the database and select Company Name from approved companies.
 * </p>
 * */
public class Outsourced extends Part{

    private String companyName;

    /**
     * Parametrized constructor.
     * @param id the ID number
     * @param name the name
     * @param price the price
     * @param stock the current stock
     * @param min the minimum stock
     * @param max the maximum stock
     * @param companyName the Company Name
     * */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Copy constructor.
     * @param partToCopy the In-House part to be copied
     * */
    public Outsourced(Outsourced partToCopy){
        super(partToCopy.getId(),partToCopy.getName(),partToCopy.getPrice(),partToCopy.getStock(),partToCopy.getMin(),partToCopy.getMax());
        this.companyName = partToCopy.companyName;
    }

    /** Dummy Constructor used for testing. Creates a Outsourced Part named 'Dummy Outsourced Part' with Company Name 'Dummy Company'. All other variables set to 999.*/
    public Outsourced(){
        super(999, "Dummy Outsourced Part", 999, 999, 999, 999);
        this.companyName = "Dummy Company";
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName(){
        return companyName;
    }
}
