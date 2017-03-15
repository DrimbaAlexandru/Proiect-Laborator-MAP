package MVC;


import Domain.client;
import Domain.film;
import Domain.inchiriere;
import MemoryRepositories.CrudRepository;
import SQL_Repositories.I_SQLRepository;
import Validator.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by Alex on 09.11.2016.
 */


public class FCIController extends Observable {
    private CrudRepository<film,Integer> ctrl_film;
    private CrudRepository<client,Integer> ctrl_client;
    private CrudRepository<inchiriere,Integer> ctrl_inchiriere;
    private boolean is_fully_SQL;

    public FCIController(CrudRepository cf, CrudRepository cc, CrudRepository ci) {
        ctrl_client = cc;
        ctrl_film = cf;
        ctrl_inchiriere = ci;
        is_fully_SQL=(cc instanceof I_SQLRepository && cf instanceof I_SQLRepository && ci instanceof I_SQLRepository);
    }

    public void myNotify(String arg) {
        setChanged();
        notifyObservers(arg);
    }
    ///Methods for adding elements into the DB
    public void adauga_film(film e) throws IOException, ExceptieValidareInRepository {
        ctrl_film.add(e);
        myNotify("film");
    }
    public void adauga_client(client e) throws IOException, ExceptieValidareInRepository {
        ctrl_client.add(e);
        myNotify("client");
    }
    public void adauga_inchiriere(inchiriere e) throws IOException, ExceptieValidareInRepository {
        ctrl_inchiriere.add(e);
        myNotify("inchiriere");
    }

    ///Methods for replacing elements in the DB
    public void modifica_film(film e) throws IOException, ExceptieValidareInRepository {
        ctrl_film.replace(e);
        myNotify("film");
        myNotify("inchiriere");
    }
    public void modifica_client(client e) throws IOException, ExceptieValidareInRepository {
        ctrl_client.replace(e);
        myNotify("client");
        myNotify("inchiriere");
    }

    ///Methods for deleting elements from the DB
    public void sterge_film(Integer key) throws IOException {
        if(ctrl_film.getByID(key)!=null)
        {
            ctrl_film.remove(key);
            myNotify("film");
            if(!is_fully_SQL)
                for(inchiriere i:getAllInchiriere())
                    if(i.getCod_f()==key)
                        sterge_inchiriere(i.getId());
            myNotify("inchiriere");
        }
    }
    public void sterge_client(Integer key) throws IOException {
        if (ctrl_client.getByID(key) != null)
        {
            ctrl_client.remove(key);
            myNotify("client");
            if(!is_fully_SQL)
                for(inchiriere i:getAllInchiriere())
                    if(i.getCod_c()==key)
                        sterge_inchiriere(i.getId());
            myNotify("inchiriere");
        }
    }
    public void sterge_inchiriere(Integer key) throws IOException {

        if (ctrl_inchiriere.getByID(key)!=null)
        {
            ctrl_inchiriere.remove(key);
            myNotify("inchiriere");
        }
    }

    ///Methods for getting elements by their ID
    public film getFilmByID(Integer key)            { return ctrl_film.getByID(key);        }
    public client getClientByID(Integer key)        { return ctrl_client.getByID(key);      }
    public inchiriere getInchiriereByID(Integer key){ return ctrl_inchiriere.getByID(key);  }

    ///Methods for getting all elements
    public List<film> getAllFilm()            { return ctrl_film.getAll();      }
    public List<client> getAllClient()        { return ctrl_client.getAll();    }
    public List<inchiriere> getAllInchiriere(){ return ctrl_inchiriere.getAll();}

    /**
     * @param filter List of three Strings: titlu, an, regizor
     */
    public void setFilterFilm(String... filter)         { ctrl_film.setFilter(filter); myNotify("film");}
    /**
     * @param filter List of two strings: nume, adresa
     */
    public void setFilterClient(String... filter)       {ctrl_client.setFilter(filter);myNotify("client");}

    /**
     * @param filter List of two strings: nume client, titlu film
     */
    public void setFIlterInchiriere(String... filter)   {ctrl_inchiriere.setFilter(filter);myNotify("inchiriere");}

    /**
     * @return A list of all the keys in the DB
     */
    public List<Integer> getAllKeysFilm()        { return ctrl_film.get_All_keys();       }
    public List<Integer> getAllKeysClient()      { return ctrl_client.get_All_keys();     }
    public List<Integer> getAllKeysInchiriere()  { return ctrl_inchiriere.get_All_keys(); }

    public List<Integer> getRangeFilteredKeysFilm(int firstIndex, int lastIndex)   { return ctrl_film.get_All_filtered_keys().subList(firstIndex,Math.min(lastIndex+1,ctrl_film.get_All_filtered_keys().size()));}
    public List<Integer> getRangeFilteredKeysClient(int firstIndex, int lastIndex)  { return ctrl_client.get_All_filtered_keys().subList(firstIndex,Math.min(lastIndex+1,ctrl_client.get_All_filtered_keys().size()));}
    public List<Integer> getRangeFilteredKeysInchiriere(int firstIndex, int lastIndex)  { return ctrl_inchiriere.get_All_filtered_keys().subList(firstIndex,Math.min(lastIndex+1,ctrl_inchiriere.get_All_filtered_keys().size()));}

    public int getSizeFilm() {return ctrl_film.get_all_keys_count();}
    public int getSizeClient(){return ctrl_client.get_all_keys_count();}
    public int getSizeInchiriere(){return ctrl_inchiriere.get_all_keys_count();}

    public int getSizeFilteredFilm() {return ctrl_film.get_All_filtered_keys().size();}
    public int getSizeFilteredClient(){return ctrl_client.get_All_filtered_keys().size();}
    public int getSizeFilteredInchiriere(){return ctrl_inchiriere.get_All_filtered_keys().size();}

}

/*
class MVC.ControllerFilmSQL extends MVC.AbstractController<film,Integer>
{
    MVC.ControllerFilmSQL(String filmeFilePath) throws IOException,MemoryRepositories.ExceptieValidareInRepository
    {
        super(new AbstractCrudPlainTextFileRepository<film,Integer>(
                new Validator.ValidatorFilm(),
                filmeFilePath,
                x->{return x.getTitlu()+"|"+x.getAn_aparitie()+"|"+x.getId()+"|"+x.getRegizor();},
                s-> {
                    String[] r = s.split("\\|");
                    film f;
                    try {
                        f = new film(r[0], Integer.parseInt(r[1]), Integer.parseInt(r[2]), r[3]);
                    }
                    catch (Exception e)
                    {
                        f=null;
                    }
                    return f;
                }
        ));
    }
}



class MVC.ControllerClientSQL extends MVC.AbstractController<client,Integer>
{
    MVC.ControllerClientSQL(String clientiFilePath) throws IOException,MemoryRepositories.ExceptieValidareInRepository
    {
        super(new ControllerClient_XML(
                new Validator.ValidatorClient(),
                clientiFilePath
        ));
    }
}


class MVC.ControllerInchirieriSQL extends MVC.AbstractController<inchiriere,Integer>
{
    MVC.ControllerInchirieriSQL(String inchirieriFilePath) throws IOException,MemoryRepositories.ExceptieValidareInRepository
    {
        super(new AbstractCrudSerializedRepository<inchiriere,Integer>(
                new Validator.ValidatorInchiriere(),
                inchirieriFilePath
        ));
    }
    public void clear_references(int obsoleteFilmID, int obsoleteClientID) throws IOException
    {
        List<inchiriere> l;
        l=getAll().stream().collect(Collectors.toList());
        for(inchiriere e:l)
        {
            if(e.getCod_f()==obsoleteFilmID || e.getCod_c()==obsoleteClientID)
                repo.remove(e.getId());
        }
    }
}*/

/*class GraspController extends Observable{
    private AbstractCrudFileRepository<film> repo_film;
    private AbstractCrudFileRepository<client> repo_client;
    private AbstractCrudFileRepository<inchiriere> repo_inchiriere;

    public GraspController(String filmeFilePath, String clientiFilePath, String inchirieriFilePath) throws IOException
    {
        repo_film=new AbstractCrudPlainTextFileRepository<film>(
                new Validator.ValidatorFilm(),
                filmeFilePath,
                x->{return x.getTitlu()+"|"+x.getAn_aparitie()+"|"+x.getCod()+"|"+x.getRegizor();},
                s-> {
                    String[] r = s.split("\\|");
                    film f;
                    try {
                         f = new film(r[0], Integer.parseInt(r[1]), Integer.parseInt(r[2]), r[3]);
                    }
                    catch (Exception e)
                    {
                        f=null;

                    }
                    return f;
                }
        );
        repo_film.read_from_file();

        repo_client=new ControllerClient_XML(
                new Validator.ValidatorClient(),
                clientiFilePath
        );
        repo_client.read_from_file();

        repo_inchiriere=new AbstractCrudSerializedRepository<inchiriere>(
                new Validator.ValidatorInchiriere(),
                inchirieriFilePath
        );
        repo_inchiriere.read_from_file();
        myNotify("film");
        myNotify("client");
        myNotify("inchiriere");
    }

    public film get_film_by_id(int id)
    {
        film f=new film("",0,id,"");
        for (film fp:repo_film.getLista())
            if(f.equals(fp))
            {return fp;}
        return null;
    }

    public client get_client_by_id(int id)
    {
        client c=new client("",id,"");
        for (client cp:repo_client.getLista())
            if(c.equals(cp))
            {return cp;}
        return null;
    }

    private void clear_references(Object o)
    {
        int i=0;
        inchiriere e;
        List<inchiriere> l=repo_inchiriere.getLista();
        while(i<l.size())
        {
            e=l.get(i);
            if(get_client_by_id(e.getCod_c())==null || get_film_by_id(e.getCod_f())==null)
                l.remove(i);
            else
                i++;
        }
        try {
            repo_inchiriere.save_to_file();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        myNotify("inchiriere");
    }

    public void myNotify(String arg)
    {
        setChanged();
        notifyObservers(arg);
    }

    public void add(Object e) throws Throwable
    {
        try {
            Field f=this.getClass().getDeclaredField("repo_"+e.getClass().getCanonicalName());
            f.setAccessible(true);
            Object o=f.get(this);
            Method m=o.getClass().getDeclaredMethod("add",Object.class);
            m.invoke(o,(Object) e);
            myNotify(e.getClass().getCanonicalName());
        }
        catch (java.lang.reflect.InvocationTargetException e1)
        {
            throw e1.getTargetException();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void modifica(int poz,Object e)
    {
        try {
            Field f=this.getClass().getDeclaredField("repo_"+e.getClass().getCanonicalName());

            f.setAccessible(true);
            Object o=f.get(this);
            Method m=o.getClass().getDeclaredMethod("modifica",int.class,Object.class);
            m.invoke(o,poz, e);
            myNotify(e.getClass().getCanonicalName());
        }
        catch (java.lang.reflect.InvocationTargetException e1)
        {
            try {
                throw e1.getCause();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public Object sterge(int poz, String className)
    {
        Object value=null;
        Object val2=null;
        try {
            Field f=this.getClass().getDeclaredField("repo_"+className);
            f.setAccessible(true);
            Object o=f.get(this);
            Method m=o.getClass().getDeclaredMethod("sterge",int.class);
            Method m2=o.getClass().getDeclaredMethod("get_Elem",int.class);
            val2=m2.invoke(o,poz);
            value = m.invoke(o,poz);
            if("filmclient".contains(className))
                clear_references(val2);
            myNotify(className);
        }
        catch (java.lang.reflect.InvocationTargetException e1)
        {
            try {
                throw e1.getCause();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return value;
    }

    public Object get_Elem(int poz, String className)
    {
        Object value=null;
        try {
            Field f=this.getClass().getDeclaredField("repo_"+className);
            f.setAccessible(true);
            Object o=f.get(this);
            Method m=o.getClass().getDeclaredMethod("get_Elem",int.class);
            value = m.invoke(o,poz);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return value;
    }

    public int get_Elem_position(Object e)
    {
        Object value=null;
        try {
            Field f=this.getClass().getDeclaredField("repo_"+e.getClass().getCanonicalName());
            f.setAccessible(true);
            Object o=f.get(this);
            Method m=o.getClass().getDeclaredMethod("get_Elem_position",Object.class);
            value = m.invoke(o,e);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return (int) value;
    }

    public int get_all_keys_count(String className)
    {
        Object value=null;
        try {
            Field f=this.getClass().getDeclaredField("repo_"+className);
            f.setAccessible(true);
            Object o=f.get(this);
            Method m=o.getClass().getDeclaredMethod("get_all_keys_count");
            value = m.invoke(o);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return (int) value;
    }

    public Object getLista(String className)
    {
        Object value=null;
        try {
            Field f=this.getClass().getDeclaredField("repo_"+className);
            f.setAccessible(true);
            Object o=f.get(this);
            Method m=o.getClass().getDeclaredMethod("getLista");
            value = m.invoke(o);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return value;
    }

    public <Elem> List<Elem> filtreaza(List<Elem> l, Predicate<Elem> pred)
    {
        List<Elem> l2=new ArrayList<>();
        for(Elem e:l)
        {
            if(pred.test(e))
            {
                l2.add(e);
            }
        }
        return l2;
    }
}
*/