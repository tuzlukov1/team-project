package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.Animal;
import pro.sky.teamproject.repository.AnimalRepository;

import java.util.Collection;

@Service
public class AnimalService {

    private final Logger logger = LoggerFactory.getLogger(AnimalService.class);

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    /**
     * Запись данных о животном в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param animal данные о животном в формате JSON
     * @return созданная запись в базе данных
     */
    public Animal addAnimal(Animal animal) {
        logger.debug("Was invoked method for create animal");
        return animalRepository.save(animal);
    }

    /**
     * Поиск животного в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id идентификатор искомого животного, не может быть null
     * @return данные о животном в формате JSON
     */
    public Animal findAnimalById(long id) {
        logger.debug("Was invoked method for get animal info with id = {} ", id);
        Animal animal = animalRepository.findById(id).orElse(null);
        logger.warn("Response {} ", animal);
        return animal;
    }

    /**
     * Внесение изменений в данные о животном в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param animal данные о животном в формате JSON
     * @return измененная запись в базе данных
     */
    public Animal editAnimal(Animal animal) {
        logger.debug("Was invoked method for edith animal");
        return animalRepository.save(animal);
    }

    /**
     * Удаление записи о животном из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор животного, не может быть null
     */
    public void deleteAnimal(long id) {
        logger.debug("Was invoked method for delete animal");
        animalRepository.deleteById(id);
    }

    /**
     * Поиск животного в БД по имени.
     * Используется метод репозитория {@link AnimalRepository#findByNameIgnoreCase(String)}
     * @param name имя искомого животного
     * @return список животных (объектов animal) с искомым именем, в фомате JSON
     */
    public Collection<Animal> findAnimalByName(String name) {
        logger.debug("Was invoked method for find by name");
        return animalRepository.findByNameIgnoreCase(name);
    }

    /**
     * Поиск животного в БД по породе.
     * Используется метод репозитория {@link AnimalRepository#findByBreedIgnoreCase(String)}
     * @param breed порода искомого животного
     * @return список животных (объектов animal) искомой породы, в фомате JSON
     */
    public Collection<Animal> findAnimalByBreed(String breed) {
        logger.debug("Was invoked method for find by breed");
        return animalRepository.findByBreedIgnoreCase(breed);
    }

    /**
     * Поиск животного в БД по возрасту.
     * Используется метод репозитория {@link AnimalRepository#findByAge(Integer)}
     * @param age возраст искомого животного
     * @return список животных (объектов animal) искомого возраста, в фомате JSON
     */
    public Collection<Animal> findAnimalByAge(Integer age) {
        logger.debug("Was invoked method for find by age");
        return animalRepository.findByAge(age);
    }

    /**
     * Показывает все записи о животных в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех имеющихся животных (объектов animal) из БД, в фомате JSON
     */
    public Collection<Animal> findAllAnimal() {
        logger.debug("Was invoked method for find all");
        return animalRepository.findAll();
    }
}
