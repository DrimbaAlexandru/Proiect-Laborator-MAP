package FileRepositories;

import Domain.HasID;
import Validator.*;

import java.io.*;
import java.util.Collection;
import java.util.function.Function;

/**
 * Created by Alex on 04.01.2017.
 */
public abstract class AbstractCrudPlainTextFileRepository<Elem extends HasID<IdType>,IdType>
        extends AbstractCrudFileRepository<Elem,IdType>
{
    private Function<Elem,String> Serializare;
    private Function<String,Elem> Deserializare;

    public AbstractCrudPlainTextFileRepository
            (Validator<Elem> v, String path, Function<Elem,String> Ser_Func, Function<String,Elem> Deser_Func)
            throws IOException, ExceptieValidareInRepository
    {
        super(v,path);
        Serializare=Ser_Func;
        Deserializare=Deser_Func;
    }

    protected void read_from_file() throws IOException,ExceptieValidareInRepository
    {
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            {
                String line;
                while((line=br.readLine())!=null)
                {
                    Elem e=Deserializare.apply(line);
                    super.add(e);
                }
                br.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IOException("Eroare la citire din fisier!");
        }
    }

    protected void save_to_file() throws IOException
    {
        Collection<Elem> temp_List=super.getAll();
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            for (Elem e : temp_List) {
                String line = Serializare.apply(e);
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IOException("Eroare la scriere in fisier!");
        }
    }

}