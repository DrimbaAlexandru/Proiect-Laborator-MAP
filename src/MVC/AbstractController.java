/*
package MVC;

import Domain.HasID;
import Domain.film;
import MemoryRepositories.CachedAbstractRepository;
import MemoryRepositories.CrudRepository;
import SQL_Repositories.filmSQLRepository;
import Validator.ValidatorFilm;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;




public abstract class AbstractController<Elem extends HasID<IdType>,IdType>
{
    protected CrudRepository<Elem,IdType> repo;

    AbstractController(CrudRepository Repository) { repo=Repository; }

    public Elem getByID(IdType key) { return repo.getElem(key); }

    public void add(Elem e) throws IOException,ExceptieValidareInRepository { repo.add(e); }

    public void replace(Elem e) throws IOException,ExceptieValidareInRepository { repo.replace(e); }

    public Elem remove(IdType key) throws IOException { Elem e=repo.remove(key); return e; }

    public int getSize() { return repo.getSize(); }

    public List<Elem> getAll() { return repo.getAll(); }

    public List<IdType> getAllKeys() {return repo.getAllKeys();}
}*/


