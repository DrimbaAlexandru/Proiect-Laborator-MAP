package GUI;

import Domain.client;
import Domain.film;
import Domain.inchiriere;
import MVC.FCIController;
import Validator.ExceptieValidareInRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Alex on 01.01.2017.
 */
public class inchiriere_edit_window
{
    private main_window gui;
    private FCIController model;

    @FXML
    private Button btn_EI_confirm=new Button("A"),btn_EI_cancel=new Button("B");
    @FXML
    private ListView<Integer> view_EI_select_client, view_EI_select_film;
    @FXML
    private TextField TF_Filtru_titlu_film_inchiriere,TF_Filtru_nume_client_inchiriere;
    private String last_titlu="",last_nume="";

    private ObservableList<Integer> lista_filme;
    private ObservableList<Integer> lista_clienti;

    private Observer updater;

    public inchiriere_edit_window(main_window _gui)
    {
        gui=_gui;
        model=gui.getModel();
        lista_filme = FXCollections.observableArrayList();
        lista_clienti = FXCollections.observableArrayList();

    }

    private void refresh_lists()
    {
        lista_filme.setAll(model.getRangeFilteredKeysFilm(0,model.getSizeFilteredFilm()));
        lista_clienti.setAll(model.getRangeFilteredKeysClient(0,model.getSizeFilteredClient()));
    }

    public void run()
    {
        Group localroot=new Group();
        Stage localStage=new Stage();
        Scene localScene=new Scene(localroot, 400, 300, Color.WHITESMOKE);

        File f=new File("reel.png");
        localStage.getIcons().add(new Image("file:"+f.getAbsolutePath()));

        localStage.setResizable(false);
        localStage.setScene(localScene);
        localStage.setTitle("adauga inchiriere");

        VBox mainLayout=new VBox();
        try {
            f=new File("fxml\\add_inchiriere.fxml");
            FXMLLoader l=new FXMLLoader(new URL("file:///"+f.getAbsolutePath()));
            l.setController(this);
            mainLayout=l.load();
            updater=new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    refresh_lists();
                    TF_Filtru_nume_client_inchiriere.setText(gui.getTF_filter_nume_text());
                    TF_Filtru_titlu_film_inchiriere.setText(gui.getTF_filter_titlu_text());
                    TF_Filtru_nume_client_inchiriere.positionCaret(TF_Filtru_nume_client_inchiriere.getText().length());
                    TF_Filtru_titlu_film_inchiriere.positionCaret(TF_Filtru_titlu_film_inchiriere.getText().length());
                }
            };
            model.addObserver(updater);
            updater.update(null,null);
            view_EI_select_client.setItems(lista_clienti);
            view_EI_select_film.setItems(lista_filme);
            view_EI_select_film.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
                @Override
                public ListCell<Integer> call(ListView<Integer> param) {
                    class myCell extends ListCell<Integer>
                    {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                            super.updateItem(item, empty);
                            if(empty)
                                setText("");
                            else
                            {
                                film f=model.getFilmByID(item);
                                setText((getIndex()+1)+". "
                                        + f.getTitlu()+", "
                                        + f.getAn_aparitie()+", "
                                        + f.getRegizor());
                            }
                        }
                    }
                    return new myCell();
                }
            });
            view_EI_select_client.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
                @Override
                public ListCell<Integer> call(ListView<Integer> param) {
                    class myCell extends ListCell<Integer>
                    {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                            super.updateItem(item,empty);
                            if(empty)
                                setText("");
                            else
                            {
                                client c=model.getClientByID(item);
                                if(c==null)
                                    setText("");
                                else
                                {
                                    int i=getIndex();
                                    setText((i+1)+". "+ c.getNume()+", "+c.getAdresa());
                                }
                            }
                        }
                    }
                    return new myCell();
                }

            });
            btn_EI_confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int index_f,index_c;
                    index_c=view_EI_select_client.getSelectionModel().getSelectedIndex();
                    index_f=view_EI_select_film.getSelectionModel().getSelectedIndex();
                    if(index_c<0 || index_f<0)
                    {
                        main_window.display_message_Box("Nu au fost selectate destule campuri!");
                        return;
                    }
                    inchiriere i=new inchiriere(view_EI_select_film.getItems().get(index_f),
                            view_EI_select_client.getItems().get(index_c));
                    try {
                        model.adauga_inchiriere(i);
                    } catch (IOException | ExceptieValidareInRepository e)
                    {
                        main_window.display_message_Box(e.getMessage());
                    }
                    localStage.close();
                }
            });

            btn_EI_cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    localStage.close();
                }
            });
            TF_Filtru_titlu_film_inchiriere.setOnKeyReleased((event)->
            {
                if(!(TF_Filtru_titlu_film_inchiriere.getText().equals(last_titlu)))
                {
                    gui.setTF_filter_titlu_text(TF_Filtru_titlu_film_inchiriere.getText());
                    last_titlu=TF_Filtru_titlu_film_inchiriere.getText();
                }
            });
            TF_Filtru_nume_client_inchiriere.setOnKeyReleased((event)->
            {
                if(!(TF_Filtru_nume_client_inchiriere.getText().equals(last_nume)))
                {
                    gui.setTF_filter_nume_text(TF_Filtru_nume_client_inchiriere.getText());
                    last_nume=TF_Filtru_nume_client_inchiriere.getText();
                }
            });


            localroot.setOnKeyReleased(event -> {
                if(event.getCode()== KeyCode.ESCAPE)
                    localStage.close();
                else if(event.getCode()==KeyCode.ENTER)
                    btn_EI_confirm.getOnMouseClicked().handle(null);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            gui.display_message_Box(e.getClass().getCanonicalName()+": "+e.getMessage());
        }

        localroot.getChildren().add(mainLayout);
        localStage.showAndWait();
        model.deleteObserver(updater);
    }
}
