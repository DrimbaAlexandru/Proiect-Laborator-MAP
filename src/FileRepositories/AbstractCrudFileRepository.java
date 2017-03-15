package FileRepositories;

import Domain.HasID;
import MemoryRepositories.AbstrTreeCRUDRepository;
import Validator.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alex on 04.01.2017.
 */
public abstract class AbstractCrudFileRepository<Elem extends HasID<IdType>,IdType>
        extends AbstrTreeCRUDRepository<Elem,IdType>
{
    protected String filePath;
    protected abstract void read_from_file() throws IOException, ExceptieValidareInRepository;
    protected abstract void save_to_file() throws IOException;
    public AbstractCrudFileRepository(Validator<Elem> v, String path)
    {
        super(v);
        filePath=path;
    }
    public Elem add (Elem e) throws IOException, ExceptieValidareInRepository
    {
        super.add(e);
        save_to_file();
        return e;
    }

    public void replace(Elem e) throws IOException, ExceptieValidareInRepository
    {
        super.replace(e);
        save_to_file();
    }

    public void remove(IdType key) throws IOException
    {
        super.remove(key);
        save_to_file();
    }
}