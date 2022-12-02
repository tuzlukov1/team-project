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
import pro.sky.teamproject.entity.AnimalDog;
import pro.sky.teamproject.repository.AnimalDogRepository;
import pro.sky.teamproject.services.DogService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DogController.class)
class DogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalDogRepository animalDogRepository;

    @SpyBean
    private DogService dogService;

    @InjectMocks
    private DogController dogController;

    @Test
    public void createAnimalTest() throws Exception {
        final long id = 1L;
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        JSONObject animalDogObject = new JSONObject();
        animalDogObject.put("id", id);
        animalDogObject.put("name", name);
        animalDogObject.put("breed", breed);
        animalDogObject.put("age", age);

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.save(any(AnimalDog.class)))
                .thenReturn(dog);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/dog")
                        .content(animalDogObject.toString())
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
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog/" + id)
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

        when(animalDogRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editAnimalTest() throws Exception {
        final long id = 1L;
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        JSONObject animalDogObject = new JSONObject();
        animalDogObject.put("id", id);
        animalDogObject.put("name", name);
        animalDogObject.put("breed", breed);
        animalDogObject.put("age", age);

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.save(any(AnimalDog.class)))
                .thenReturn(dog);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/dog")
                        .content(animalDogObject.toString())
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
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAnimalNegativeTest() throws Exception {
        final long id = 10L;

        when(animalDogRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAnimalFindByNameTest() throws Exception {
        final long id = 1L;
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.findByNameIgnoreCase(any(String.class)))
                .thenReturn(Collections.singletonList(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog?name=" + name)
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
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.findByBreedIgnoreCase(any(String.class)))
                .thenReturn(Collections.singletonList(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog?breed=" + breed)
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
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.findByAge(any(Integer.class)))
                .thenReturn(Collections.singletonList(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog?age=" + age)
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
        final String name = "Dog";
        final String breed = "outbred";
        final int age = 3;

        AnimalDog dog = new AnimalDog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        when(animalDogRepository.findAll())
                .thenReturn(Collections.singletonList(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].breed").value(breed))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void findAnimalFindAllAnimalNegativeTest() throws Exception {

        when(animalDogRepository.findAll())
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .equals(Collections.EMPTY_LIST);
    }
}