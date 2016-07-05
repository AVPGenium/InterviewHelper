package entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

/**
 * Created by mpmayorov on 04.07.2016.
 */
@DatabaseTable
public class Candidate {
    @DatabaseField(generatedId = true)
    private int idCandidate;

    @DatabaseField(canBeNull = false)
    private String fio;

    @Override
    public String toString() {
        return "Candidate{" +
                "idCandidate=" + idCandidate +
                ", fio='" + fio + '\'' +
                ", bornDate=" + bornDate +
                ", banned='" + banned + '\'' +
                '}';
    }

    @DatabaseField(canBeNull = false)
    private Date bornDate;

    @DatabaseField(canBeNull = false)
    private String banned;

    @ForeignCollectionField(foreignFieldName = "idCandidate", eager = true)
    private Collection<Interview> interviews;

    public Candidate() {
    }

    public int getIdCandidate() {
        return idCandidate;
    }

    public void setIdCandidate(int idCandidate) {
        this.idCandidate = idCandidate;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }

    public Collection<Interview> getInterviews() {
        return interviews;
    }

    public void setInterviews(Collection<Interview> interviews) {
        this.interviews = interviews;
    }
}
