package controllers;

import helpers.CalendarHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Group;
import models.Person;
import server.Server;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private final String MAINVIEW_PATH = "../views/MainView.fxml";
    private CalendarViewController calendarViewController;
    private SidebarViewController sidebarViewController;
    private GroupPopupViewController groupPopupViewController;
    private EditGroupPopupViewController editGroupPopupViewController;
    private MenuBar menuBar;
    private Person person;
    private int weekNumber;
    private LocalDate firstDayOfWeek = CalendarHelper.getFirstDateOfWeek();
    private Label weekNumberLabel;
    private Stage primaryStage;
    private Label yearLabel;
    private ArrayList<Label> labelList = new ArrayList<>();
    private ArrayList<String> labelNames = new ArrayList<>();

    private Group currentlySelectedGroup;


    public void setCurrentlySelectedGroup(Group currentlySelectedGroup) {
        this.currentlySelectedGroup = currentlySelectedGroup;
    }

    public Group getCurrentlySelectedGroup() {
        return currentlySelectedGroup;
    }

    public MainViewController(Stage primaryStage, Person person) throws Exception {

        this.person = person;
        currentlySelectedGroup = new Group("Min kalender");
        currentlySelectedGroup.setCalendar_id(person.getCalendarId());

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        LocalDate firstDayOfWeek = CalendarHelper.getFirstDateOfWeek();
        calendarViewController = new CalendarViewController(this, primaryStage);
        sidebarViewController = new SidebarViewController(this, calendarViewController,primaryStage);
        Pane calendarPane = (AnchorPane) scene.lookup("#calendarPane");
        groupPopupViewController = new GroupPopupViewController(calendarPane, this, primaryStage);
        editGroupPopupViewController = new EditGroupPopupViewController(calendarPane, this, primaryStage);
        Button nextWeek = (Button) scene.lookup("#nextWeek");
        Button pastWeek = (Button) scene.lookup("#pastWeek");
        weekNumberLabel = (Label) scene.lookup("#ukenr");
        yearLabel = (Label) scene.lookup("#yearLabel");
        weekNumber = CalendarHelper.getCurrentWeekNumber();
        labelNames.addAll(Arrays.asList("#manLabel","#tirLabel","#onsLabel","#torLabel","#freLabel","#lørLabel","#sønLabel"));
        for(int i = 0; i<7 ;i++){
            labelList.add((Label) scene.lookup(labelNames.get(i)));
        }

        // Listeners
        menuBar = (MenuBar) main.getChildrenUnmodifiable().get(1); // TODO: Find a better way to do this
        ObservableList<Menu> menus = menuBar.getMenus();

        // Account menu
        Menu accountMenu = menus.get(0);

        accountMenu.setText(person.getEmail());
        ObservableList<MenuItem> accountMenuItems = accountMenu.getItems();

        MenuItem logoutMenuItem = accountMenuItems.get(0);

        logoutMenuItem.setOnAction(e -> fireLogout(e));

        //Change week
        nextWeek.setOnAction(e -> fireNextWeek(e));
        pastWeek.setOnAction(e -> fireLastWeek(e));
        weekNumberLabel.setText("UKE "+ getSelectedWeekNumber());

        //Get dates
        generateDateLabels(firstDayOfWeek);

        // Item menu
        Menu itemMenu = menus.get(1);
        ObservableList<MenuItem> itemMenuItems = itemMenu.getItems();

        MenuItem leaveGroupMenuItem = itemMenuItems.get(0);
        leaveGroupMenuItem.setOnAction(e -> fireLeaveGroup(e));

        MenuItem addGroupMenuItem = itemMenuItems.get(1);
        addGroupMenuItem.setOnAction(e -> fireAddGroup(e));

        MenuItem editGroupMenuItem = itemMenuItems.get(2);
        editGroupMenuItem.setOnAction(e -> fireEditGroup(e));

        //HTTPConnection connection = new HTTPConnection("https://www.github.com");
    }


    void fireAddGroup(ActionEvent event) {
        groupPopupViewController.show();
    }

    void fireEditGroup(ActionEvent event) {
        if (currentlySelectedGroup!=null){
            editGroupPopupViewController.show(currentlySelectedGroup);
        }

    }

    void fireLeaveGroup(ActionEvent event) {
        if (currentlySelectedGroup != null){
            Server.getInstance().leaveGroup(currentlySelectedGroup);
            this.refresh();
        }

    }

    void fireLogout(ActionEvent event) {
        new LoginViewController(primaryStage);
    }

    void fireNextWeek(ActionEvent event) {
        firstDayOfWeek = firstDayOfWeek.plusWeeks(1);
        generateDateLabels(firstDayOfWeek);
        weekNumberLabel.setText("UKE " + CalendarHelper.getWeekNumber(firstDayOfWeek));
        calendarViewController.generateDayPanes(firstDayOfWeek);
        calendarViewController.populateWeekWithAppointments(firstDayOfWeek);
    }

    void fireLastWeek(ActionEvent event) {
        firstDayOfWeek = firstDayOfWeek.minusWeeks(1);
        generateDateLabels(firstDayOfWeek);
        weekNumberLabel.setText("UKE " + CalendarHelper.getWeekNumber(firstDayOfWeek));
        calendarViewController.generateDayPanes(firstDayOfWeek);
        calendarViewController.populateWeekWithAppointments(firstDayOfWeek);
    }

    public void generateDateLabels(LocalDate firstDayOfWeek){
        for(int i=0;i<7;i++){
            labelList.get(i).setText(Character.toUpperCase(labelNames.get(i).charAt(1))+labelNames.get(i).substring(2,4)
                    +" " + firstDayOfWeek.getDayOfMonth() + "." + firstDayOfWeek.getMonthValue());
            firstDayOfWeek = firstDayOfWeek.plusDays(1);
        }
        yearLabel.setText(firstDayOfWeek.getYear() + "");
    }

    public LocalDate getFirstDayOfWeek() {return firstDayOfWeek;}

    public int getSelectedWeekNumber() {
        return weekNumber;
    }

    public void setSelectedWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    protected Person getCurrentPerson() {
        return this.person;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("yolo");
    }

    public void refresh(){
        sidebarViewController.refresh();
        calendarViewController.refresh();
    }

}
