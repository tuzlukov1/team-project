package pro.sky.teamproject.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "ownership_dogs")
public class OwnershipDogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private UserDog userDog;
    @OneToOne
    @JoinColumn(name = "animal_id")
    private AnimalDog animalDog;
    private int probationDays;
    private LocalDate endDateProbation;
    private String passageProbation;

    public OwnershipDogs() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDog getUserDog() {
        return userDog;
    }

    public void setUserDog(UserDog userDog) {
        this.userDog = userDog;
    }

    public AnimalDog getAnimalDog() {
        return animalDog;
    }

    public void setAnimalDog(AnimalDog animalDog) {
        this.animalDog = animalDog;
    }

    public int getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(int probationDays) {
        this.probationDays = probationDays;
    }

    public LocalDate getEndDateProbation() {
        return endDateProbation;
    }

    public void setEndDateProbation(LocalDate endDateProbation) {
        this.endDateProbation = endDateProbation;
    }

    public String getPassageProbation() {
        return passageProbation;
    }

    public void setPassageProbation(String passageProbation) {
        this.passageProbation = passageProbation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OwnershipDogs that = (OwnershipDogs) o;
        return id == that.id &&
                probationDays == that.probationDays &&
                Objects.equals(userDog, that.userDog) &&
                Objects.equals(animalDog, that.animalDog) &&
                Objects.equals(endDateProbation, that.endDateProbation) &&
                Objects.equals(passageProbation, that.passageProbation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);
    }

    @Override
    public String toString() {
        return "OwnershipDogs{" +
                "id=" + id +
                ", userDog=" + userDog +
                ", animalDog=" + animalDog +
                ", probationDays=" + probationDays +
                ", endDateProbation=" + endDateProbation +
                ", passageProbation='" + passageProbation + '\'' +
                '}';
    }
}
