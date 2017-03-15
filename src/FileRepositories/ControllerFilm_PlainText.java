package FileRepositories;

import Domain.film;
import Validator.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Alex on 10.01.2017.
 */
public class ControllerFilm_PlainText extends AbstractCrudPlainTextFileRepository<film,Integer> {
    private String[] last_filter= new String[] {"","",""};

    public ControllerFilm_PlainText(Validator<film> v, String path) throws IOException,ExceptieValidareInRepository
    {
        super(v,path,
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
                });
        read_from_file();
    }

    /**
     * @param filtru #1 String: titlu, #2: an, #3 regizor
     * @return
     */
    @Override
    public void setFilter(String... filtru) {
        last_filter=filtru;
    }

    @Override
    public List<Integer> get_All_filtered_keys() {
        ArrayList<Integer> result=new ArrayList<>();
        for(film f:getAll())
        {
            if(f.getTitlu().contains(last_filter[0]) &&
                    Integer.toString(f.getAn_aparitie()).contains(last_filter[1]) &&
                    f.getRegizor().contains(last_filter[2]))
                result.add(f.getId());
        }
        return result;
    }
}
