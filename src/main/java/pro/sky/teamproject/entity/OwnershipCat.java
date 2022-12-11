package pro.sky.teamproject.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ownership_cats")
public class OwnershipCat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserCat owner;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalCat animal;

    public AnimalCat getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalCat animal) {
        this.animal = animal;
    }

    public UserCat getOwner() {
        return owner;
    }

    public void setOwner(UserCat owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}