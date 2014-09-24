/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyp;

import it.sauronsoftware.jave.Encoder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

/**
 *
 * @author Isabirye Malcolm Ngobi
 */
public class MainScreenController implements Initializable  {

    private int height = 0;
    private int width = 0;
    private double duration = 0;
    private double frameRate = 0;
    private int Frames = 0;

    private int inCut;
    private int outCut;
    private int pptCounter = 0;

    private Boolean stop = false; 
    
    private BufferedImage nextFrame;
    
    @FXML
    private ProgressBar pb;

    @FXML
    private Label previewBoxLabel;

    @FXML
    private Label previewPowerPointBox;

    @FXML
    private Slider slider;

    @FXML
    private ListView listView = new ListView();

    @FXML
    private TableView<tableDataModel> tableView;

    private ObservableList importedFiles = FXCollections.observableArrayList();
    private ObservableList<tableDataModel> imageList = FXCollections.observableArrayList();
    private List<BufferedImage> initalPPTArr = new ArrayList<>();
    private ObservableList<tableDataModel> pptList = FXCollections.observableArrayList();

    @FXML
    public void handle() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");
        //Set extention filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));

        //Show open file dialog for more than one file - Multiple Files
        List<File> list = fileChooser.showOpenMultipleDialog(null);

        System.out.println(list);

        //Loops the list of Files chosen and prints out each of them
        for (int i = 0; i < list.size(); i++) {
            importedFiles.addAll(list.get(i).getName());

            System.out.println(list.get(i).getName());
        }

        listView.setItems(importedFiles);
    }

    @FXML
    public void AddClipBeta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        //Set extention filter
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"),
                new FileChooser.ExtensionFilter("Other Video Formats/ All Files", "*.*"));

        //Show open file dialog for one file
        File file = fileChooser.showOpenDialog(null);   

        Encoder encoder = new Encoder();
        try {

            duration = (double) encoder.getInfo(file).getDuration(); //In milliseconds
            frameRate = (double) encoder.getInfo(file).getVideo().getFrameRate();

            Frames = (int) ((duration / 1000) * frameRate);

            height = encoder.getInfo(file).getVideo().getSize().getHeight();
            width = encoder.getInfo(file).getVideo().getSize().getWidth();

        } catch (Exception e) {
        }

        VideoSegment clip = new VideoSegment(0, file.getPath(), 0, Frames, width, height);

        try {
            clip.openFile();
        } catch (Exception e) {}

        nextFrame = clip.getThumbnail();
        
        previewBoxLabel.setGraphic(new ImageView(SwingUtils.convertToFxImage(nextFrame)));

        //Loop all frames while adding the thumbnails to a table
       /*while(clip.getNextFrame() != null){Add code here}*/
        System.out.println(file.getPath());

        TableColumn<tableDataModel, ImageView> col = new TableColumn<>();
        tableView.getColumns().add(col);
        imageList.add(new tableDataModel(new ImageView(SwingUtils.convertToFxImage(nextFrame))));

        final EventHandler eh = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 1) {
                    TableCell c = (TableCell) t.getSource();
                    int arrayIndex = c.getIndex();

                    previewBoxLabel.setGraphic(c.getGraphic());
                    System.out.println(arrayIndex);
                    //Workaround - without this the cell would be blank
                    c.setGraphic(imageList.get(arrayIndex).getImage());
                } else {
                    t.consume();
                }
            }
        };

        col.setPrefWidth(50);
        col.setCellValueFactory(new PropertyValueFactory<tableDataModel, ImageView>("image"));

        col.setCellFactory(new Callback<TableColumn<tableDataModel, ImageView>, TableCell<tableDataModel, ImageView>>() {

            @Override
            public TableCell<tableDataModel, ImageView> call(TableColumn<tableDataModel, ImageView> p) {

                TableCell<tableDataModel, ImageView> cell = new TableCell<tableDataModel, ImageView>() {
                    protected void updateItem(ImageView item, boolean empty) {
                        // calling super here is very important - don't skip this!
                        super.updateItem(item, empty);
                        if (item != null) {
                            this.setGraphic(item);
                            this.setOnMouseClicked(eh);} 

                    }
                };
                System.out.println(cell.getIndex());
                return cell;
            }

        });
       
        int i = 0;
        //Change while loop condition to "nextFrame != null" if you want to read all frames of the file
        while (i != 50) {
            //imageList.add(new tableDataModel(new ImageView(convertToFxImage(clip.getThumbnail()))));
            try {
                nextFrame = clip.getNextFrame();
                imageList.add(new tableDataModel(new ImageView(SwingUtils.convertToFxImage(nextFrame))));
            } catch (Exception e) {
            }
            i++;
        }

        System.out.println(imageList);
        tableView.setItems(imageList);
        slider.setMax(imageList.size() - 1);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                //Sets current slider value to video frame
                ImageView image = imageList.get(new_val.intValue()).getImage();
                image.fitWidthProperty().bind(previewBoxLabel.widthProperty());
                image.fitHeightProperty().bind(previewBoxLabel.heightProperty());
                image.setPreserveRatio(true);
                //image.setSmooth(true);
                //image.setCache(true);
                //image.setFitWidth(640);
                //System.out.println("Image height is: " + image.getFitHeight() + "width: " + image.getFitWidth());

                //Sets current slider value to video frame
                if (!pptList.isEmpty()) {
                    ImageView pptImage = pptList.get(new_val.intValue()).getImage();
                    //Various image sizing properties
                    pptImage.fitHeightProperty().bind(previewPowerPointBox.heightProperty());
                    pptImage.fitWidthProperty().bind(previewPowerPointBox.widthProperty());
                    pptImage.setPreserveRatio(true);
                    pptImage.setSmooth(true);
                    pptImage.setCache(true);
                    previewPowerPointBox.setGraphic(pptImage);
                }
                previewBoxLabel.setGraphic(image);

            }
        });

        /*System.out.println(duration);
         System.out.println(frameRate);
         System.out.println(Frames);
         */
    }

    @FXML
    public void inCut() {
        inCut = (int) slider.getValue();
        System.out.println(inCut);
    }

    @FXML
    public void outCut() {
        outCut = (int) slider.getValue();
        System.out.println(outCut);
    }

    @FXML
    public void cut() {

        System.out.println("ImageList size: " + imageList.size());
        /*Remove the frames from after the outCut onwards and makes sure not to
         cut if the last frame is included as the outCut*/
        int imageListSize = imageList.size();
        
        int counter = outCut + 1;
        
        do{
            imageList.remove(counter);
            imageListSize = imageList.size();
        }while(counter != imageListSize);
        
        /*Remove the frames from before the inCut and makes sure not to
         cut if the first frame is included as the inCut*/
        for (int i = 0; i < inCut; i++) {
            imageList.remove(0);
        }

        slider.setMax(imageList.size() - 1);
    }

    @FXML
    public void playButton() {

        //Just in case someone clicks stop before they clicked play 
        stop = false;
        
        /*Sets the slider to 0 so the clip can be played 
         from the beginning if the slider is at the end */
        if (slider.getValue() == imageList.size() - 1) {
            slider.setValue(0);
        }

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                for (int i = (int) slider.getValue(); i < imageList.size(); i++) {

                    if (!stop && !slider.isPressed()) {
                        final int iFinal = i;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (pptList.isEmpty()) {
                                    previewBoxLabel.setGraphic(imageList.get(iFinal).getImage());
                                } else {
                                    previewBoxLabel.setGraphic(imageList.get(iFinal).getImage());
                                    previewPowerPointBox.setGraphic(pptList.get(iFinal).getImage());
                                }

                                slider.setValue(iFinal); //Update slider as video plays
                            }
                        }); //End of Platform.runLater

                        Thread.sleep((long) (1000 / frameRate));

                    } //End of if statement
                    else {
                        break;
                    } //End of else statement

                } //End of For loop

                return null;
            }
        };
        new Thread(task).start();
        stop = false;

    }

    @FXML
    public void stopButton() {
        stop = true;
    }

    public void addSlideBeta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        //Set extention filter
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
        //Show open file dialog for one file
        File file = fileChooser.showOpenDialog(null);

        try {
            FileInputStream is = new FileInputStream(file.getPath());
            SlideShow ppt = new SlideShow(is);
            Dimension pgsize = ppt.getPageSize();
            Slide[] slide = ppt.getSlides();
            is.close();

            int i = 0;
            int j = slide.length;
            System.out.println(slide.length);
           
            while (i < j) {

                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                //clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                //render
                slide[i].draw(graphics);

                //Add slides to array
                initalPPTArr.add(img);

                i++;
                System.out.println(i);
            }

            System.out.println("Out of loop");

            //Add the first slide consecutively
            for (int k = 0; k < imageList.size(); k++) {
                pptList.add(new tableDataModel(initalPPTArr.get(0)));
            }

            ImageView image = pptList.get(1).getImage();
            image.fitHeightProperty().bind(previewPowerPointBox.heightProperty());
            image.fitWidthProperty().bind(previewPowerPointBox.widthProperty());
            image.setPreserveRatio(true);
            image.setSmooth(true);
            image.setCache(true);
            previewPowerPointBox.setGraphic(image);

            TableColumn<tableDataModel, ImageView> col = new TableColumn<>();
            tableView.getColumns().add(col);
            col.setPrefWidth(50);
            col.setCellValueFactory(new PropertyValueFactory<tableDataModel, ImageView>("image"));

            col.setCellFactory(new Callback<TableColumn<tableDataModel, ImageView>, TableCell<tableDataModel, ImageView>>() {

                @Override
                public TableCell<tableDataModel, ImageView> call(TableColumn<tableDataModel, ImageView> p) {

                    TableCell<tableDataModel, ImageView> cell = new TableCell<tableDataModel, ImageView>() {
                        protected void updateItem(ImageView item, boolean empty) {
                            // calling super here is very important - don't skip this!
                            super.updateItem(item, empty);
                            if (item != null) {this.setGraphic(item);} 
                        }
                    };
                    return cell;
                }
            });

            tableView.setItems(pptList);

        } catch (Exception e) {
        }
        System.out.println("Done");
    }

    @FXML
    public void pptMarker() {

        if (pptCounter < initalPPTArr.size() - 1) {

            pptCounter++;
            
            System.out.println("PowerPoint Counter is: " + pptCounter);
            System.out.println("Inital PowerPoint Array size is: " + initalPPTArr.size());
            
            int sliderVal = (int) slider.getValue();

            for (int i = sliderVal; i < pptList.size(); i++) {

                pptList.set(i, new tableDataModel(initalPPTArr.get(pptCounter)));
                System.out.println("work");

            }

            ImageView image = pptList.get(sliderVal).getImage();
            image.fitHeightProperty().bind(previewPowerPointBox.heightProperty());
            image.fitWidthProperty().bind(previewPowerPointBox.widthProperty());
            image.setPreserveRatio(true);
            image.setSmooth(true);
            image.setCache(true);
            previewPowerPointBox.setGraphic(image);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
