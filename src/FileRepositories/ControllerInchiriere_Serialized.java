package FileRepositories;

import Domain.*;
import MemoryRepositories.CrudRepository;
import Validator.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 10.01.2017.
 */
public class ControllerInchiriere_Serialized extends AbstractCrudSerializedRepository<inchiriere,Integer> {

    private CrudRepository<film,Integer> repo_f;
    private CrudRepository<client,Integer> repo_c;
    private String[] last_filter;

    public ControllerInchiriere_Serialized(Validator<inchiriere> v,
                                           String path,
                                           CrudRepository<film,Integer> _repo_f,
                                           CrudRepository<client,Integer> _repo_c) throws IOException, ExceptieValidareInRepository
    {
        super(v,path);
        repo_f=_repo_f;
        repo_c=_repo_c;
        read_from_file();
        last_filter= new String [] {"",""};
    }

    /**
     * @param filtru #1: nume client, #2: titlu film
     * @return
     */
    @Override
    public void setFilter(String... filtru) {
        last_filter=filtru;
    }


    @Override
    public List<Integer> get_All_filtered_keys() {
        ArrayList<Integer> result=new ArrayList<>();
        film f;
        client c;
        for(inchiriere i:getAll())
        {
            f=repo_f.getByID(i.getCod_f());
            c=repo_c.getByID(i.getCod_c());
            if(f!=null && c!=null)
            {
                if(f.getTitlu().contains(last_filter[1]) && c.getNume().contains(last_filter[0]))
                    result.add(i.getId());
            }
            else
                    try {
                        remove(i.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        }
        return result;
    }
}
