package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.AnimalCat;
import pro.sky.teamproject.repository.AnimalCatRepository;

import java.util.Collection;

@Service
public class CatService {

    private final Logger logger = LoggerFactory.getLogger(CatService.class);

    private final AnimalCatRepository animalCatRepository;

    public CatService(AnimalCatRepository animalCatRepository) {
        this.animalCatRepository = animalCatRepository;
    }

    /**
     * Запись данных о животном в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param animalCat данные о животном в формате JSON
     * @return созданная запись в базе данных
     */
    public AnimalCat addAnimal(AnimalCat animalCat) {
        logger.debug("Was invoked method for create animalCat");
        return animalCatRepository.save(animalCat);
    }

    /**
     * Поиск животного в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id идентификатор искомого животного, не может быть null
     * @return данные о животном в формате JSON
     */
    public AnimalCat findAnimalById(long id) {
        logger.debug("Was invoked method for get animalCat info with id = {} ", id);
        AnimalCat animalCat = animalCatRepository.findById(id).orElse(null);
        logger.warn("Response {} ", animalCat);
        return animalCat;
    }

    /**
     * Внесение изменений в данные о животном в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param animalCat данные о животном в формате JSON
     * @return измененная запись в базе данных
     */
    public AnimalCat editAnimal(AnimalCat animalCat) {
        logger.debug("Was invoked method for edith animalCat");
        return animalCatRepository.save(animalCat);
    }

    /**
     * Удаление записи о животном из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор животного, не может быть null
     */
    public void deleteAnimal(long id) {
        logger.debug("Was invoked method for delete animalCat");
        animalCatRepository.deleteById(id);
    }

    /**
     * Поиск животного в БД по имени.
     * Используется метод репозитория {@link AnimalCatRepository#findByNameIgnoreCase(String)}
     * @param name имя искомого животного
     * @return список животных (объектов animal) с искомым именем, в фомате JSON
     */
    public Collection<AnimalCat> findAnimalByName(String name) {
        logger.debug("Was invoked method for find by name");
        return animalCatRepository.findByNameIgnoreCase(name);
    }

    /**
     * Поиск животного в БД по породе.
     * Используется метод репозитория {@link AnimalCatRepository#findByBreedIgnoreCase(String)}
     * @param breed порода искомого животного
     * @return список животных (объектов animal) искомой породы, в фомате JSON
     */
    public Collection<AnimalCat> findAnimalByBreed(String breed) {
        logger.debug("Was invoked method for find by breed");
        return animalCatRepository.findByBreedIgnoreCase(breed);
    }

    /**
     * Поиск животного в БД по возрасту.
     * Используется метод репозитория {@link AnimalCatRepository#findByAge(Integer)}
     * @param age возраст искомого животного
     * @return список животных (объектов animal) искомого возраста, в фомате JSON
     */
    public Collection<AnimalCat> findAnimalByAge(Integer age) {
        logger.debug("Was invoked method for find by age");
        return animalCatRepository.findByAge(age);
    }

    /**
     * Показывает все записи о животных в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех имеющихся животных (объектов animal) из БД, в фомате JSON
     */
    public Collection<AnimalCat> findAllAnimal() {
        logger.debug("Was invoked method for find all");
        return animalCatRepository.findAll();
    }
}