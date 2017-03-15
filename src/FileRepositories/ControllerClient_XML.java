package FileRepositories;

import Domain.client;

import Validator.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Alex on 02.12.2016.
 */




public class ControllerClient_XML extends AbstractCrudFileRepository<client,Integer>
{
    private String[] last_filtre = new String[] {"",""};

    public ControllerClient_XML(Validator<client> v, String path)throws IOException,ExceptieValidareInRepository
    {
        super(v,path);
        read_from_file();
    }

    class HandlerXML_Client extends DefaultHandler{

        boolean bnume;
        boolean bcod_membru;
        boolean badresa;

        String nume;
        String cod;
        String adresa;

        public void startElement(String uri, String localName,String qName,
                                 Attributes attributes) throws SAXException {

            if (qName.equalsIgnoreCase("NUME")) {
                bnume = true;
            }

            if (qName.equalsIgnoreCase("cod_client")) {
                bcod_membru = true;
            }

            if (qName.equalsIgnoreCase("ADRESA")) {
                badresa = true;
            }

        }

        public void endElement(String uri, String localName,
                               String qName) throws SAXException {

            //System.out.println("End Element :" + qName);
            if(qName.equalsIgnoreCase("client"))
            {
                try {
                    client c=new client(nume,Integer.parseInt(cod),adresa);
                    add(c);
                }
                catch (IOException|ExceptieValidareInRepository e)
                {
                    throw new SAXException(e);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {

            if (bnume) {
                //System.out.println("Nume : " + new String(ch, start, length));
                nume=new String(ch, start, length);
                bnume=false;
            }

            if (bcod_membru) {
                //System.out.println("Cod : " + new String(ch, start, length));
                cod = new String(ch, start, length);
                bcod_membru = false;
            }

            if (badresa) {
                //System.out.println("Adresa : " + new String(ch, start, length));
                adresa=new String(ch, start, length);
                badresa=false;
            }
        }
    };

    protected void read_from_file() throws IOException,ExceptieValidareInRepository
    {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            HandlerXML_Client handler = new HandlerXML_Client();
            saxParser.parse(filePath, handler);
        }
        catch (SAXException e)
        {
            Exception cause=e.getException();
            if((cause instanceof ExceptieValidareInRepository) || (cause instanceof IOException))
                throw new ExceptieValidareInRepository(cause.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void save_to_file() throws IOException
    {
        Collection<client> lista=getAll();
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<clienti>");
            bw.newLine();
            for (client e : lista) {
                bw.write("\t<client>");bw.newLine();
                bw.write("\t\t<nume>"+e.getNume()+"</nume>");bw.newLine();
                bw.write("\t\t<cod_client>"+e.getId()+"</cod_client>");bw.newLine();
                bw.write("\t\t<adresa>"+e.getAdresa()+"</adresa>");bw .newLine();
                bw.write("\t</client>");bw.newLine();
            }
            bw.write("</clienti>");
            bw.newLine();
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IOException("Eroare la scriere in fisier!");
        }
    }

    /**
     * @param filter #1 String: nume, #2 String: adresa
     * @return lista de chei ce identifica clienti ce corespund filtrarii
     */
    @Override
    public void setFilter(String... filter) {
        last_filtre=filter;
    }

    @Override
    public List<Integer> get_All_filtered_keys() {
        ArrayList<Integer>  result=new ArrayList<>();
        for(client c:getAll())
        {
            if(c.getNume().contains(last_filtre[0]) && c.getAdresa().contains(last_filtre[1]))
                result.add(c.getId());
        }
        return result;
    }
}
