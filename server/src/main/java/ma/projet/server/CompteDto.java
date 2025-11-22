package ma.projet.server;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompteDto {
    public Long id;
    public double solde;
    public Date dateCreation;
    public String type;

    public CompteDto() {}

    public CompteDto(Long id, double solde, Date dateCreation, String type) {
        this.id = id;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
    }
}

