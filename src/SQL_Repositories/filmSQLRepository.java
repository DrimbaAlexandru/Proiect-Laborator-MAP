package SQL_Repositories;

import Domain.film;
import MemoryRepositories.CrudRepository;
import Validator.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Alex on 02.01.2017.
 */

public class filmSQLRepository implements CrudRepository<film,Integer>,I_SQLRepository
{
    Validator<film> validator;
    private Connection con;
    private int size;
    private String[] last_filtre = new String[] {"","",""};

    public filmSQLRepository(Validator<film> _validator, Connection _con) throws IOException
    {
        validator=_validator;
        con=_con;
        size=get_All_keys().size();
    }

    public film add (film e) throws IOException, ExceptieValidareInRepository
    {
        try {
            validator.Validate(e);
        } catch (ExceptieValidator exceptieValidator) {
            throw new ExceptieValidareInRepository(exceptieValidator.getMessage());
        }

        PreparedStatement stmt = null;
        int generatedID=-1;

        try {
            String query="declare @result int;\n" +
                    "declare @result_table table(nr int);\n" +
                    "execute insert_film @result output, ?, ?, ?;\n" +
                    "insert into @result_table values(@result);\n" +
                    "Select nr from @result_table;";
            boolean hasMoreResultSets;

            stmt=con.prepareStatement(query);
            stmt.setString(1,e.getTitlu());
            stmt.setInt(2,e.getAn_aparitie());
            stmt.setString(3,e.getRegizor());

            hasMoreResultSets=stmt.execute();
            con.commit();
            while ( hasMoreResultSets || stmt.getUpdateCount() != -1 )
            {
                if ( hasMoreResultSets )
                {
                    ResultSet rs = stmt.getResultSet();
                    if(rs!=null)
                        while (rs.next())
                            generatedID = rs.getInt("nr");
                }
                else
                if ( stmt.getUpdateCount() == -1 )
                    break;
                hasMoreResultSets = stmt.getMoreResults();
            }
        }
        catch (SQLException e1) {
            if(e1.getMessage().contains("UNIQUE KEY"))
                throw new ExceptieValidareInRepository("Elementul exista deja!");
            else
                e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        size=get_All_keys().size();
        return new film(e.getTitlu(),e.getAn_aparitie(),generatedID,e.getRegizor());
    }

    public void replace(film e) throws IOException, ExceptieValidareInRepository
    {
        try {
            validator.Validate(e);
        } catch (ExceptieValidator exceptieValidator) {
            throw new ExceptieValidareInRepository(exceptieValidator.getMessage());
        }

        PreparedStatement stmt = null;
        try {
            String query="execute update_film ?,?,?,?";

            stmt=con.prepareStatement(query);
            stmt.setInt(1,e.getId());
            stmt.setString(2,e.getTitlu());
            stmt.setInt(3,e.getAn_aparitie());
            stmt.setString(4,e.getRegizor());

            stmt.execute();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
            throw new IOException(e1.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        size=get_All_keys().size();
    }

    public void remove(Integer key) throws IOException
    {
        PreparedStatement stmt = null;
        try {
            String query="execute delete_film ?";

            stmt=con.prepareStatement(query);
            stmt.setInt(1,key);

            stmt.execute();
            con.commit();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
            throw new IOException(e1.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        size=get_All_keys().size();
    }

    public film getByID(Integer key)
    {
        film f=null;
        PreparedStatement stmt = null;
        try {
            String query="select * from [dbo].get_film (?)";

            stmt=con.prepareStatement(query);
            stmt.setInt(1,key);
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                f = new film(rs.getString("titlu"), rs.getInt("an_aparitie"),rs.getInt("ID_film"),rs.getString("regizor"));
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return f;
    }



    /**
     * @param filter string #1: titlu, #2: an, #3: regizor
     * @return
     */
    @Override
    public void setFilter(String... filter) {
        last_filtre=filter;
    }

    public List<Integer> get_All_filtered_keys()
    {
        PreparedStatement stmt = null;
        List<Integer> result=new ArrayList<>();
        try {
            String query="select * from [dbo].get_filtered_keys_filme (?,?,?)";

            stmt=con.prepareStatement(query);
            stmt.setString(1,last_filtre[0]);
            stmt.setString(2,last_filtre[1]);
            stmt.setString(3,last_filtre[2]);
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                result.add(rs.getInt("ID_film"));
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public int get_all_keys_count()
    {
        return size;
    }

    public List<Integer> get_All_keys()
    {
        PreparedStatement stmt = null;
        List<Integer> result=new ArrayList<>();
        try {
            String query="select * from [dbo].get_filtered_keys_filme (?,?,?)";

            stmt=con.prepareStatement(query);
            stmt.setString(1,"");
            stmt.setString(2,"");
            stmt.setString(3,"");
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                result.add(rs.getInt("ID_film"));
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public List<film> getByID(List<Integer> keys)
    {
        ArrayList<film> l=new ArrayList<>();
        film f=null;
        PreparedStatement stmt = null;
        try {
            String query="select * from [dbo].get_film (?)";

            stmt=con.prepareStatement(query);
            for(int k:keys)
            {
                stmt.setInt(1,k);
                ResultSet rs=stmt.executeQuery();
                //con.commit();
                while (rs.next())
                    l.add(new film(rs.getString("titlu"), rs.getInt("an_aparitie"),rs.getInt("ID_film"),rs.getString("regizor")));
            }
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return l;
    }
}
