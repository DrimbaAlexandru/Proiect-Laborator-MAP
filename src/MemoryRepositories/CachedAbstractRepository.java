package MemoryRepositories;

import Domain.HasID;

import Validator.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by Alex on 03.01.2017.
 */
public class CachedAbstractRepository<Elem extends HasID<IdType>,IdType> implements CrudRepository<Elem,IdType>
{
    private int max_cache_size=128;
    private int total_size;
    private String[] last_filter=null;
    private Elem qqq;

    //A list that contains the latest used keys at it's start and the oldest used keys at the end
    private LinkedList<IdType> cached_keys_list;

    //A list of the keys of all the elements that correspond to the last filter applied
    private List<IdType> keys;

    //Map of cached elements
    private TreeMap<IdType,Elem> cache;

    //SQL Repository where the elements will be added/removed/gotten from
    private CrudRepository<Elem,IdType> Repository;

    /**
     * Constructs a CachedRepository based on an CRUD Repository
     * @param _repo
     */
    public CachedAbstractRepository(CrudRepository<Elem,IdType>_repo,String... allPassFilter)
    {
        Repository =_repo;
        total_size=_repo.get_all_keys_count();
        cached_keys_list=new LinkedList<>();
        keys=_repo.get_All_keys();
        last_filter=allPassFilter;
        cache=new TreeMap<>();
    }

    /// Method that assures that the cache contains no more than a given amount of elements
    private void assure_cache_size()
    {
        if(cache.size()>=max_cache_size) {
            System.out.println("Eliminat din cache "+qqq.getClass().getCanonicalName()+" cu cheia "+ cached_keys_list.getLast());
            cache.remove(cached_keys_list.removeLast());
        }
    }

    /// Method that brings to cache an entry with a given key from the DB
    private void bring_from_DB(IdType key)
    {
        if(!cache.containsKey(key))
        {
            Elem e= Repository.getByID(key);
            qqq=e;
            if(e!=null)
            {
                assure_cache_size();
                System.out.println("Adus in cache "+e.getClass().getCanonicalName()+" cu cheia "+key);
                cache.put(e.getId(),e);
                cached_keys_list.addFirst(e.getId());
            }
        }
    }

    public Elem add(Elem e) throws IOException, ExceptieValidareInRepository
    {
        Elem added= Repository.add(e);
        total_size= Repository.get_all_keys_count();
        setFilter(last_filter);
        return added;
    }

    public void replace(Elem e) throws IOException,ExceptieValidareInRepository
    {
        Repository.replace(e);
        if(cache.containsKey(e.getId()))
            cache.put(e.getId(),e);
        setFilter(last_filter);
    }

    public void remove(IdType key) throws IOException
    {

        if(cache.containsKey(key))
        {
            cache.remove(key);
            cached_keys_list.remove(key);
        }
        Repository.remove(key);
        setFilter(last_filter);
        total_size= Repository.get_all_keys_count();
    }

    public Elem getByID(IdType key)
    {
        if(!cache.containsKey(key))
            bring_from_DB(key);
        return cache.get(key);
    }

    /**
     * @return returns the total number of elements in the DB
     */
    public int get_all_keys_count()
    {
        return total_size;
    }

    /**
     * @return A list of all the elements in the DB
     */
    public List<Elem> getAll() { return Repository.getAll();}

    /**
     * @return A list of all the keys in the DB
     */
    public List<IdType> get_All_keys()    {return Repository.get_All_keys();}

    /**
     * @param filter List of string parameters. Each SQLRepository needs its own configuration of parameters.
     */
    public void setFilter(String... filter)
    {
        last_filter=filter;
        Repository.setFilter(filter);
        keys= Repository.get_All_filtered_keys();
    }

    @Override
    public List<Elem> getByID(List<IdType> keys) {
        return Repository.getByID(keys);
    }

    @Override
    public List<IdType> get_All_filtered_keys()
    {
        return keys;
    }
}
