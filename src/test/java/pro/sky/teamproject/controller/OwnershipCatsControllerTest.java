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
import pro.sky.teamproject.entity.AnimalCat;
import pro.sky.teamproject.entity.OwnershipCats;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.repository.OwnershipCatsRepository;
import pro.sky.teamproject.services.CatService;
import pro.sky.teamproject.services.OwnershipCatsService;
import pro.sky.teamproject.services.UserCatService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OwnershipCatsController.class)
public class OwnershipCatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnershipCatsRepository ownershipCatsRepository;

    @MockBean
    private UserCatService userCatService;

    @MockBean
    private CatService catService;

    @SpyBean
    private OwnershipCatsService ownershipCatsService;

    @InjectMocks
    private OwnershipCatsController ownershipCatsController;

//    статические методы для создания UserCat, AnimalCat, OwnershipCats

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
    public void createOwnershipCatPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

        when(userCatService.findUserCatById(any(Long.class)))
                .thenReturn(userCat);
        when(catService.findAnimalById(any(Long.class)))
                .thenReturn(animalCat);
        when(ownershipCatsRepository.save(any(OwnershipCats.class)))
                .thenReturn(ownershipCats);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ownershipCats?userCatId=" + idUserCat + "&animalCatId=" + idAnimalCat + "&probationDays=" + probationDays)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userCat").value(userCat))
                .andExpect(jsonPath("$.animalCat").value(animalCat))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void createOwnershipCatBadRequestTest() throws Exception {

//        UserCat
        final Long idUserCat = 1L;
//        AnimalCat
        final long idAnimalCat = 1L;
//        OWNERSHIP_CAT
        final int probationDays = -1;

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ownershipCats?userCatId=" + idUserCat + "&animalCatId=" + idAnimalCat + "&probationDays=" + probationDays))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createOwnershipCatNegativeTests() throws Exception {

//        UserCat
        final Long idUserCat = 1L;
//        AnimalCat
        final long idAnimalCat = 1L;
//        OWNERSHIP_CAT
        final int probationDays = 30;

        when(userCatService.findUserCatById(any(Long.class)))
                .thenReturn(null);
        when(catService.findAnimalById(any(Long.class)))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ownershipCats?userCatId=" + idUserCat + "&animalCatId=" + idAnimalCat + "&probationDays=" + probationDays))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOwnershipCatInfoPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipCats));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userCat").value(userCat))
                .andExpect(jsonPath("$.animalCat").value(animalCat))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void getOwnershipCatInfoBadRequestTest() throws Exception {

        final Long id = 0L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOwnershipCatInfoNegativeTest() throws Exception {

        final Long id = 1L;

        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateOwnershipCatPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

        when(userCatService.findUserCatById(any(Long.class)))
                .thenReturn(userCat);
        when(catService.findAnimalById(any(Long.class)))
                .thenReturn(animalCat);
        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipCats));
        when(ownershipCatsRepository.save(any(OwnershipCats.class)))
                .thenReturn(ownershipCats);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats?id=" + id + "&userCatId=" + idUserCat + "&animalCatId=" + idAnimalCat)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userCat").value(userCat))
                .andExpect(jsonPath("$.animalCat").value(animalCat))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void updateOwnershipCatBadRequestTest() throws Exception {

//        UserCat
        final Long idUserCat = 0L;
//        AnimalCat
        final long idAnimalCat = 1L;
//        OWNERSHIP_CAT
        final long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats?id=" + id + "&userCatId=" + idUserCat + "&animalCatId=" + idAnimalCat))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateOwnershipCatNegativeTest() throws Exception {

//        UserCat
        final Long idUserCat = 1L;
//        AnimalCat
        final long idAnimalCat = 1L;
//        OWNERSHIP_CAT
        final long id = 1L;

        when(userCatService.findUserCatById(any(Long.class)))
                .thenReturn(null);
        when(catService.findAnimalById(any(Long.class)))
                .thenReturn(null);
        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats?id=" + id + "&userCatId=" + idUserCat + "&animalCatId=" + idAnimalCat))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProbationDaysOwnershipCatPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT before with edit probation days
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now().plusDays(probationDays);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_CAT after with edit probation days
        OwnershipCats ownershipCatsEdit = ownershipCats;
        final int probationDaysEdit = 14;
        final LocalDate endDateProbationEdit = LocalDate.now().plusDays(probationDaysEdit);
        ownershipCatsEdit.setProbationDays(probationDaysEdit);

        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipCats));
        when(ownershipCatsRepository.save(any(OwnershipCats.class)))
                .thenReturn(ownershipCatsEdit);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats/probation-days?id=" + id + "&probationDays=" + probationDaysEdit)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userCat").value(userCat))
                .andExpect(jsonPath("$.animalCat").value(animalCat))
                .andExpect(jsonPath("$.probationDays").value(probationDaysEdit))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbationEdit.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbation));
    }

    @Test
    public void updateProbationDaysOwnershipCatNegativeTest() throws Exception {

        final long id = 1L;
        final int probationDays = 30;

        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats/probation-days?id=" + id + "&userCatId=" + id + "&probationDays=" + probationDays))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProbationDaysOwnershipCatBadRequestTest() throws Exception {

        final Long id = 0L;
        final int probationDays = -1;

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats/probation-days?id=" + id + "&userCatId=" + id + "&probationDays=" + probationDays))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteOwnershipCatPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipCats));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ownershipCats/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteOwnershipCatsNegativeTest() throws Exception {

        final long id = 10L;

        when(ownershipCatsRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ownershipCats/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOwnershipCatsBadRequestTest() throws Exception {

        final Long id = 0L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ownershipCats/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePassageProbationOwnershipCatPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_CAT after with edit passage probation
        OwnershipCats ownershipCatsEdit = ownershipCats;
        String passageProbationEdit = "пройден";
        ownershipCats.setPassageProbation(passageProbationEdit);

        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipCats));
        when(ownershipCatsRepository.save(any(OwnershipCats.class)))
                .thenReturn(ownershipCatsEdit);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats/passage-probation?id=" + id + "&passageProbation=" + passageProbationEdit)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userCat").value(userCat))
                .andExpect(jsonPath("$.animalCat").value(animalCat))
                .andExpect(jsonPath("$.probationDays").value(probationDays))
                .andExpect(jsonPath("$.endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$.passageProbation").value(passageProbationEdit));
    }

    @Test
    public void updatePassageProbationOwnershipCatBadRequestTest() throws Exception {

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

//        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

//        OWNERSHIP_CAT after with edit passage probation
        OwnershipCats ownershipCatsEdit = ownershipCats;
        String passageProbationEdit = "BadRequest";
        ownershipCats.setPassageProbation(passageProbationEdit);

        when(ownershipCatsRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(ownershipCats));
        when(ownershipCatsRepository.save(any(OwnershipCats.class)))
                .thenReturn(ownershipCatsEdit);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats/passage-probation?id=" + id + "&passageProbation=" + passageProbationEdit))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePassageProbationOwnershipCatNegativeTest() throws Exception {
        final long idNotFound = 100L;
        final String passageProbation = "не окончен";

        when(ownershipCatsRepository.findById(ArgumentMatchers.eq(idNotFound)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/ownershipCats/passage-probation?id=" + idNotFound + "&passageProbation=" + passageProbation))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOwnershipCatsByEndDateProbationPositiveTest() throws Exception {

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

//        OWNERSHIP_CAT
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.of(2022, 12, 31);
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

        String endDateProbationForTest = "2022-12-31";

        when(ownershipCatsRepository.findOwnershipCatsByEndDateProbation(ArgumentMatchers.eq(endDateProbation)))
                .thenReturn(Collections.singletonList(ownershipCats));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats?endDateProbation=" + endDateProbationForTest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].userCat").value(userCat))
                .andExpect(jsonPath("$[0].animalCat").value(animalCat))
                .andExpect(jsonPath("$[0].probationDays").value(probationDays))
                .andExpect(jsonPath("$[0].endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$[0].passageProbation").value(passageProbation));
    }

    @Test
    public void findOwnershipCatsByEndDateProbationBadRequestTest() throws Exception {

        String endDateProbationForTest = "2022 12 31";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats?endDateProbation=" + endDateProbationForTest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findOwnershipCatsByPassageProbationPositiveTest() throws Exception {

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

        //        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

        String passageProbationForTest = "не окончен";

        when(ownershipCatsRepository.findOwnershipCatsByPassageProbationIgnoreCase(ArgumentMatchers.eq(passageProbation)))
                .thenReturn(Collections.singletonList(ownershipCats));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats?passageProbation=" + passageProbationForTest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].userCat").value(userCat))
                .andExpect(jsonPath("$[0].animalCat").value(animalCat))
                .andExpect(jsonPath("$[0].probationDays").value(probationDays))
                .andExpect(jsonPath("$[0].endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$[0].passageProbation").value(passageProbation));
    }

    @Test
    public void findOwnershipCatsByPassageProbationBadRequestTest() throws Exception {

        String passageProbationForTest = "BadRequest";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats?passageProbation=" + passageProbationForTest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findOwnershipCatsAllOwnershipCatsPositiveTest() throws Exception {

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

        //        OWNERSHIP_CAT before with edit passage probation
        final long id = 1L;
        final UserCat userCat = createNewUserCatForTests(idUserCat, fullName, phone, userId);
        final AnimalCat animalCat = createNewCatForTests(idAnimalCat, name, breed, age);
        final int probationDays = 30;
        final LocalDate endDateProbation = LocalDate.now();
        final String passageProbation = "не окончен";
        OwnershipCats ownershipCats = createNewOwnershipCatForTests(id, userCat, animalCat, probationDays, endDateProbation, passageProbation);

        when(ownershipCatsRepository.findAll())
                .thenReturn(Collections.singletonList(ownershipCats));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ownershipCats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].userCat").value(userCat))
                .andExpect(jsonPath("$[0].animalCat").value(animalCat))
                .andExpect(jsonPath("$[0].probationDays").value(probationDays))
                .andExpect(jsonPath("$[0].endDateProbation").value(endDateProbation.toString()))
                .andExpect(jsonPath("$[0].passageProbation").value(passageProbation));
    }
}