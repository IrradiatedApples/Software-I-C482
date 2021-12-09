/**
 *
 * @author Arthur J Amende
 */
package Classes;

/** In-House Part extended from Part Class.
 * In-House Parts include Machine ID.
 * <p>
 *     FUTURE ENHANCEMENT: Add Machines to the database and select Machine ID from approved Machines.
 * </p>
 * */
public class InHouse extends Part{

    private int machineID;

    /**
     * Parametrized constructor.
     * @param id the ID number
     * @param name the name
     * @param price the price
     * @param stock the current stock
     * @param min the minimum stock
     * @param max the maximum stock
     * @param machineID the Machine ID
     * */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Copy constructor.
     * @param partToCopy the In-House part to be copied
     * */
    public InHouse(InHouse partToCopy){
        super(partToCopy.getId(),partToCopy.getName(),partToCopy.getPrice(),partToCopy.getStock(),partToCopy.getMin(),partToCopy.getMax());
        this.machineID = partToCopy.machineID;
    }

    /** Dummy Constructor used for testing. Creates an In-House Part named 'Dummy InHouse Part' with all other variables set to 999.*/
    public InHouse(){
        super(999, "Dummy InHouse Part", 999, 999, 999, 999);
        this.machineID = 999;
    }

    /**
     * @param machineID the machineID to set
     */
    public void setMachineID(int machineID){
        this.machineID = machineID;
    }

    /**
     * @return the machineID
     */
    public int getMachineID(){
        return machineID;
    }
}
