package ma.projet.server;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebService(endpointInterface = "ma.projet.server.CompteService")
public class CompteServiceImpl implements CompteService {

    private final List<CompteDto> comptes = new ArrayList<>();

    public CompteServiceImpl() {
        comptes.add(new CompteDto(1L, 100.0, new Date(), "COURANT"));
        comptes.add(new CompteDto(2L, 250.5, new Date(), "EPARGNE"));
    }

    @Override
    public List<CompteDto> getComptes() {
        return new ArrayList<>(comptes);
    }

    @Override
    public boolean createCompte(double solde, String type) {
        long id = comptes.stream().mapToLong(c -> c.id != null ? c.id : 0L).max().orElse(0L) + 1;
        comptes.add(new CompteDto(id, solde, new Date(), type));
        return true;
    }

    @Override
    public boolean deleteCompte(long id) {
        return comptes.removeIf(c -> c.id != null && c.id == id);
    }
}

