package controllers;

import helpers.HTMLConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private final String MAINVIEW_PATH = "../views/MainView.fxml";
    private CalendarViewController calendarViewController;
    private SidebarViewController sidebarViewController;
    private GroupPopupViewController groupPopupViewController;
    private CalendarPopupViewController calendarPopupViewController;
    private MenuBar menuBar;
    private Person person;

    public MainViewController(Stage primaryStage, Person person) throws Exception {

        this.person = person;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        sidebarViewController = new SidebarViewController(this, primaryStage);
        calendarViewController = new CalendarViewController(this, primaryStage);
        Pane calendarPane = (AnchorPane) scene.lookup("#calendarPane");
        groupPopupViewController = new GroupPopupViewController(calendarPane);
        calendarPopupViewController = new CalendarPopupViewController(calendarPane);

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

        HTMLConnection connection = new HTMLConnection("https://www.github.com");
    }

    void fireAddGroup(ActionEvent event) {
        groupPopupViewController.show();
    }

    void fireAddCalendar(ActionEvent event) {
        calendarPopupViewController.show();
    }

    void fireLogout(ActionEvent event) {
        System.out.println("Log out");
    }

    protected Person getPerson() {
        return this.person;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("yolo");
    }
}
