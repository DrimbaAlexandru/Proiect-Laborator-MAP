package Builder;

import Domain.client;
import Domain.film;
import Domain.inchiriere;
import MVC.FCIController;
import MemoryRepositories.CrudRepository;
import SQL_Repositories.I_SQLRepository;
import SQL_Repositories.SQLServer_Connection;
import Validator.Validator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by Alex on 10.01.2017.
 */
public class Builder {
    public Builder(){}

    private Object create_Object(String className, Object... args) throws InstantiationException
    {
        try {
            Class c=Class.forName(className);
            Constructor cons = c.getConstructors()[0];
            Object o=cons.newInstance(args);
            return o;
        }
        catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new InstantiationException("Eroare la construirea obiectelor!");
        }
    }

    private class FCI_Handler extends DefaultHandler {

        public FCI_Handler(){}
        private boolean b_tag_ctr_type = false, b_tag_SQL_Connection;
        private boolean b_tag_ctr_film = false, b_tag_ctr_client = false, b_tag_ctr_inchiriere = false;
        private boolean b_tag_val_film = false, b_tag_val_client = false, b_tag_val_inchiriere = false;
        private boolean b_tag_Constructor = false, b_tag_className = false, b_tag_argument = false;
        private int argument_position = -1;
        private int no_arguments = 0;

        private String DbName, Ip, Port;

        private SQLServer_Connection connection = null;
        private CrudRepository<film, Integer> repo_f;
        private CrudRepository<client, Integer> repo_c;
        private CrudRepository<inchiriere, Integer> repo_i;
        private Validator<film> val_f;
        private Validator<client> val_c;
        private Validator<inchiriere> val_i;
        private boolean isSQL = false;

        public CrudRepository<client, Integer> getRepo_c() { return repo_c; }
        public CrudRepository<film, Integer> getRepo_f() { return repo_f;}
        public CrudRepository<inchiriere, Integer> getRepo_i() { return repo_i; }
        public boolean isSQL() { return isSQL; }

        String className;
        Object[] args;

        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            switch (qName) {
                case "Controller_Type":
                    b_tag_ctr_type = true;
                    isSQL= Boolean.parseBoolean(attributes.getValue("isSQL"));
                    break;
                case "SQL_Connection":
                    b_tag_SQL_Connection = true;
                    DbName = attributes.getValue("DBName");
                    Ip = attributes.getValue("ServerIP");
                    Port = attributes.getValue("ServerPort");
                    break;
                case "Validator_film":
                    b_tag_val_film = true;
                    break;
                case "Constructor":
                    b_tag_Constructor = true;
                    try {
                        no_arguments = Integer.parseInt(attributes.getValue("arg_count"));
                        args = new Object[no_arguments];
                    } catch (NumberFormatException e) {
                        no_arguments = 0;
                    }
                    break;
                case "className":
                    b_tag_className = true;
                    break;
                case "argument":
                    b_tag_argument = true;
                    try {
                        argument_position = Integer.parseInt(attributes.getValue("nr"));
                    } catch (NumberFormatException e) {
                        argument_position = -1;
                    }
                    break;
                case "Validator_client":
                    b_tag_val_client = true;
                    break;
                case "Validator_inchiriere":
                    b_tag_val_inchiriere = true;
                    break;
                case "Controller_film":
                    b_tag_ctr_film = true;
                    break;
                case "Controller_client":
                    b_tag_ctr_client = true;
                    break;
                case "Controller_inchiriere":
                    b_tag_ctr_inchiriere = true;
            }
        }

        public void endElement(String uri, String localName,
                               String qName) throws SAXException {
            try {
                switch (qName) {
                    case "Controller_Type":
                        b_tag_ctr_type = false;
                        break;
                    case "SQL_Connection":
                        if (b_tag_SQL_Connection)
                            try {
                                if (isSQL)
                                    connection = new SQLServer_Connection(DbName, Ip, Port);
                            } catch (IOException e) {
                                throw new SAXException(e.getMessage());
                            }
                        b_tag_SQL_Connection = false;
                        break;
                    case "Validator_film":
                        b_tag_val_film = false;
                        val_f = (Validator<film>) create_Object(className, args);
                        break;
                    case "Constructor":
                        b_tag_Constructor = false;
                        break;
                    case "className":
                        b_tag_className = false;
                        break;
                    case "argument":
                        b_tag_argument = false;
                        break;
                    case "Validator_client":
                        b_tag_val_client = false;
                        val_c = (Validator<client>) create_Object(className, args);
                        break;
                    case "Validator_inchiriere":
                        b_tag_val_inchiriere = false;
                        val_i = (Validator<inchiriere>) create_Object(className, args);
                        break;
                    case "Controller_film":
                        b_tag_ctr_film = false;
                        repo_f = (CrudRepository<film, Integer>) create_Object(className, args);
                        break;
                    case "Controller_client":
                        b_tag_ctr_client = false;
                        repo_c = (CrudRepository<client, Integer>) create_Object(className, args);
                        break;
                    case "Controller_inchiriere":
                        b_tag_ctr_inchiriere = false;
                        repo_i = (CrudRepository<inchiriere, Integer>) create_Object(className, args);
                        break;
                }
            }
            catch (InstantiationException e)
            {
                throw new SAXException(e.getMessage());
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            String chars=new String(ch,start,length);
            if(b_tag_className && b_tag_Constructor)
                className=chars;
            if(b_tag_argument  && b_tag_Constructor)
                switch (chars){
                    case "Validator_film": args[argument_position]=val_f;break;
                    case "Validator_client": args[argument_position]=val_c;break;
                    case "Validator_inchiriere": args[argument_position]=val_i;break;
                    case "SQL_Connection": args[argument_position]=connection.getConnection();break;
                    case "Controller_film": args[argument_position]=repo_f;break;
                    case "Controller_client": args[argument_position]=repo_c;break;
                    default:                  args[argument_position]=chars;break;
                }
        }
    }

    public FCIController build() throws IOException
    {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            FCI_Handler handler = new FCI_Handler();
            saxParser.parse("config file.xml", handler);
            return new FCIController(handler.getRepo_f(),handler.getRepo_c(),handler.getRepo_i());
        }
        catch (SAXException e)
        {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
