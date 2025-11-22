package ma.projet.server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface CompteService {
    @WebMethod
    List<CompteDto> getComptes();

    @WebMethod
    boolean createCompte(double solde, String type);

    @WebMethod
    boolean deleteCompte(long id);
}

