package pro.sky.teamproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamproject.entity.AnimalDog;
import pro.sky.teamproject.entity.OwnershipDogs;
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.repository.OwnershipDogsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnershipDogsServiceTest {

    @Mock
    private OwnershipDogsRepository ownershipDogsRepository;

    @InjectMocks
    private OwnershipDogsService ownershipDogsService;

    public static UserDog createNewUserDogForTests(long idUserDog, String fullName, long phone, long userId) {
        UserDog person = new UserDog();
        person.setId(idUserDog);
        person.setFullName(fullName);
        person.setPhone(phone);
        person.setUserId(userId);
        return person;
    }

    public static AnimalDog createNewDogForTests(long idAnimalDog, String name, String breed, int age) {
        AnimalDog dog = new AnimalDog();
        dog.setId(idAnimalDog);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);
        return dog;
    }

    public static OwnershipDogs createNewOwnershipDogForTests(long id,
                                                              UserDog userDog,
                                                              AnimalDog animalDog,
                                                              int probationDays,
                                                              LocalDate endDateProbation,
                                                              String passageProbation) {
        OwnershipDogs ownershipDogs = new OwnershipDogs();
        ownershipDogs.setId(id);
        ownershipDogs.setUserDog(userDog);
        ownershipDogs.setAnimalDog(animalDog);
        ownershipDogs.setProbationDays(probationDays);
        ownershipDogs.setEndDateProbation(endDateProbation);
        ownershipDogs.setPassageProbation(passageProbation);
        return ownershipDogs;
    }

    @Test
    public void updateProbationDaysWhenDateEqualTodayTest() {

//        UserDog
        final Long idUserDog = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;
        final Long userId = 1L;

//        AnimalDog
        final long idAnimalDog = 1L;
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

//        OWNERSHIP_DOG_BEFORE_EDIT
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 2;
        final LocalDate endDateProbation = LocalDate.now().plusDays(1);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog,
                probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_DOG_AFTER_EDIT
        final int probationDaysAfter = 1;
        final LocalDate endDateProbationAfter = LocalDate.now().plusDays(1);
        OwnershipDogs ownershipDogsAfter = createNewOwnershipDogForTests(id, userDog, animalDog,
                probationDaysAfter, endDateProbationAfter, passageProbation);

        List<OwnershipDogs> ownershipDogsList = new ArrayList<>(List.of(
                ownershipDogs
        ));
        List<OwnershipDogs> expected = new ArrayList<>(List.of(
                ownershipDogsAfter
        ));

        when(ownershipDogsRepository.findAll())
                .thenReturn(ownershipDogsList);
        assertThat(ownershipDogsService.updateProbationDays())
                .isEqualTo(expected);
    }

    @Test
    public void updateProbationDaysWhenDateNotEqualTodayAndStatusEqualNotFinishedTest() {

//        UserDog
        final Long idUserDog = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;
        final Long userId = 1L;

//        AnimalDog
        final long idAnimalDog = 1L;
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

//        OWNERSHIP_DOG
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 0;
        final LocalDate endDateProbation = LocalDate.now().minusDays(1);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog,
                probationDays, endDateProbation, passageProbation);

        List<OwnershipDogs> ownershipDogsList = new ArrayList<>(List.of(
                ownershipDogs
        ));
        List<OwnershipDogs> expected = new ArrayList<>(List.of(
                ownershipDogs
        ));

        when(ownershipDogsRepository.findAll())
                .thenReturn(ownershipDogsList);
        assertThat(ownershipDogsService.updateProbationDays())
                .isEqualTo(expected);
    }
}