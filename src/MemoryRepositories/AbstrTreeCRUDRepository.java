package MemoryRepositories;

import Domain.HasID;
import Validator.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;



public abstract class AbstrTreeCRUDRepository <Elem extends HasID<IdType>, IdType>
        implements CrudRepository<Elem, IdType>
{
    private TreeMap<IdType,Elem> elems;
    private Validator<Elem> val;

    public AbstrTreeCRUDRepository(Validator<Elem> v)
    {
        val=v;
        elems=new TreeMap<IdType, Elem>();
    }

    public Elem add(Elem e) throws IOException, ExceptieValidareInRepository
    {
        try {
            val.Validate(e);
            elems.put(e.getId(),e);
        }
        catch (ExceptieValidator exc) {
            throw new ExceptieValidareInRepository(exc.getMessage());
        }
        return e;
    }

    public void replace(Elem e) throws IOException, ExceptieValidareInRepository
    {
        try {
            val.Validate(e);
            elems.put(e.getId(),e);
        }
        catch (ExceptieValidator exc) {
            throw new ExceptieValidareInRepository(exc.getMessage());
        }
    }
    public void remove(IdType key) throws IOException
    {
        elems.remove(key);
    }

    public Elem getByID(IdType key)
    {
        return elems.get(key);
    }

    public List<Elem> getByID(List<IdType> keys)
    {
        ArrayList<Elem> l=new ArrayList<Elem>();
        for(IdType k:keys)
        {
            Elem e=elems.get(k);
            if(e==null)
                l.add(e);
        }
        return l;
    }

    public int get_all_keys_count()
    {
        return elems.size();
    }

    public List<Elem> getAll()
    {
        return elems.values().stream().collect(Collectors.toList());
    }

    public List<IdType> get_All_keys() { return elems.keySet().stream().collect(Collectors.toList());}

}
