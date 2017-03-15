package FileRepositories;

import Domain.HasID;
import Validator.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alex on 04.01.2017.
 */
public abstract class AbstractCrudSerializedRepository<Elem extends HasID<IdType>,IdType>
        extends AbstractCrudFileRepository<Elem,IdType>
{
    public AbstractCrudSerializedRepository(Validator<Elem> v, String path) throws IOException, ExceptieValidareInRepository
    {
        super(v,path);
    }

    protected void read_from_file() throws IOException,ExceptieValidareInRepository
    {
        ArrayList<Elem> lista;
        File f=new File(filePath);
        if(!f.exists() || f.length()==0)
            lista=new ArrayList<Elem>();
        else
            try {
                FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                lista = (ArrayList<Elem>) in.readObject();
                in.close();
                fileIn.close();
            }
            catch(ClassNotFoundException c) {
                c.printStackTrace();
                return;
            }
            catch (ClassCastException|IOException c)
            {
                c.printStackTrace();
                throw new IOException("Eroare la deserializare/citire din fisier!");
            }
        for(Elem e:lista)
        {
            super.add(e);
            e.setIdUsed(e.getId());
        }

    }

    protected void save_to_file() throws IOException
    {
        ArrayList<Elem> lista=new ArrayList<Elem>();
        Collection<Elem> col=getAll();
        for(Elem e : col)
            lista.add(e);
        try{
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(lista);
            out.close();
            fileOut.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IOException("Eroare la scriere in fisier!");
        }
    }
}