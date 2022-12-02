package pro.sky.teamproject.controller;

import org.json.JSONObject;
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
import pro.sky.teamproject.repository.AnimalCatRepository;
import pro.sky.teamproject.services.CatService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatController.class)
class CatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalCatRepository animalCatRepository;

    @SpyBean
    private CatService catService;

    @InjectMocks
    private CatController catController;

    @Test
    public void createAnimalTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        JSONObject animalCatObject = new JSONObject();
        animalCatObject.put("id", id);
        animalCatObject.put("name", name);
        animalCatObject.put("breed", breed);
        animalCatObject.put("age", age);

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.save(any(AnimalCat.class)))
                .thenReturn(cat);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cat")
                        .content(animalCatObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void getAnimalInfoPositiveTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void getAnimalInfoNegativeTest() throws Exception {
        final long id = 10L;

        when(animalCatRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editAnimalTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        JSONObject animalCatObject = new JSONObject();
        animalCatObject.put("id", id);
        animalCatObject.put("name", name);
        animalCatObject.put("breed", breed);
        animalCatObject.put("age", age);

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.save(any(AnimalCat.class)))
                .thenReturn(cat);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cat")
                        .content(animalCatObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void deleteAnimalPositiveTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAnimalNegativeTest() throws Exception {
        final long id = 10L;

        when(animalCatRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAnimalFindByNameTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.findByNameIgnoreCase(any(String.class)))
                .thenReturn(Collections.singletonList(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat?name=" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].breed").value(breed))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void findAnimalFindByBreedTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.findByBreedIgnoreCase(any(String.class)))
                .thenReturn(Collections.singletonList(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat?breed=" + breed)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].breed").value(breed))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void findAnimalFindByAgeTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.findByAge(any(Integer.class)))
                .thenReturn(Collections.singletonList(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat?age=" + age)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].breed").value(breed))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void findAnimalFindAllAnimalPositiveTest() throws Exception {
        final long id = 1L;
        final String name = "Cat";
        final String breed = "outbred";
        final int age = 3;

        AnimalCat cat = new AnimalCat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setAge(age);

        when(animalCatRepository.findAll())
                .thenReturn(Collections.singletonList(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].breed").value(breed))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void findAnimalFindAllAnimalNegativeTest() throws Exception {

        when(animalCatRepository.findAll())
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .equals(Collections.EMPTY_LIST);
    }
}