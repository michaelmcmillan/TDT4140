package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private final String MAINVIEW_PATH = "../views/MainView.fxml";
    private CalendarViewController appointmentViewController;
    private SidebarViewController sidebarViewController;
    private AppointmentPopupViewController popupView;
    private MenuBar menuBar;


    public MainViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        appointmentViewController = new CalendarViewController(this, primaryStage);
        sidebarViewController = new SidebarViewController(this, primaryStage);

        // Listeners
        menuBar = (MenuBar) main.getChildrenUnmodifiable().get(1); // TODO: Find a better way to do this
        ObservableList<Menu> menus = menuBar.getMenus();

        // Account menu
        Menu accountMenu = menus.get(0);
        ObservableList<MenuItem> accountMenuItems = accountMenu.getItems();

        MenuItem logoutMenuItem = accountMenuItems.get(0);
        logoutMenuItem.setOnAction(e -> fireLogout(e));

        // Item menu
        Menu itemMenu = menus.get(1);
        ObservableList<MenuItem> itemMenuItems = itemMenu.getItems();

        MenuItem addCalendarMenuItem = itemMenuItems.get(0);
        addCalendarMenuItem.setOnAction(e -> fireAddCalendar(e));

        MenuItem addGroupMenuItem = itemMenuItems.get(1);
        addGroupMenuItem.setOnAction(e -> fireAddGroup(e));
    }

    void fireAddGroup(ActionEvent event) {
        System.out.println("Add group");
    }

    void fireAddCalendar(ActionEvent event) {
        System.out.println("Add calendar");
    }

    void fireLogout(ActionEvent event) {
        System.out.println("Log out");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("yolo");
    }
}
