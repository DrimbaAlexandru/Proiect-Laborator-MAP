package GUI;

import Domain.*;
import MVC.FCIController;
import PDFReportGenerator.PDFReportGenerator;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static javafx.stage.FileChooser.*;

/**
 * Created by Alex on 01.01.2017.
 */
public class raport_window
{
    private FCIController model;
    public raport_window(main_window _gui)
    {
        model=_gui.getModel();
    }
    int nr_filme;

    Stage localStage=new Stage();
    private Slider slider=new Slider();
    private Spinner<Integer> spinner=new Spinner<>();

    private ObservableList<Integer> list= FXCollections.observableArrayList();

    private ArrayList<statistica_film> raport;

    private void build_sorted_report()
    {
        TreeMap<Integer,statistica_film> map=new TreeMap<>();
        for(film f:model.getAllFilm())
        {
            map.put(f.getId(),new statistica_film(f.getId(),f.getTitlu()));
        }
        for(inchiriere i:model.getAllInchiriere())
        {
            map.get(i.getCod_f()).incrementInch();
        }
        raport=new ArrayList<>();
        raport.addAll(map.values().stream().sorted((s1,s2)->{return s2.getInch()-s1.getInch();}).collect(Collectors.toList()));
    }

    private void update_nr(int nr)
    {
        nr_filme=nr;
        spinner.getValueFactory().setValue(nr);
        slider.setValue(nr);
        update_raport();
    }

    public void run()
    {
        build_sorted_report();
        nr_filme=1;

        Group localroot=new Group();

        Scene localScene=new Scene(localroot, 410, 400, Color.WHITE);
        File f=new File("reel.png");
        localStage.getIcons().add(new Image("file:"+f.getAbsolutePath()));
        localStage.setResizable(true);
        localStage.setTitle("Raport");

        VBox mainLay=new VBox(5);
        mainLay.setPrefHeight(400);
        mainLay.setMinWidth(410);
        mainLay.setPadding(new Insets(5,5,5,5));
        localStage.setResizable(false);

        HBox spinner_row=new HBox(5);

        slider.setMin(1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setMax(model.getSizeFilm());
        slider.setValue(nr_filme);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);

        slider.setOnMouseClicked(event -> {
            update_nr((int)slider.getValue());
        });
        slider.setOnMouseDragged((event)-> {
            update_nr((int)slider.getValue());
        });

        int size=model.getSizeFilm();
        System.out.println(size);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,size,1));

        spinner.setPrefWidth(70);
        spinner.setEditable(true);

        slider.setPrefWidth(330);

        spinner.setOnMouseClicked((event -> {
            update_nr(spinner.getValue());
        }));


        spinner.setOnKeyReleased((event -> {
            update_nr(spinner.getValue());
        }));

        try {
            FXMLLoader l=new FXMLLoader();

            f=new File("fxml\\menuraport.fxml");
            l.setLocation(new URL("file:///"+f.getAbsolutePath()));
            l.setController(this);
            mainLay.getChildren().add(l.load());
        }
        catch (Exception e) {e.printStackTrace();}

        spinner_row.getChildren().addAll(slider,spinner);
        mainLay.getChildren().addAll(spinner_row);

        TableView<Integer> tabel=new TableView<>();
        tabel.setEditable(false);
        tabel.setItems(list);

        TableColumn<Integer,Integer> col_nr_crt = new TableColumn<>("Nr. crt.");
        col_nr_crt.setCellValueFactory((param ->
        {
            return new ReadOnlyObjectWrapper<Integer>(param.getValue()+1);
        }));

        TableColumn<Integer,Integer> col_nr_inchirieri=new TableColumn<>("Nr. inchirieri");
        col_nr_inchirieri.setCellValueFactory((param)->{
            return new ReadOnlyObjectWrapper<>((raport.get(param.getValue()).getInch()));
        });

        TableColumn<Integer,String> col_nume_film=new TableColumn<>("Titlu film");
        col_nume_film.setCellValueFactory((param)-> {
            return new ReadOnlyObjectWrapper<>(model.getFilmByID(raport.get(param.getValue()).getId_film()).getTitlu());
        });

        tabel.getColumns().clear();
        col_nr_crt.setPrefWidth(50);
        col_nr_inchirieri.setPrefWidth(85);
        col_nume_film.setMinWidth(260);
        tabel.getColumns().addAll(col_nr_crt,col_nr_inchirieri,col_nume_film);
        col_nr_inchirieri.setResizable(false);

        mainLay.getChildren().addAll(tabel);

        localStage.setScene(localScene);
        localroot.getChildren().addAll(mainLay);
        col_nume_film.setPrefWidth(200);
        update_raport();
        localStage.show();
    }

    private void update_raport()
    {
        list.clear();
        for(int i=0;i<nr_filme;i++)
        {
            list.add(i);
        }
    }

    @FXML
    private void handel_save_pdf()
    {
        FileChooser fc=new FileChooser();
        fc.setInitialDirectory(new File("C:\\"));
        fc.getExtensionFilters().add((new ExtensionFilter("PDF Document (*.pdf)","*.pdf")));
        File result=fc.showSaveDialog(localStage);
        if(result!=null)
            try {
                PDFReportGenerator.createPdf(result.getAbsolutePath(),raport.subList(0,nr_filme));
            } catch (IOException e) {
                main_window.display_message_Box("Eroare la scrierea documentului!");
            }
    }

    @FXML
    private void handel_close()
    {
        localStage.close();
    }
}
