package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.AnimalDog;
import pro.sky.teamproject.repository.AnimalDogRepository;

import java.util.Collection;

@Service
public class DogService {

    private final Logger logger = LoggerFactory.getLogger(DogService.class);

    private final AnimalDogRepository animalDogRepository;

    public DogService(AnimalDogRepository animalDogRepository) {
        this.animalDogRepository = animalDogRepository;
    }

    /**
     * Запись данных о животном в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param animalDog данные о животном в формате JSON
     * @return созданная запись в базе данных
     */
    public AnimalDog addAnimal(AnimalDog animalDog) {
        logger.debug("Was invoked method for create animalDog");
        return animalDogRepository.save(animalDog);
    }

    /**
     * Поиск животного в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id идентификатор искомого животного, не может быть null
     * @return данные о животном в формате JSON
     */
    public AnimalDog findAnimalById(long id) {
        logger.debug("Was invoked method for get animalDog info with id = {} ", id);
        AnimalDog animalDog = animalDogRepository.findById(id).orElse(null);
        logger.warn("Response {} ", animalDog);
        return animalDog;
    }

    /**
     * Внесение изменений в данные о животном в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param animalDog данные о животном в формате JSON
     * @return измененная запись в базе данных
     */
    public AnimalDog editAnimal(AnimalDog animalDog) {
        logger.debug("Was invoked method for edith animalDog");
        return animalDogRepository.save(animalDog);
    }

    /**
     * Удаление записи о животном из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор животного, не может быть null
     */
    public void deleteAnimal(long id) {
        logger.debug("Was invoked method for delete animalDog");
        animalDogRepository.deleteById(id);
    }

    /**
     * Поиск животного в БД по имени.
     * Используется метод репозитория {@link AnimalDogRepository#findByNameIgnoreCase(String)}
     * @param name имя искомого животного
     * @return список животных (объектов animalDog) с искомым именем, в фомате JSON
     */
    public Collection<AnimalDog> findAnimalByName(String name) {
        logger.debug("Was invoked method for find by name");
        return animalDogRepository.findByNameIgnoreCase(name);
    }

    /**
     * Поиск животного в БД по породе.
     * Используется метод репозитория {@link AnimalDogRepository#findByBreedIgnoreCase(String)}
     * @param breed порода искомого животного
     * @return список животных (объектов animalDog) искомой породы, в фомате JSON
     */
    public Collection<AnimalDog> findAnimalByBreed(String breed) {
        logger.debug("Was invoked method for find by breed");
        return animalDogRepository.findByBreedIgnoreCase(breed);
    }

    /**
     * Поиск животного в БД по возрасту.
     * Используется метод репозитория {@link AnimalDogRepository#findByAge(Integer)}
     * @param age возраст искомого животного
     * @return список животных (объектов animalDog) искомого возраста, в фомате JSON
     */
    public Collection<AnimalDog> findAnimalByAge(Integer age) {
        logger.debug("Was invoked method for find by age");
        return animalDogRepository.findByAge(age);
    }

    /**
     * Показывает все записи о животных в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех имеющихся животных (объектов animalDog) из БД, в фомате JSON
     */
    public Collection<AnimalDog> findAllAnimal() {
        logger.debug("Was invoked method for find all");
        return animalDogRepository.findAll();
    }
}