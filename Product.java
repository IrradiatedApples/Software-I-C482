/**
 *
 * @author Arthur J Amende
 */
package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Product Class.
 * Fully defines a Product including all associated parts.
 * */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Parametrized constructor.
     * @param id the ID number
     * @param name the name
     * @param price the price
     * @param stock the current stock
     * @param min the minimum stock
     * @param max the maximum stock
     * */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;

        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * Copy constructor.
     * @param productToCopy the Product to be copied
     * */
    public Product(Product productToCopy){
        this.id = productToCopy.id;
        this.name = productToCopy.name;
        this.price = productToCopy.price;
        this.stock = productToCopy.stock;
        this.min = productToCopy.min;
        this.max = productToCopy.max;

        this.associatedParts = FXCollections.observableArrayList();
        for (Part part : productToCopy.associatedParts){
            this.associatedParts.add(copyPart(part));
        }
    }

    /** Dummy Constructor used for testing. Creates a Product named 'Dummy Product' with all other variables set to 999 and no associated Parts.*/
    public Product(){
        this.id = 999;
        this.name = "Dummy Product";
        this.price = 999;
        this.stock = 999;
        this.min = 999;
        this.max = 999;

        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * @param id the id to set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name){this.name = name;}

    /**
     * @param price the price to set
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max){
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId(){
        return id;
    }

    /**
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice(){
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock(){
        return stock;
    }

    /**
     * @return the min
     */
    public int getMin(){
        return min;
    }

    /**
     * @return the max
     */
    public int getMax(){
        return max;
    }

    /**
     * @param part the part added to associatedParts
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the selectedAssociatedPart deleted from associatedParts
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        int i = 0;
        boolean deleted = false;
        for (int j = 0; j < associatedParts.size(); ++j){
            if (selectedAssociatedPart.getId() == associatedParts.get(j).getId()){
                i = j;
                deleted = true;
                break;
            }
        }
        if (deleted){
            associatedParts.remove(i);
        }
        return deleted;
    }

    /**
     * @return the associatedParts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        ObservableList<Part> newAssociatedParts = FXCollections.observableArrayList();
        for(Part part : associatedParts){
            newAssociatedParts.add(copyPart(part));
        }
        return newAssociatedParts;
    }

    /**
     * Copies an In-House or Outsourced Part returned as a Part Class. 
     * @param partToCopy the Part to be copied
     * @return a duplicated part*/
    private Part copyPart(Part partToCopy){
        Part newPart = new InHouse();
        if(partToCopy instanceof InHouse){
            newPart = new InHouse((InHouse) partToCopy);
        } else if (partToCopy instanceof Outsourced){
            newPart = new Outsourced((Outsourced) partToCopy);
        }
        return newPart;
    }
}
