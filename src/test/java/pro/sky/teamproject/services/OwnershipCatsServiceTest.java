package pro.sky.teamproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamproject.entity.AnimalCat;
import pro.sky.teamproject.entity.OwnershipCats;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.repository.OwnershipCatsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnershipCatsServiceTest {

    @Mock
    private OwnershipCatsRepository ownershipCatsRepository;

    @InjectMocks
    private OwnershipCatsService ownershipCatsService;

    public static UserCat createNewUserCatForTests(long idUserCat, String fullName, long phone, long userId) {
        UserCat person = new UserCat();
        person.setId(idUserCat);
        person.setFullName(fullName);
        person.setPhone(phone);
        person.setUserId(userId);
        return person;
    }

    public static AnimalCat createNewCatForTests(long idAnimalCat, String name, String breed, int age) {
        AnimalCat cat = new AnimalCat();
        cat.setId(idAnimalCat);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);
        return cat;
    }

    public static OwnershipCats createNewOwnershipCatForTests(long id,
                                                              UserCat userCat,
                                                              AnimalCat animalCat,
                                                              int probationDays,
                                                              LocalDate endDateProbation,
                                                              String passageProbation) {
        OwnershipCats ownershipCats = new OwnershipCats();
        ownershipCats.setId(id);
        ownershipCats.setUserCat(userCat);
        ownershipCats.setAnimalCat(animalCat);
        ownershipCats.setProbationDays(probationDays);
        ownershipCats.setEndDateProbation(endDateProbation);
        ownershipCats.setPassageProbation(passageProbation);
        return ownershipCats;
    }

    @Test
    public void updateProbationDaysWhenDateEqualTodayTest() {

//        UserCat
        final Long idUserCat = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;
        final Long userId = 1L;

//        AnimalCat
        final long idAnimalCat = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

//        OWNERSHIP_DOG_BEFORE_EDIT
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 2;
        final LocalDate endDateProbation = LocalDate.now().plusDays(1);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat,
                probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_DOG_AFTER_EDIT
        final int probationDaysAfter = 1;
        final LocalDate endDateProbationAfter = LocalDate.now().plusDays(1);
        OwnershipCats ownershipCatsAfter = createNewOwnershipCatForTests(id, userCat, animalCat,
                probationDaysAfter, endDateProbationAfter, passageProbation);

        List<OwnershipCats> ownershipCatsList = new ArrayList<>(List.of(
                ownershipCats
        ));
        List<OwnershipCats> expected = new ArrayList<>(List.of(
                ownershipCatsAfter
        ));

        when(ownershipCatsRepository.findAll())
                .thenReturn(ownershipCatsList);
        assertThat(ownershipCatsService.updateProbationDays())
                .isEqualTo(expected);
    }

    @Test
    public void updateProbationDaysWhenDateNotEqualTodayAndStatusEqualNotFinishedTest() {

//        UserCat
        final Long idUserCat = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;
        final Long userId = 1L;

//        AnimalCat
        final long idAnimalCat = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

//        OWNERSHIP_DOG
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 0;
        final LocalDate endDateProbation = LocalDate.now().minusDays(1);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat,
                probationDays, endDateProbation, passageProbation);

        List<OwnershipCats> ownershipCatsList = new ArrayList<>(List.of(
                ownershipCats
        ));
        List<OwnershipCats> expected = new ArrayList<>(List.of(
                ownershipCats
        ));

        when(ownershipCatsRepository.findAll())
                .thenReturn(ownershipCatsList);
        assertThat(ownershipCatsService.updateProbationDays())
                .isEqualTo(expected);
    }
}