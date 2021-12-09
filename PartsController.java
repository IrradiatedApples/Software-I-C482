/**
 *
 * @author Arthur J Amende
 */
package Controllers;

import Classes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Parts Form.
 * The Parts Form adds new Parts and modifies existing Parts.
 * <p>
 *     FUTURE ENHANCEMENT: Allow an existing Part to be copied to make a similar Part.
 * </p>
 *
 * */
public class PartsController implements Initializable {
    public Label labelTitle;
    public Button buttonSave;

    public TextField textID;
    public TextField textName;
    public TextField textInv;
    public TextField textPrice;
    public TextField textMax;
    public TextField textMin;
    public TextField textMachCom;

    public RadioButton radioInHouse;
    public RadioButton radioOutsourced;
    public ToggleGroup toggleMachCom;
    public Label labelMachCom;

    public Label labelExceptions;

    private Inventory inventory = null;

    private enum MODE {ADD, MODIFY}
    private MODE mode;

    /**
     * Initializes the Part Form.
     * Exceptions list is set to blank.
     * <p>
     * RUNTIME ERROR: Exception in thread "JavaFX Application Thread" java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
     * Caused by: java.lang.NullPointerException.
     * Attempted to use the inventory but as initialize is called before passInventory it was still set to null.
     * </p>
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        labelExceptions.setText("");
    }

    /**
     * Inventory passed from the Main From.
     * @param inventory the Inventory
     * */
    public void passInventory(Inventory inventory){
        this.inventory = inventory;
    }

    /**
     * Transfers back to the Main From.
     * Triggered when the 'Cancel' button is selected.
     * */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Forms/Main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the Part From in Add Mode.
     * Title is set to 'Add Part'. Toggle is set to 'In-House'.
     * */
    public void addMode(){
        labelTitle.setText("Add Part");
        toggleMachCom.selectToggle(radioInHouse);
        mode = MODE.ADD;
    }

    /**
     * Initializes the Part From in Modify Mode.
     * Title is set to 'Modify Part'.
     * All fields are set per the Part to be modified.
     * @param modifyPart the Part to be modified
     * */
    public void modifyMode(Part modifyPart){
        labelTitle.setText("Modify Part");
        mode = MODE.MODIFY;

        textID.setText(String.valueOf(modifyPart.getId()));
        textName.setText(modifyPart.getName());
        textPrice.setText(String.valueOf(modifyPart.getPrice()));
        textInv.setText(String.valueOf(modifyPart.getStock()));
        textMax.setText(String.valueOf(modifyPart.getMax()));
        textMin.setText(String.valueOf(modifyPart.getMin()));

        if (modifyPart instanceof InHouse){
            textMachCom.setText(String.valueOf(((InHouse) modifyPart).getMachineID()));
            toggleMachCom.selectToggle(radioInHouse);
            labelMachCom.setText("Machine ID");
        } else if (modifyPart instanceof Outsourced){
            textMachCom.setText(((Outsourced) modifyPart).getCompanyName());
            toggleMachCom.selectToggle(radioOutsourced);
            labelMachCom.setText("Company Name");
        }
    }

    /**
     * Saves the Part.
     * <p>
     * In Add Mode a new Part is generated and added to the Inventory.
     * In Modify Mode the Part is updated in the Inventory.
     * Error checking is performed to ensure all variables are the correct format.
     * Any exceptions found stops the save and a list of exceptions is generated.
     * Triggered when the 'Save' button is selected.
     * </p>
     * <p>
     * RUNTIME ERROR: Exception in thread "JavaFX Application Thread"
     * Caused by: java.lang.NumberFormatException.
     * Attempted to pass characters only to Integer.parseInt. Added try statement to capture exception.
     * </p>
     * */
    public void onSave(ActionEvent actionEvent) throws IOException{

        int id = 999;
        String name = textName.getText();
        int stock = 999;
        double price = 999;
        int max = 999;
        int min = 999;

        //Exceptions
        boolean exceptions = false;
        boolean minMax = false;
        boolean minMaxStock = false;
        labelExceptions.setText("");
        if (name.isEmpty()){
            labelExceptions.setText("\nNo data in name field");
            exceptions = true;
        }

        try {
            stock = Integer.parseInt(textInv.getText());
            minMaxStock = true;
        } catch (NumberFormatException e) {
            labelExceptions.setText(labelExceptions.getText() + "\nInventory is not an integer");
            exceptions = true;
        }

        try {
            price = Double.parseDouble(textPrice.getText());
        } catch (NumberFormatException e){
            labelExceptions.setText(labelExceptions.getText() + "\nPrice is not a double");
            exceptions = true;
        }

        try {
            max = Integer.parseInt(textMax.getText());
            minMax = true;
        } catch (NumberFormatException e){
            minMaxStock = false;
            labelExceptions.setText(labelExceptions.getText() + "\nMax is not an integer");
            exceptions = true;
        }

        try {
            min = Integer.parseInt(textMin.getText());
        } catch (NumberFormatException e) {
            minMax = false;
            minMaxStock = false;
            labelExceptions.setText(labelExceptions.getText() + "\nMin is not an integer");
            exceptions = true;
        }

        if (minMax && min > max){
            labelExceptions.setText(labelExceptions.getText() + "\nMax must be greater than Min");
            exceptions = true;
        }

        if (minMaxStock && (stock > max || stock < min)){
            labelExceptions.setText(labelExceptions.getText() + "\nInventory must be between Min and Max");
            exceptions = true;
        }

        if (exceptions) {
            labelExceptions.setText("Exceptions:" + labelExceptions.getText());
            return;
        }

        Part newPart = new InHouse();
        if (toggleMachCom.getSelectedToggle() == radioInHouse) {
            int machineID = Integer.parseInt(textMachCom.getText());
            newPart = new InHouse(id, name, price, stock, min, max, machineID);
        } else if (toggleMachCom.getSelectedToggle() == radioOutsourced) {
            String companyName = textMachCom.getText();
            newPart = new Outsourced(id, name, price, stock, min, max, companyName);
        }

        if (mode == MODE.ADD) {
            inventory.addPart(newPart);
        } else if (mode == MODE.MODIFY){
            newPart.setId(Integer.parseInt(textID.getText()));
            int index = 0;
            for (Part part : inventory.getAllParts()){
                if (part.getId() == newPart.getId()){
                    inventory.updatePart(index, newPart);
                    break;
                }
                index++;
            }
            index = 0;
            for (Product product : inventory.getAllProducts()){
                if (product.deleteAssociatedPart(newPart)){
                    product.addAssociatedPart(newPart);
                    inventory.updateProduct(index, product);
                }
                index++;
            }
        }

        toMain(actionEvent);
    }

    /**
     * The bottom label is set to 'Machine ID'.
     * Triggered when the In-House radio is selected.
     * */
    public void setLabelMachineID(ActionEvent actionEvent) {
        labelMachCom.setText("Machine ID");
    }

    /**
     * The bottom label is set to 'Company Name'.
     * Triggered when the Outsourced radio is selected.
     * */
    public void setLabelCompanyName(ActionEvent actionEvent){
        labelMachCom.setText("Company Name");
    }
}
