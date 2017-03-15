package GUI; /**
 * Created by Alex on 04.12.2016.
 */
import Domain.*;
import MVC.FCIController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Alex on 03.12.2016.
 */
public class main_window implements Observer
{
    private Stage thisStage;
    private Scene scene;
    private boolean exit_with_logout=false;
    private user loggedUser;

    public void setLoggedUser(user loggedUser) {
        this.loggedUser = loggedUser;
    }

    private static int items_on_page=10;
    private int page_f=0,page_c=0,page_i=0;

    private FCIController model;
    public FCIController getModel() {
        return model;
    }

    private boolean inchirieri_display_order=true;
    private Group root=new Group();

    @FXML
    private ObservableList<Integer> lista_filme = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Integer> lista_inchirieri=FXCollections.observableArrayList();
    private ObservableList<Integer> lista_clienti = FXCollections.observableArrayList();

    @FXML
    private TableView<Integer> tableView_filme = new TableView<>();
    @FXML
    private TableView<Integer> tableView_inchirieri = new TableView<>();
    private TableView<Integer> tableView_clienti = new TableView<>();

    @FXML
    private TextField TF_filter_titlu=new TextField(),TF_filter_regizor=new TextField(),TF_filter_an=new TextField();
    @FXML
    private TextField TF_filterI_nume=new TextField(),TF_filterI_titlu=new TextField();
    private TextField TF_filter_nume=new TextField(),TF_filter_adresa=new TextField();

    @FXML
    private Pagination pagination_filme,pagination_clienti,pagination_inchirieri;
    @FXML
    private TextField jump_to_page_filme,jump_to_page_clienti, jump_to_page_inchirieri;

    @FXML
    private Button btn_add_film,btn_mod_film,btn_del_film,btn_add_inchiriere,btn_del_inchiriere;

    /*----------------------METODE------------------------------------*/

    public void update(Observable obs, Object arg) {
        String msg = (String) arg;

        if (msg != null && obs != null) {
            try {
                if (msg.contains("client"))
                {
                    pagination_clienti.setPageCount(model.getSizeFilteredClient()/items_on_page+1);
                    update_lista_clienti(model.getRangeFilteredKeysClient(page_c*items_on_page,(page_c+1)*items_on_page-1));
                }
                if (msg.contains("film"))
                {
                    pagination_filme.setPageCount(model.getSizeFilteredFilm()/items_on_page+1);
                    update_lista_filme(model.getRangeFilteredKeysFilm(page_f*items_on_page,(page_f+1)*items_on_page-1));
                }
                if (msg.contains("inchiriere"))
                {
                    pagination_inchirieri.setPageCount(model.getSizeFilteredInchiriere()/items_on_page+1);
                    update_lista_inchirieri(model.getRangeFilteredKeysInchiriere(page_i*items_on_page,(page_i+1)*items_on_page-1));
                }
            }
            catch (NullPointerException e)
            {e.printStackTrace();}
        }
    }

    public void setTF_filter_titlu_text(String filter){TF_filter_titlu.setText(filter);filtrare_filme();};
    public void setTF_filter_nume_text(String filter){TF_filter_nume.setText(filter);filtrare_clienti();};
    public String getTF_filter_titlu_text(){return TF_filter_titlu.getText();};
    public String getTF_filter_nume_text(){return TF_filter_nume.getText();};


    @FXML
    private void filtrare_filme()
    {
        model.setFilterFilm(TF_filter_titlu.getText(),TF_filter_an.getText(),TF_filter_regizor.getText());
        pagination_filme.setPageCount(model.getSizeFilteredFilm()/items_on_page+1);
    }

    private void filtrare_clienti()
    {
        model.setFilterClient(TF_filter_nume.getText(),TF_filter_adresa.getText());
        pagination_clienti.setPageCount(model.getSizeFilteredClient()/items_on_page+1);
    }

    @FXML
    public void filtrare_inchirieri()
    {
        String nume=TF_filterI_nume.getText(),titlu=TF_filterI_titlu.getText();
        model.setFIlterInchiriere(nume,titlu);
        pagination_inchirieri.setPageCount(model.getSizeFilteredInchiriere()/items_on_page+1);
    }

    public void show_raport_window()
    {
        if(model.getSizeFilm()>0)
        {
            raport_window win=new raport_window(this);
            win.run();
        }
        else
            display_message_Box("Nu exista niciun film in baza de date!");
    }

    public static void display_message_Box(String msg)
    {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message!");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**Functie ce actualizeaza ListView-ul ce contine clientii.
    * @param l Lista de intregi ce reprezinta chei ce identifica unic clienti
    * */
    private void update_lista_clienti(List<Integer> l) { lista_clienti.clear(); if(model.getSizeClient()!=0) lista_clienti.setAll(l); }

    /**Functie ce actualizeaza ListView-ul ce contine filmele.
     * @param l Lista de intregi ce reprezinta chei ce identifica unic filme
     * */
    private void update_lista_filme(List<Integer> l) { lista_filme.clear(); if(model.getSizeFilm()!=0) lista_filme.setAll(l); }

    /**Functie ce actualizeaza ListView-ul ce contine inchirierile.
     * @param l Lista de intregi ce reprezinta chei ce identifica unic inchirieri
     * */
    private void update_lista_inchirieri(List<Integer> l) { lista_inchirieri.clear(); if(model.getSizeInchiriere()!=0) lista_inchirieri.setAll(l); }


    /** Afiseaza fereastra de editat detalii client.
     * @param key Cheie de identificare a clientului(necesar doar daca se modifica)
     * @param action String ce reprezinta comportamentul fersetrei: "adauga"/"modifica"
     */
    private void edit_client_window(int key,String action)
    {
        client_edit_window win=new client_edit_window(this);
        win.run(key,action);
    }

    @FXML
    /** Afiseaza fereastra de inchiriere filme.*/
    private void edit_inchiriere_window() {
        inchiriere_edit_window win=new inchiriere_edit_window(this);
        win.run();
    }

    @FXML
    private void handel_adauga_inchiriere() { edit_inchiriere_window(); }

    @FXML
    private void handle_btn_del_inchiriere()
    {
        int index=tableView_inchirieri.getSelectionModel().getSelectedIndex();
        if(index<0)
            display_message_Box("Nu a fost selectat niciun element!");
        else
            try {
                model.sterge_inchiriere(tableView_inchirieri.getItems().get(index));
            } catch (IOException e) {
                display_message_Box(e.getMessage());
            }
    }

    @FXML
    private void handel_set_ordine_inchiriere_client()
    {
        inchirieri_display_order=true;
        reset_tableView_inchirieri_columns();
        filtrare_inchirieri();
    }

    @FXML
    private void handel_set_ordine_inchiriere_film()
    {
        inchirieri_display_order=false;
        reset_tableView_inchirieri_columns();
        filtrare_inchirieri();
    }

    /** Afiseaza fereastra de editat detalii film.
     * @param key Cheie de identificare a filmului
     * @param action String ce reprezinta comportamentul fersetrei: "adauga"/"modifica"
     */
    private void edit_film_window(int key,final String action)
    {
        film_edit_window win=new film_edit_window(this);
        win.run(key,action);
    }

    @FXML
    private void handel_adauga_film()
    {
        edit_film_window(0,"adauga");
    }

    @FXML
    private void handel_modifica_film()
    {
        int index=tableView_filme.getSelectionModel().getSelectedIndex();
        if(index<0)
            edit_film_window(-1,"modifica");
        else
            edit_film_window(lista_filme.get(index),"modifica");
    }

    private VBox get_clienti_area()
    {
        VBox clienti_area=new VBox(5);
        HBox left_top_layer=new HBox(5);
        Button btn_add_client=new Button("Adauga");
        Button btn_mod_client=new Button("Modifica");
        Button btn_del_client=new Button("Sterge");
        btn_add_client.setDisable(!loggedUser.Is_super_user());
        btn_mod_client.setDisable((!loggedUser.Is_super_user()));
        btn_del_client.setDisable((!loggedUser.Is_super_user()));
        Separator sep=new Separator(Orientation.VERTICAL);
        sep.setPrefWidth(5);
        TF_filter_nume=new TextField();
        TF_filter_nume.setPromptText("Nume");
        TF_filter_adresa=new TextField();
        TF_filter_adresa.setPromptText("Adresa");

        btn_add_client.setOnMouseClicked((event)-> {edit_client_window(0,"adauga");});
        btn_mod_client.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index=tableView_clienti.getSelectionModel().getSelectedIndex();
                if(index<0)
                    edit_client_window(-1,"modifica");
                else
                    edit_client_window(lista_clienti.get(index),"modifica");
            }
        });
        btn_del_client.setOnMouseClicked((event)-> {
            handel_btn_del_client();
        });
        TF_filter_adresa.setOnKeyReleased((event)-> { filtrare_clienti();});
        TF_filter_nume.setOnKeyReleased((event)-> { filtrare_clienti();});

        Separator invisep=new Separator(Orientation.VERTICAL);
        invisep.setVisible(false);
        invisep.setPrefWidth(5);
        Label filtru=new Label("Filtru:");
        filtru.setPrefHeight(30);
        left_top_layer.getChildren().addAll(invisep,btn_add_client,btn_mod_client,btn_del_client,sep,filtru,TF_filter_nume,TF_filter_adresa);
        left_top_layer.setPrefHeight(30);

        tableView_clienti.setEditable(false);
        tableView_clienti.setMaxWidth(600);
        tableView_clienti.setMaxHeight(269);
        tableView_clienti.setItems(lista_clienti);

        TableColumn<Integer,String> col_ID=new TableColumn<>("ID");
        TableColumn<Integer,String> col_nume=new TableColumn<>("Nume");
        TableColumn<Integer,String> col_adresa=new TableColumn<>("Adresa");
        col_ID.setCellValueFactory((param)->
            {return new ReadOnlyObjectWrapper<String>(Integer.toString(model.getClientByID(param.getValue()).getId()));});
        col_nume.setCellValueFactory((param)->
            {return new ReadOnlyObjectWrapper<String>(model.getClientByID(param.getValue()).getNume());});
        col_adresa.setCellValueFactory((param)->
            {return new ReadOnlyObjectWrapper<String>(model.getClientByID(param.getValue()).getAdresa());});
        col_ID.setPrefWidth(50);
        col_nume.setPrefWidth(250);
        col_adresa.setPrefWidth(250);

        tableView_clienti.getColumns().setAll(col_ID,col_nume,col_adresa);
        tableView_clienti.setOnKeyReleased((event)->{
            if(event.getCode()== KeyCode.DELETE)
                handel_btn_del_client();
        });

        HBox buffer=new HBox(0);
        buffer.setPrefHeight(275);
        Separator invisep2=new Separator(Orientation.VERTICAL);
        invisep2.setVisible(false);
        invisep2.setPrefWidth(5);

        HBox box_paginator=new HBox(0);
        pagination_clienti = new Pagination();
        pagination_clienti.setPrefHeight(45);
        pagination_clienti.setPrefWidth(511);
        pagination_clienti.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                page_c=pagination_clienti.getCurrentPageIndex();

                if(!jump_to_page_clienti.getText().equals(Integer.toString(page_c+1)))
                    jump_to_page_clienti.setText(Integer.toString(page_c+1));
                filtrare_clienti();
                return new Separator(Orientation.HORIZONTAL);
            }
        });

        jump_to_page_clienti=new TextField();
        jump_to_page_clienti.setPrefWidth(70);
        jump_to_page_clienti.setPromptText("Jump to...");
        jump_to_page_clienti.setOnKeyReleased((event -> {
            try {
                int nr = Integer.parseInt(jump_to_page_clienti.getText());
                if (nr > 0 && nr <= pagination_clienti.getPageCount()) {
                    page_c = nr-1;
                    pagination_clienti.setCurrentPageIndex(nr-1);
                    filtrare_clienti();
                }
            }
            catch (NumberFormatException e)
            {;}
        }));
        pagination_clienti.setPageCount(model.getSizeClient()/items_on_page+1);

        box_paginator.getChildren().addAll(pagination_clienti,jump_to_page_clienti);

        buffer.getChildren().addAll(invisep2,tableView_clienti);
        tableView_clienti.setPrefWidth(590);

        Separator sep2=new Separator(Orientation.HORIZONTAL);
        sep2.setPrefHeight(3);

        clienti_area.getChildren().addAll(new Separator(Orientation.HORIZONTAL),left_top_layer,sep2, buffer,box_paginator);
        return clienti_area;
    }

    private VBox get_film_area()
    {
        try {
            File f=new File("fxml\\filme.fxml");

            FXMLLoader l=new FXMLLoader(new URL("file:///"+f.getAbsolutePath()));
            l.setController(this);
            VBox v=l.load();

            btn_add_film.setDisable(!loggedUser.Is_super_user());
            btn_del_film.setDisable(!loggedUser.Is_super_user());
            btn_mod_film.setDisable(!loggedUser.Is_super_user());

            tableView_filme.setItems(lista_filme);
            TableColumn<Integer,String> col_ID=new TableColumn<>("ID");
            col_ID.setCellValueFactory(
                    (param)->
                    {return new ReadOnlyObjectWrapper<>(Integer.toString(model.getFilmByID(param.getValue()).getId()));});
            TableColumn<Integer,String> col_titlu=new TableColumn<>("Titlu");
            col_titlu.setCellValueFactory(
                    (param)->
                    {return new ReadOnlyObjectWrapper<>(model.getFilmByID(param.getValue()).getTitlu());}
            );
            TableColumn<Integer,String> col_an=new TableColumn<>("An aparitie");
            col_an.setCellValueFactory(
                    (param)->
                    {return new ReadOnlyObjectWrapper<>(Integer.toString(model.getFilmByID(param.getValue()).getAn_aparitie()));}
            );
            TableColumn<Integer,String> col_reg=new TableColumn<>("Regizor");
            col_reg.setCellValueFactory(
                    (param)->
                    {return new ReadOnlyObjectWrapper<>(model.getFilmByID(param.getValue()).getRegizor());}
            );
            col_ID.setPrefWidth(50);
            col_an.setPrefWidth(75);
            col_titlu.setPrefWidth(250);
            col_reg.setPrefWidth(200);
            tableView_filme.getColumns().clear();
            tableView_filme.getColumns().addAll(col_ID,col_titlu,col_an,col_reg);
            tableView_filme.setEditable(false);
            tableView_filme.setOnKeyReleased((event)->{
                if(event.getCode()== KeyCode.DELETE)
                    handle_btn_del_film();
            });
            TF_filter_titlu.setOnKeyReleased((event)->
            {
                filtrare_filme();
            });
            TF_filter_an.setOnKeyReleased((event)->
            {
                filtrare_filme();
            });
            TF_filter_regizor.setOnKeyReleased((event)->
            {
                filtrare_filme();
            });

            pagination_filme.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer param) {
                    page_f=pagination_filme.getCurrentPageIndex();

                    if(!jump_to_page_filme.getText().equals(Integer.toString(page_f+1)))
                        jump_to_page_filme.setText(Integer.toString(page_f+1));
                    filtrare_filme();
                    return new Separator(Orientation.HORIZONTAL);
                }
            });


            jump_to_page_filme.setOnKeyReleased((event -> {
                    try {
                        int nr = Integer.parseInt(jump_to_page_filme.getText());
                        if (nr > 0 && nr <= pagination_filme.getPageCount()) {
                            page_f = nr-1;
                            pagination_filme.setCurrentPageIndex(nr-1);
                            filtrare_filme();
                        }
                    }
                    catch (NumberFormatException e)
                    {;}
            }));
            pagination_filme.setPageCount(model.getSizeFilm()/items_on_page+1);
            return v;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VBox(5);
    }

    private VBox get_inchirieri_area()
    {
        try {
            File f=new File("fxml\\inchiriere.fxml");
            FXMLLoader l=new FXMLLoader(new URL("file:///"+f.getAbsolutePath()));
            l.setController(this);
            VBox v= l.load();

            btn_add_inchiriere.setDisable(!loggedUser.Is_super_user());
            btn_del_inchiriere.setDisable(!loggedUser.Is_super_user());
            TF_filterI_nume.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    filtrare_inchirieri();
                }
            });
            TF_filterI_titlu.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    filtrare_inchirieri();
                }
            });
            tableView_inchirieri.setItems(lista_inchirieri);
            tableView_inchirieri.setOnKeyReleased((event)->{
                if(event.getCode()== KeyCode.DELETE)
                    handle_btn_del_inchiriere();
            });

            pagination_inchirieri.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer param) {
                    page_i=pagination_inchirieri.getCurrentPageIndex();

                    if(!jump_to_page_inchirieri.getText().equals(Integer.toString(page_i+1)))
                        jump_to_page_inchirieri.setText(Integer.toString(page_i+1));
                    filtrare_inchirieri();
                    return new Separator(Orientation.HORIZONTAL);
                }
            });

            jump_to_page_inchirieri.setOnKeyReleased((event -> {
                try {
                    int nr = Integer.parseInt(jump_to_page_inchirieri.getText());
                    if (nr > 0 && nr <= pagination_inchirieri.getPageCount()) {
                        page_i = nr-1;
                        pagination_inchirieri.setCurrentPageIndex(nr-1);
                        filtrare_inchirieri();
                    }
                }
                catch (NumberFormatException e)
                {;}
            }));
            pagination_inchirieri.setPageCount(model.getSizeInchiriere()/items_on_page+1);
            reset_tableView_inchirieri_columns();
            return v;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VBox(5);
    }

    private void reset_tableView_inchirieri_columns()
    {
        TableColumn<Integer,String> col_ID=new TableColumn<>("ID");
        TableColumn<Integer,String> col_nume=new TableColumn<>("Nume client");
        TableColumn<Integer,String> col_film=new TableColumn<>("Titlu film");
        col_ID.setPrefWidth(50);
        col_nume.setPrefWidth(250);
        col_film.setPrefWidth(250);
        col_ID.setCellValueFactory((param)->{
            return new ReadOnlyObjectWrapper<>(Integer.toString(model.getInchiriereByID(param.getValue()).getId()));});
        col_film.setCellValueFactory((param)->{
            return new ReadOnlyObjectWrapper<>(model.getFilmByID(model.getInchiriereByID(param.getValue()).getCod_f()).getTitlu());});
        col_nume.setCellValueFactory((param)->{
            return new ReadOnlyObjectWrapper<>(model.getClientByID(model.getInchiriereByID(param.getValue()).getCod_c()).getNume());});

        if(inchirieri_display_order)
            tableView_inchirieri.getColumns().setAll(col_ID,col_nume,col_film);
        else
            tableView_inchirieri.getColumns().setAll(col_ID,col_film,col_nume);

    }

    private HBox get_bottom_layer()
    {
        HBox down_layer=new HBox();
        VBox clienti_area=get_clienti_area();
        Node filme_area=get_film_area();
        Node inchirieri_area=get_inchirieri_area();

        Tab tab_filme=new Tab("Filme");
        Tab tab_clienti=new Tab("Clienti");
        Tab tab_inchirieri=new Tab("Inchirieri");

        tab_filme.setContent(filme_area);
        tab_clienti.setContent(clienti_area);
        tab_inchirieri.setContent(inchirieri_area);

        tab_clienti.setClosable(false);
        tab_filme.setClosable(false);
        tab_inchirieri.setClosable(false);

        TabPane tabs=new TabPane();
        tabs.setMinWidth(1024);
        tabs.getTabs().addAll(tab_clienti,tab_filme,tab_inchirieri);

        down_layer.getChildren().addAll(tabs);
        return down_layer;
    }

    private Node getMainLayer()
    {
        VBox front_layer=new VBox(20);
        Pane top_layer=get_top_layer();
        HBox down_layer=get_bottom_layer();

        front_layer.getChildren().addAll(top_layer,down_layer);
        return front_layer;
    }
    @FXML
    private void handel_menu_logout(){exit_with_logout=true;thisStage.close();}
    @FXML
    private void handel_menu_close(){exit_with_logout=false;thisStage.close();}
    @FXML
    private void handel_menu_raport(){show_raport_window();}
    @FXML
    private void handel_menu_resetare()
    {
        TF_filter_adresa.setText("");
        TF_filter_nume.setText("");
        TF_filter_an.setText("");
        TF_filter_regizor.setText("");
        TF_filter_titlu.setText("");
        TF_filterI_nume.setText("");
        TF_filterI_titlu.setText("");
        filtrare_clienti();
        filtrare_filme();
        filtrare_inchirieri();
    }
    @FXML
    private void handel_menu_about(){display_message_Box("Proiect facultativ MAP \n (C) D. Alex 2016-2017");}

    private Pane get_top_layer()
    {
        try {
            File f=new File("fxml\\menubar.fxml");
            FXMLLoader l=new FXMLLoader(new URL("file:///"+f.getAbsolutePath()));
            l.setController(this);
            Pane v= l.load();
            return v;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pane();
    }

    public boolean run(Stage _stage)
    {
        root.getChildren().clear();
        exit_with_logout=false;
        root.getChildren().addAll(getMainLayer());
        Stage stage=new Stage();
        File f=new File("reel.png");
        stage.getIcons().add(new Image("file:"+f.getAbsolutePath()));
        stage.setResizable(false);
        stage.setTitle("Numele programului");
        stage.setScene(scene);
        thisStage=stage;
        stage.showAndWait();
        loggedUser=null;
        return exit_with_logout;
    }

    @FXML
    private void handle_btn_del_film() {
        int index=tableView_filme.getSelectionModel().getSelectedIndex();
        if(index<0)
            display_message_Box("Niciun element selectat");
        else
            {
            index=lista_filme.get(index);
                try {
                    model.sterge_film(index);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    private void handel_btn_del_client()
    {
        int index=tableView_clienti.getSelectionModel().getSelectedIndex();
        if(index<0)
            display_message_Box("Niciun element selectat");
        else
        {
            index=lista_clienti.get(index);
            try {
                model.sterge_client(index);
            } catch (IOException e) {
                display_message_Box(e.getMessage());
            }
        }
    }

    public main_window(FCIController model, user u)
    {
        scene=new Scene(root, 600, 480, Color.WHITESMOKE);
        loggedUser=u;
        System.out.println("Started app");
        this.model=model;
        getMainLayer();
    }
    //public GUI.main_window() {}
}