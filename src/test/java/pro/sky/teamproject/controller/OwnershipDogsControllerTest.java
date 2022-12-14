package pro.sky.teamproject.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamproject.entity.AnimalDog;
import pro.sky.teamproject.entity.OwnershipDogs;
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.listener.TelegramBotUpdatesListener;
import pro.sky.teamproject.repository.OwnershipDogsRepository;
import pro.sky.teamproject.services.DogService;
import pro.sky.teamproject.services.OwnershipDogsService;
import pro.sky.teamproject.services.UserDogService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OwnershipDogsController.class)
public class OwnershipDogsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnershipDogsRepository ownershipDogsRepository;

    @MockBean
    private UserDogService userDogService;

    @MockBean
    private DogService dogService;

    @MockBean
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    @SpyBean
    private OwnershipDogsService ownershipDogsService;

    @InjectMocks
    private OwnershipDogsController ownershipDogsController;

//    статические методы для создания UserDog, AnimalDog, OwnershipDogs

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
    public void createOwnershipDogPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

        when(userDogService.findUserDogById(any(Long.class)))
                .thenReturn(userDog);
        when(dogService.findAnimalById(any(Long.class)))
                .thenReturn(animalDog);
        when(ownershipDogsRepository.save(any(OwnershipDogs.class)))
                .thenReturn(ownershipDogs);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ownershipDogs?userDogId=" + idUserDog + "&animalDogId=" + idAnimalDog + "&probationDays=" + probationDays)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userDog").value(userDog))
                .andExpect(jsonPath("$.animalDog").value(animalDog))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void createOwnershipDogBadRequestTest() throws Exception {

//        UserDog
        final Long idUserDog = 1L;
//        AnimalDog
        final long idAnimalDog = 1L;
//        OWNERSHIP_CAT
        final int probationDays = -1;

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ownershipDogs?userDogId=" + idUserDog + "&animalDogId=" + idAnimalDog + "&probationDays=" + probationDays))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createOwnershipDogNegativeTests() throws Exception {

//        UserDog
        final Long idUserDog = 1L;
//        AnimalDog
        final long idAnimalDog = 1L;
//        OWNERSHIP_CAT
        final int probationDays = 30;

        when(userDogService.findUserDogById(any(Long.class)))
                .thenReturn(null);
        when(dogService.findAnimalById(any(Long.class)))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ownershipDogs?userDogId=" + idUserDog + "&animalDogId=" + idAnimalDog + "&probationDays=" + probationDays))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOwnershipDogInfoPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipDogs));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userDog").value(userDog))
                .andExpect(jsonPath("$.animalDog").value(animalDog))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void getOwnershipDogInfoBadRequestTest() throws Exception {

        final Long id = 0L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOwnershipDogInfoNegativeTest() throws Exception {

        final Long id = 1L;

        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateOwnershipDogPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

        when(userDogService.findUserDogById(any(Long.class)))
                .thenReturn(userDog);
        when(dogService.findAnimalById(any(Long.class)))
                .thenReturn(animalDog);
        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipDogs));
        when(ownershipDogsRepository.save(any(OwnershipDogs.class)))
                .thenReturn(ownershipDogs);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs?id=" + id + "&userDogId=" + idUserDog + "&animalDogId=" + idAnimalDog)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userDog").value(userDog))
                .andExpect(jsonPath("$.animalDog").value(animalDog))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void updateOwnershipDogBadRequestTest() throws Exception {

//        UserDog
        final Long idUserDog = 0L;
//        AnimalDog
        final long idAnimalDog = 1L;
//        OWNERSHIP_CAT
        final long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs?id=" + id + "&userDogId=" + idUserDog + "&animalDogId=" + idAnimalDog))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateOwnershipDogNegativeTest() throws Exception {

//        UserDog
        final Long idUserDog = 1L;
//        AnimalDog
        final long idAnimalDog = 1L;
//        OWNERSHIP_CAT
        final long id = 1L;

        when(userDogService.findUserDogById(any(Long.class)))
                .thenReturn(null);
        when(dogService.findAnimalById(any(Long.class)))
                .thenReturn(null);
        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs?id=" + id + "&userDogId=" + idUserDog + "&animalDogId=" + idAnimalDog))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProbationDaysOwnershipDogPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT before with edit probation days
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now().plusDays(probationDays);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_CAT after with edit probation days
        OwnershipDogs ownershipDogsEdit = ownershipDogs;
        final int probationDaysEdit = 14;
        final LocalDate endDateProbationEdit = LocalDate.now().plusDays(probationDaysEdit);
        ownershipDogsEdit.setProbationDays(probationDaysEdit);

        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipDogs));
        when(ownershipDogsRepository.save(any(OwnershipDogs.class)))
                .thenReturn(ownershipDogsEdit);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs/probation-days?id=" + id + "&probationDays=" + probationDaysEdit)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userDog").value(userDog))
                .andExpect(jsonPath("$.animalDog").value(animalDog))
                .andExpect(jsonPath("$.probationDays").value(probationDaysEdit))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbationEdit.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void updateProbationDaysOwnershipDogNegativeTest() throws Exception {

        final long id = 1L;
        final int probationDays = 30;

        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs/probation-days?id=" + id + "&userDogId=" + id + "&probationDays=" + probationDays))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProbationDaysOwnershipDogBadRequestTest() throws Exception {

        final Long id = 0L;
        final int probationDays = -1;

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs/probation-days?id=" + id + "&userDogId=" + id + "&probationDays=" + probationDays))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteOwnershipDogPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipDogs));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ownershipDogs/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteOwnershipDogsNegativeTest() throws Exception {

        final long id = 10L;

        when(ownershipDogsRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ownershipDogs/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOwnershipDogsBadRequestTest() throws Exception {

        final Long id = 0L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ownershipDogs/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePassageProbationOwnershipDogPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_CAT after with edit passage probation
        OwnershipDogs ownershipDogsEdit = ownershipDogs;
        String passageProbationEdit = "пройден";
        ownershipDogs.setPassageProbation(passageProbationEdit);

        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipDogs));
        when(ownershipDogsRepository.save(any(OwnershipDogs.class)))
                .thenReturn(ownershipDogsEdit);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs/passage-probation?id=" + id + "&passageProbation=" + passageProbationEdit)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userDog").value(userDog))
                .andExpect(jsonPath("$.animalDog").value(animalDog))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbationEdit));
    }

    @Test
    public void updatePassageProbationOwnershipDogBadRequestTest() throws Exception {

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

//        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_CAT after with edit passage probation
        OwnershipDogs ownershipDogsEdit = ownershipDogs;
        String passageProbationEdit = "BadRequest";
        ownershipDogs.setPassageProbation(passageProbationEdit);

        when(ownershipDogsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipDogs));
        when(ownershipDogsRepository.save(any(OwnershipDogs.class)))
                .thenReturn(ownershipDogsEdit);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs/passage-probation?id=" + id + "&passageProbation=" + passageProbationEdit))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePassageProbationOwnershipDogNegativeTest() throws Exception {
        final long idNotFound = 100L;
        final String passageProbation = "не окончен";

        when(ownershipDogsRepository.findById(ArgumentMatchers.eq(idNotFound)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipDogs/passage-probation?id=" + idNotFound + "&passageProbation=" + passageProbation))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOwnershipDogsByEndDateProbationPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

        String endDateProbationForTest = "2022-12-31";

        when(ownershipDogsRepository.findOwnershipDogsByEndDateProbation(ArgumentMatchers.eq(endDateProbation)))
                .thenReturn(Collections.singletonList(ownershipDogs));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs?endDateProbation=" + endDateProbationForTest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].userDog").value(userDog))
                .andExpect(jsonPath("$[0].animalDog").value(animalDog))
                .andExpect(jsonPath("$[0].probationDays").value(probationDays))
                .andExpect(jsonPath("$[0].endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$[0].passageProbation").value(passageProbation));
    }

    @Test
    public void findOwnershipDogsByEndDateProbationBadRequestTest() throws Exception {

        String endDateProbationForTest = "2022 12 31";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs?endDateProbation=" + endDateProbationForTest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findOwnershipDogsByPassageProbationPositiveTest() throws Exception {

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

        //        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

        String passageProbationForTest = "не окончен";

        when(ownershipDogsRepository.findOwnershipDogsByPassageProbationIgnoreCase(ArgumentMatchers.eq(passageProbation)))
                .thenReturn(Collections.singletonList(ownershipDogs));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs?passageProbation=" + passageProbationForTest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].userDog").value(userDog))
                .andExpect(jsonPath("$[0].animalDog").value(animalDog))
                .andExpect(jsonPath("$[0].probationDays").value(probationDays))
                .andExpect(jsonPath("$[0].endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$[0].passageProbation").value(passageProbation));
    }

    @Test
    public void findOwnershipDogsByPassageProbationBadRequestTest() throws Exception {

        String passageProbationForTest = "BadRequest";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs?passageProbation=" + passageProbationForTest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findOwnershipDogsAllOwnershipDogsPositiveTest() throws Exception {

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

        //        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserDog userDog = createNewUserDogForTests(idUserDog, fullName, phone, userId);
        final AnimalDog animalDog = createNewDogForTests(idAnimalDog, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipDogs ownershipDogs = createNewOwnershipDogForTests(id, userDog, animalDog, probationDays, endDateProbation, passageProbation);

        when(ownershipDogsRepository.findAll())
                .thenReturn(Collections.singletonList(ownershipDogs));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipDogs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].userDog").value(userDog))
                .andExpect(jsonPath("$[0].animalDog").value(animalDog))
                .andExpect(jsonPath("$[0].probationDays").value(probationDays))
                .andExpect(jsonPath("$[0].endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$[0].passageProbation").value(passageProbation));
    }
}