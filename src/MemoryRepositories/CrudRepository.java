package MemoryRepositories;

import Domain.HasID;
import Validator.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alex on 07.11.2016.
 */
public interface CrudRepository<Elem extends HasID<IdType>, IdType>
{
    Elem add (Elem e) throws IOException, ExceptieValidareInRepository;
    void replace(Elem e) throws IOException, ExceptieValidareInRepository;
    void remove(IdType key) throws IOException;
    public Elem getByID(IdType key);
    public List<IdType> get_All_keys();
    public int get_all_keys_count();
    public default List<Elem> getAll() {return getByID(get_All_keys());}
    void setFilter(String... filter);
    List<IdType> get_All_filtered_keys();
    List<Elem> getByID(List<IdType> keys);

}