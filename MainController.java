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
 * Controller for the Main Form.
 * The Main Form includes tables of all Parts and Products.
 * <p>
 *     FUTURE ENHANCEMENT: Add a View Part and View Product dialog so Parts and Products can be reviewed without risking modification.
 * </p>
 * */
public class MainController implements Initializable {

    public TableView tableParts;
    public TableColumn colPartID;
    public TableColumn colPartName;
    public TableColumn colPartInv;
    public TableColumn colPartPrice;

    public TableView tableProducts;
    public TableColumn colProductID;
    public TableColumn colProductName;
    public TableColumn colProductInv;
    public TableColumn colProductPrice;

    public TextField textSearchParts;
    public TextField textSearchProducts;

    static private Inventory inventory = null;
    static boolean addTestData = true;

    private ObservableList<Part> displayParts = FXCollections.observableArrayList();
    private ObservableList<Product> displayProducts = FXCollections.observableArrayList();

    /**
     * Generates initial Inventory of Parts and Products.
     * */
    private void testData(){
        inventory = new Inventory();

        InHouse part1 = new InHouse(1,"Part 1", 1.99, 1, 0, 1, 1);
        InHouse part2 = new InHouse(2,"Part 2", 2.99, 2, 0, 2, 2);
        InHouse part3 = new InHouse(3,"Part 3", 3.99, 3, 0, 3, 3);

        Outsourced part4 = new Outsourced(4, "Part 4", 4.99, 4, 0, 4, "Company 4");
        Outsourced part5 = new Outsourced(5, "Part 5", 5.99, 5, 0, 5, "Company 5");
        Outsourced part6 = new Outsourced(6, "Part 6", 6.99, 6, 0, 6, "Company 6");

        Product product1 = new Product(1,"Product 1",1.99,1,1,1);
        Product product2 = new Product(2,"Product 2",2.99,2,2,2);
        Product product3 = new Product(3,"Product 3",3.99,3,3,3);

        product1.addAssociatedPart(part1);

        product2.addAssociatedPart(part1);
        product2.addAssociatedPart(part2);

        product3.addAssociatedPart(part1);
        product3.addAssociatedPart(part6);

        inventory.addPart(part1);
        inventory.addPart(part2);
        inventory.addPart(part3);
        inventory.addPart(part4);
        inventory.addPart(part5);
        inventory.addPart(part6);

        inventory.addProduct(product1);
        inventory.addProduct(product2);
        inventory.addProduct(product3);

        addTestData = false;
    }

    /**
     * Initializes the Main Form.
     * Initial inventory of Parts and Products are generated on first call. Table Views are initialized.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        if (addTestData){testData();}

        displayParts = inventory.getAllParts();
        displayProducts = inventory.getAllProducts();

        tableParts.setItems(displayParts);
        colPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableProducts.setItems(displayProducts);
        colProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Transfers to the Parts Form in Add Mode.
     * Triggered by the 'Add' button on the Parts pane.
     * <p>
     * RUNTIME ERROR: Exception in thread "JavaFX Application Thread" java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
     * Caused by: java.lang.IllegalStateException: Location is not set.
     * The file path to the Parts Form was incorrect.
     * </p>
     * */
    public void toAddParts(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/Parts.fxml"));
        Parent root = loader.load();

        PartsController partsController = loader.getController();
        partsController.addMode();
        partsController.passInventory(inventory);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Transfers to the Parts Form in Modify Mode.
     * Triggered by the 'Modify' button on the Parts pane.
     * */
    public void toModifyParts(ActionEvent actionEvent) throws IOException{
        Part selectedPart = (Part) tableParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/Parts.fxml"));
        Parent root = loader.load();

        PartsController partsController = loader.getController();
        partsController.passInventory(inventory);
        partsController.modifyMode(selectedPart);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Transfers to the Product Form in Add Mode.
     * Triggered by the 'Add' button on the Product pane.
     * */
    public void toAddProd(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/Products.fxml"));
        Parent root = loader.load();

        ProductsController productsController = loader.getController();
        productsController.passInventory(inventory);
        productsController.addMode();

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Transfers to the Product Form in Modify Mode.
     * Triggered by the 'Modify' button on the Product pane.
     * */
    public void toModifyProd(ActionEvent actionEvent) throws IOException{
        Product selectedProduct = (Product) tableProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/Products.fxml"));
        Parent root = loader.load();

        ProductsController productsController = loader.getController();
        productsController.passInventory(inventory);
        productsController.modifyMode(selectedProduct);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the application.
     * Triggered by the 'Exit' button.
     * */
    public void exitSystem(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Deletes Part from Inventory.
     * Triggered by the 'Delete' button on the Part pane.
     * If no Part is selected nothing happens.
     * */
    public void onDeletePart(ActionEvent actionEvent) {
        Part deletePart = (Part) tableParts.getSelectionModel().getSelectedItem();
        if (deletePart == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Parts");
        alert.setHeaderText("Delete");
        alert.setContentText("Do you want to delete this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
//            int index = 0;
//            for (Product product : inventory.getAllProducts()){
//                if (product.deleteAssociatedPart(deletePart)){
//                    inventory.updateProduct(index, product);
//                }
//                index++;
//            }

            displayParts.remove(deletePart);
            inventory.deletePart(deletePart);

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Parts");
            alert.setHeaderText("Delete");
            alert.setContentText("Part " + deletePart.getName() + " deleted.");
            result = alert.showAndWait();

//            displayProducts = inventory.getAllProducts();
//            tableProducts.setItems(displayProducts);
        }
    }

    /**
     * Deletes Product from Inventory.
     * Triggered by the 'Delete' button on the Product pane.
     * Only Products with no Parts can be deleted.
     * If not Product is selected nothing happens.
     * */
    public void onDeleteProduct(ActionEvent actionEvent) {
        Product deleteProduct = (Product) tableProducts.getSelectionModel().getSelectedItem();
        if (deleteProduct == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Products");
        alert.setHeaderText("Delete");
        alert.setContentText("Do you want to delete this product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if (deleteProduct.getAllAssociatedParts().isEmpty()){
                displayProducts.remove(deleteProduct);
                inventory.deleteProduct(deleteProduct);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Products");
                alert.setHeaderText("Delete");
                alert.setContentText("Product " + deleteProduct.getName() + " deleted.");
                result = alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Products");
                alert.setContentText("This product has parts and can not be deleted.");
                result = alert.showAndWait();
            }
        }
    }

    /**
     * Searches Parts based on input from search field.
     * If a number is searched the Part with the matching ID will be selected.
     * If text is searched all Parts matching the text will be displayed.
     * If no match is found an error dialog will appear.
     * If nothing is searched all Parts will be displayed.
     * */
    public void searchParts(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            String search = textSearchParts.getText();
            if (search.isEmpty()){
                displayParts = inventory.getAllParts();
                tableParts.setItems(displayParts);
                return;
            }

            try{
                int id = Integer.parseInt(search);
                for (Part row : displayParts) {
                    if (row.getId() == id){
                        tableParts.getSelectionModel().select(row);
                        return;
                    }
                }
                tableParts.getSelectionModel().clearSelection();
            } catch (NumberFormatException e) {
                displayParts = inventory.lookupPart(search);
                if (!displayParts.isEmpty()){
                    tableParts.setItems(displayParts);
                } else {
                    displayParts = inventory.getAllParts();
                    tableParts.setItems(displayParts);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Parts");
                    alert.setContentText("No Parts Found. Search is case sensitive.");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
        }
    }

    /**
     * Searches Products based on input from search field.
     * If a number is searched the Product with the matching ID will be selected.
     * If text is searched all Products matching the text will be displayed.
     * If no match is found an error dialog will appear.
     * If nothing is searched all Products will be displayed.
     * */
    public void searchProducts(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            String search = textSearchProducts.getText();
            if (search.isEmpty()){
                displayProducts = inventory.getAllProducts();
                tableProducts.setItems(displayProducts);
                return;
            }

            try{
                int id = Integer.parseInt(search);
                for (Product row : displayProducts){
                    if (row.getId() == id){
                        tableProducts.getSelectionModel().select(row);
                        return;
                    }
                }
                tableProducts.getSelectionModel().clearSelection();
            } catch (NumberFormatException e){
                displayProducts = inventory.lookupProduct(search);
                if (!displayProducts.isEmpty()){
                    tableProducts.setItems(displayProducts);
                } else {
                    displayProducts = inventory.getAllProducts();
                    tableProducts.setItems(displayProducts);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Products");
                    alert.setContentText("No Products Found. Search is case sensitive.");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
        }
    }
}
