/**
 *
 * @author Arthur J Amende
 */package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.regex.Pattern;

/** Inventory tracking all Parts and Products.
 * <p>
 *     FUTURE ENHANCEMENT: Do not allow Products to use Parts not in the inventory. If a Part is removed from the Inventory it should generate an error if it is used on a Product.
 * </p>*/
public class Inventory {
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;

    private static int uniquePartID = 1;
    private static int uniqueProductID = 1;

    /** Constructor creating an empty Inventory. */
    public Inventory(){
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }

    /**
     * @param newPart Part added to Inventory
     * */
    public void addPart(Part newPart){
        newPart.setId(uniquePartID);
        uniquePartID++;
        allParts.add(newPart);
    }

    /**
     * @param newProduct Product added to Inventory
     * */
    public void addProduct(Product newProduct){
        newProduct.setId(uniqueProductID);
        uniqueProductID++;
        allProducts.add(newProduct);
    }

    /**
     * @param partID ID of Part to be located
     * @return copy of located Part. Returns null if not found.
     * */
    public Part lookupPart(int partID){
        Part foundPart = null;
        for(Part part : allParts){
            if (part.getId() == partID){
                foundPart = copyPart(part);
            }
        }
        return foundPart;
    }

    /**
     * @param productID ID of Product to be located
     * @return copy of located Product. Returns null if not found.
     * */
    public Product lookupProduct(int productID){
        Product foundProduct = null;
        for(Product product : allProducts){
            if(product.getId() == productID){
                foundProduct = new Product(product);
            }
        }
        return foundProduct;
    }

    /**
     * Find all Parts beginning with partName
     * @param partName Part Name search
     * @return ObservableList of Parts beginning with partName
     * */
    public ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for(Part part : allParts){
            String name = part.getName();
            boolean foundPart = Pattern.compile("^" + partName).matcher(name).find();
            if(foundPart){
                foundParts.add(copyPart(part));
            }
        }
        return foundParts;
    }

    /**
     * Find all Products beginning with productName
     * @param productName Product Name search
     * @return ObservableList of Products beginning with productName
     * */
    public ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for(Product product : allProducts){
            String name = product.getName();
            boolean foundProduct = Pattern.compile("^" + productName).matcher(name).find();
            if(foundProduct){
                foundProducts.add(new Product(product));
            }
        }
        return foundProducts;
    }

    /**
     * @param index index of Part to be updated
     * @param selectedPart new Part data to update existing Part
     * */
    public void updatePart(int index, Part selectedPart){
        allParts.set(index,selectedPart);
    }

    /**
     * @param index index of Product to be updated
     * @param newProduct new Product data to update existing Product
     * */
    public void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart Part to be deleted from Inventory
     * @return true if deleted, false if not deleted
     * */
    public boolean deletePart(Part selectedPart){
        int i = 0;
        boolean deleted = false;
        for (int j = 0; j < allParts.size(); ++j){
            if (selectedPart.getId() == allParts.get(j).getId()){
                i = j;
                deleted = true;
                break;
            }
        }
        if (deleted){
            allParts.remove(i);
        }
        return deleted;
    }

    /**
     * @param selectedProduct Product to be deleted from Inventory
     * @return true if deleted, false if not deleted
     * */
    public boolean deleteProduct(Product selectedProduct){
        int i = 0;
        boolean deleted = false;
        for (int j = 0; j < allProducts.size(); ++j){
            if (selectedProduct.getId() == allProducts.get(j).getId()){
                i = j;
                deleted = true;
                break;
            }
        }
        if (deleted){
            allProducts.remove(i);
        }
        return deleted;
    }

    /**
     * @return all Parts in Inventory
     * */
    public ObservableList<Part> getAllParts(){
        ObservableList<Part> newAllParts = FXCollections.observableArrayList();
        for(Part part : allParts){
            newAllParts.add(copyPart(part));
        }
        return newAllParts;
    }

    /**
     * @return all Products in Inventory
     * */
    public ObservableList<Product> getAllProducts() {
        ObservableList<Product> newAllProducts = FXCollections.observableArrayList();
        for(Product product : allProducts){
            newAllProducts.add(new Product(product));
        }
        return newAllProducts;
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
