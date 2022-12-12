package pro.sky.teamproject.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "ownership_cats")
public class OwnershipCats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private UserCat userCat;
    @OneToOne
    @JoinColumn(name = "animal_id")
    private AnimalCat animalCat;
    private int probationDays;
    private LocalDate endDateProbation;
    private String passageProbation;

    public OwnershipCats() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserCat getUserCat() {
        return userCat;
    }

    public void setUserCat(UserCat userCat) {
        this.userCat = userCat;
    }

    public AnimalCat getAnimalCat() {
        return animalCat;
    }

    public void setAnimalCat(AnimalCat animalCat) {
        this.animalCat = animalCat;
    }

    public LocalDate getEndDateProbation() {
        return endDateProbation;
    }

    public void setEndDateProbation(LocalDate endDateProbation) {
        this.endDateProbation = endDateProbation;
    }

    public int getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(int probationDays) {
        this.probationDays = probationDays;
    }

    public String getPassageProbation() {
        return passageProbation;
    }

    public void setPassageProbation(String passageProbation) {
        this.passageProbation = passageProbation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwnershipCats that = (OwnershipCats) o;
        return id == that.id &&
                probationDays == that.probationDays &&
                Objects.equals(userCat, that.userCat) &&
                Objects.equals(animalCat, that.animalCat) &&
                Objects.equals(endDateProbation, that.endDateProbation) &&
                Objects.equals(passageProbation, that.passageProbation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);
    }

    @Override
    public String toString() {
        return "OwnershipCats{" +
                "id=" + id +
                ", userCat=" + userCat +
                ", animalCat=" + animalCat +
                ", probationDays=" + probationDays +
                ", endDateProbation=" + endDateProbation +
                ", passageProbation='" + passageProbation + '\'' +
                '}';
    }
}
