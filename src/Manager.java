import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.*;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private Image img1, img2, img3;
    private ImageView currentImage;
    private List<Slider> sliders;
    private ColorAdjust ca;
    private Label info;

    public Manager(Stage pstage){
        this.sliders = new ArrayList<>();

        this.info = new Label();

        this.ca = new ColorAdjust();

        this.img1 = new Image("Images/Image1.jpg");
        this.img2 = new Image("Images/Image2.jpg");
        this.img3 = new Image("Images/Image3.jpg");

        this.currentImage = new ImageView(this.img1);
        this.currentImage.setFitHeight(500);
        this.currentImage.setFitWidth(500);

        pstage.setScene(init());
    }

    private Scene init(){
        // Menu
        MenuBar mb = new MenuBar();
        RadioMenuItem img1M, img2M, img3M;
        MenuItem reInM;

        ToggleGroup imgToggle = new ToggleGroup();

        img1M = new RadioMenuItem("Image #1");
        img1M.setSelected(true);
        img1M.setToggleGroup(imgToggle);
        img1M.setOnAction(event -> update(1));

        img2M = new RadioMenuItem("Image #2");
        img2M.setToggleGroup(imgToggle);
        img2M.setOnAction(event -> update(2));

        img3M = new RadioMenuItem("Image #3");
        img3M.setToggleGroup(imgToggle);
        img3M.setOnAction(event -> update(3));

        reInM = new MenuItem("Reinitialiser");
        reInM.setOnAction(event -> reset());

        Menu imgMenu = new Menu("Images");
        Menu acMenu = new Menu("Actions");
        imgMenu.getItems().addAll(img1M, img2M, img3M);
        acMenu.getItems().addAll(reInM);

        mb.getMenus().addAll(imgMenu,acMenu);


        //context menu
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(imgMenu, acMenu);

            this.currentImage.setOnContextMenuRequested( event ->
                    contextMenu.show(this.currentImage, event.getScreenX(), event.getScreenY()));



        //sliders
        VBox sliderVbox = new VBox();
        sliderVbox.setSpacing(20);
        sliderVbox.setAlignment(Pos.CENTER);

        for (int counter = 0; counter < 4; counter++) {
            Slider sl = new Slider(-100, 100, 0);
            sl.setShowTickLabels(true);
            sl.setShowTickMarks(true);
            sl.setMajorTickUnit(25);
            sl.setMinorTickCount(0);
            sl.setMinWidth(200);
            this.sliders.add(sl);
        }

        slidersUpdate();


        // Labels pour les sliders
        List<Label> lbls = new ArrayList<>();
        Label lb1 = new Label("Luminosité");
        lbls.add(lb1);
        Label lb2 = new Label("Constrast");
        lbls.add(lb2);
        Label lb3 = new Label("Teinte");
        lbls.add(lb3);
        Label lb4 = new Label("Saturation");
        lbls.add(lb4);

        //alterner entre label and slider
        for(int counter=0; counter < 4; counter++){
            sliderVbox.getChildren().add(lbls.get(counter));
            sliderVbox.getChildren().add(this.sliders.get(counter));
        }

        HBox mainbox = new HBox();
        mainbox.setSpacing(50);
        mainbox.getChildren().addAll(this.currentImage, sliderVbox);
        mainbox.setAlignment(Pos.CENTER);


        //INFOS
        Rectangle bg = new Rectangle((int) Screen.getPrimary().getBounds().getWidth(), 40);
        bg.setFill(Color.LIGHTGRAY);

        this.info.setText("Lab 6!");

        StackPane st = new StackPane();
        st.getChildren().addAll(bg, this.info);
        st.setAlignment(Pos.TOP_LEFT);


        // main border Pane
        BorderPane bp = new BorderPane();
        bp.setTop(mb);
        bp.setCenter(mainbox);
        bp.setBottom(st);
        bp.setMinSize(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        return new Scene(bp);
    }

    private void update(int index){
        switch (index){
            case 1: this.currentImage.setImage(this.img1);
            break;
            case 2: this.currentImage.setImage(this.img2);
            break;
            case 3: this.currentImage.setImage(this.img3);
            break;
            default: System.out.print("Imposible!");
        }

        this.info.setText("Changement d'image");
    }

    private void slidersUpdate(){
        this.sliders.get(0).valueProperty().addListener((observable, oldValue, newValue) -> {
            this.ca.setBrightness(newValue.doubleValue() / 100);
            this.info.setText("Changement de luminosité");});

        this.sliders.get(1).valueProperty().addListener((observable, oldValue, newValue) ->{
                this.ca.setContrast(newValue.doubleValue() / 100);
                this.info.setText("Changement de contraste");});

        this.sliders.get(2).valueProperty().addListener((observable, oldValue, newValue) ->{
                this.ca.setHue(newValue.doubleValue() / 100);
                this.info.setText("Changement de colour");});

        this.sliders.get(3).valueProperty().addListener((observable, oldValue, newValue) ->{
                this.ca.setSaturation(newValue.doubleValue() / 100);
                this.info.setText("Changement de saturation");});

        this.currentImage.setEffect(this.ca);
    }

    private void reset(){
        for (Slider sl: this.sliders
             ) {
            sl.setValue(0);
        }

        this.info.setText("Reset");
    }
}
