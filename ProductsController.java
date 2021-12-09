/**
 *
 * @author Arthur J Amende
 */
package Controllers;

import Classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the Products Form.
 * The Products Form adds new Products and modifies existing Products.
 * <p>
 *     FUTURE ENHANCEMENT: Allow an existing Product to be copied to make a similar Product.
 * </p>
 * <p>
 *     FUTURE ENHANCEMENT: Add a View Part dialog so Parts can be reviewed prior to adding to Product.
 * </p>
 * */
public class ProductsController implements Initializable {
    public Label labelTitle;

    public TableView tablePartsAdd;
    public TableColumn colPartAddID;
    public TableColumn colPartAddName;
    public TableColumn colPartAddInv;
    public TableColumn colPartAddPrice;

    public TextField textID;
    public TextField textName;
    public TextField textInv;
    public TextField textPrice;
    public TextField textMax;
    public TextField textMin;

    public TableView tableProdParts;
    public TableColumn colProdPartsID;
    public TableColumn colProdPartsName;
    public TableColumn colProdPartsInv;
    public TableColumn colProdPartsPrice;

    public TextField textSearchParts;

    public Label labelExceptions;

    private Inventory inventory = null;

    private ObservableList<Part> displayAllParts = FXCollections.observableArrayList();
    private ObservableList<Part> displayProdParts = FXCollections.observableArrayList();

    private enum MODE {ADD, MODIFY}
    private MODE mode;

    /**
     * Initializes the Part Form.
     * Exceptions list is set to blank.
     * Table views are initialized.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        labelExceptions.setText("");

        colPartAddID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPartAddName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartAddInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPartAddPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colProdPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProdPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colProdPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

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
     * Inventory passed from the Main From.
     * @param inventory the Inventory
     * */
    public void passInventory(Inventory inventory){
        this.inventory = inventory;
        displayAllParts = inventory.getAllParts();
        tablePartsAdd.setItems(displayAllParts);
    }

    /**
     * Initializes the Product From in Add Mode.
     * Title is set to 'Add Product'.
     * */
    public void addMode(){
        labelTitle.setText("Add Product");
        mode = MODE.ADD;

        tableProdParts.setItems(displayProdParts);
    }

    /**
     * Initializes the Product From in Modify Mode.
     * Title is set to 'Modify Product'.
     * All fields are set per the Product to be modified.
     * @param modifyProduct the Part to be modified
     * */
    public void modifyMode(Product modifyProduct){
        labelTitle.setText("Modify Product");
        mode = MODE.MODIFY;

        textID.setText(String.valueOf(modifyProduct.getId()));
        textName.setText(modifyProduct.getName());
        textPrice.setText(String.valueOf(modifyProduct.getPrice()));
        textInv.setText(String.valueOf(modifyProduct.getStock()));
        textMax.setText(String.valueOf(modifyProduct.getMax()));
        textMin.setText(String.valueOf(modifyProduct.getMin()));

        displayProdParts = modifyProduct.getAllAssociatedParts();
        tableProdParts.setItems(displayProdParts);
    }

    /**
     * Saves the Product.
     * In Add Mode a new Product is generated and added to the Inventory.
     * In Modify Mode the Product is updated in the Inventory.
     * Error checking is performed to ensure all variables are the correct format.
     * Any exceptions found stops the save and a list of exceptions is generated.
     * Triggered when the 'Save' button is selected.
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

        Product newProduct = new Product(id, name, price, stock, min, max);
        for (Object part : tableProdParts.getItems()){
            newProduct.addAssociatedPart((Part) part);
        }

        if (mode == MODE.ADD) {
            inventory.addProduct(newProduct);
        } else if (mode == MODE.MODIFY){
            newProduct.setId(Integer.parseInt(textID.getText()));
            int index = 0;
            for (Product product : inventory.getAllProducts()){
                if (product.getId() == newProduct.getId()){
                    inventory.updateProduct(index, newProduct);
                    break;
                }
                index++;
            }
        }

        toMain(actionEvent);
    }

    /**
     * Adds Part to Product.
     * Triggered when 'Add' button is selected.
     * */
    public void onAddPart(ActionEvent actionEvent) {
        displayProdParts.add((Part) tablePartsAdd.getSelectionModel().getSelectedItem());
    }

    /**
     * Removes Part from Product.
     * Triggered when 'Remove Associated Part' button is selected.
     * */
    public void onRemovePart(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Associated Parts");
        alert.setHeaderText("Remove");
        alert.setContentText("Do you want to remove this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String name = ((Part) tableProdParts.getSelectionModel().getSelectedItem()).getName();
            displayProdParts.remove(tableProdParts.getSelectionModel().getSelectedItem());

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Associated Parts");
            alert.setHeaderText("Remove");
            alert.setContentText("Part " + name + " removed.");
            result = alert.showAndWait();
        }
    }

    /**
     * Searches all Parts based on input from search field.
     * If a number is searched the Part with the matching ID will be selected.
     * If text is searched all Parts matching the text will be displayed.
     * If no match is found no Parts will be displayed.
     * If nothing is searched all Parts will be displayed.
     * */
    public void searchParts(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            String search = textSearchParts.getText();
            if (search.isEmpty()){
                displayAllParts = inventory.getAllParts();
                tablePartsAdd.setItems(displayAllParts);
                return;
            }

            try{
                int id = Integer.parseInt(search);
                for (Object row : displayAllParts){
                    if (((Part) row).getId() == id){
                        tablePartsAdd.getSelectionModel().select(row);
                        return;
                    }
                }
                tablePartsAdd.getSelectionModel().clearSelection();
            } catch (NumberFormatException e) {
                displayAllParts = inventory.lookupPart(search);
                if (!displayAllParts.isEmpty()){
                    tablePartsAdd.setItems(displayAllParts);
                } else {
                    displayAllParts = inventory.getAllParts();
                    tablePartsAdd.setItems(displayAllParts);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Parts");
                    alert.setContentText("No Parts Found. Search is case sensitive.");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
        }
    }
}
