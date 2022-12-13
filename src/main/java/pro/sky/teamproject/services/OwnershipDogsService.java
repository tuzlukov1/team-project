package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.AnimalDog;
import pro.sky.teamproject.entity.OwnershipDogs;
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.listener.TelegramBotUpdatesListener;
import pro.sky.teamproject.repository.OwnershipDogsRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
public class OwnershipDogsService {

    static final String DEFAULT_PASSAGE_PROBATION = "не окончен";

    private final Logger logger = LoggerFactory.getLogger(OwnershipDogsService.class);

    private final OwnershipDogsRepository ownershipDogsRepository;
    private final UserDogService userDogService;
    private final DogService dogService;

    private final TelegramBotUpdatesListener telegramBotUpdatesListener;


    public OwnershipDogsService(OwnershipDogsRepository ownershipDogsRepository,
                                UserDogService userDogService,
                                DogService dogService,@Lazy TelegramBotUpdatesListener telegramBotUpdatesListener) {
        this.ownershipDogsRepository = ownershipDogsRepository;
        this.userDogService = userDogService;
        this.dogService = dogService;
        this.telegramBotUpdatesListener = telegramBotUpdatesListener;
    }

    /**
     * Запись в БД идентификаторов нового владельца, выбранного питомца и
     * колличество дней испытательного срока. Выполняются запросы к БД через
     * сервисы {@link UserDogService} и {@link DogService} в таблицы users_dog и animals_dog.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param userId        идентификатор пользователя из таблицы users_dog
     * @param animalId      идентификатор питомца из таблицы animals_dog
     * @param probationDays назначенное колличество дней испытательного срока
     * @return созданная новая запись в таблице ownership_dogs
     */
    public OwnershipDogs addOwnershipDog(long userId, long animalId, int probationDays) {
        logger.debug("Was invoked method for create ownershipDog");
        UserDog foundUserDog = userDogService.findUserDogById(userId);
        AnimalDog foundAnimalDog = dogService.findAnimalById(animalId);
        if (foundUserDog == null || foundAnimalDog == null) {
            return null;
        }
        LocalDate endProbation = LocalDate.now().plusDays(probationDays);

        OwnershipDogs ownershipDog = new OwnershipDogs();
        ownershipDog.setUserDog(foundUserDog);
        ownershipDog.setAnimalDog(foundAnimalDog);
        ownershipDog.setEndDateProbation(endProbation);
        ownershipDog.setProbationDays(probationDays);
        ownershipDog.setPassageProbation(DEFAULT_PASSAGE_PROBATION);

        return ownershipDogsRepository.save(ownershipDog);
    }

    /**
     * Поиск владельца и взятого им питомца в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     *
     * @param id идентификатор пары (владелец и питомец), не может быть null
     * @return данные о паре (владелец и питомец) в формате JSON
     */
    public OwnershipDogs findOwnershipDogById(Long id) {
        logger.debug("Was invoked method for get ownership dog info with id = {} ", id);
        OwnershipDogs foundOwnershipDog = ownershipDogsRepository.findById(id).orElse(null);
        logger.warn("Response {} ", foundOwnershipDog);
        return foundOwnershipDog;
    }

    /**
     * Редактирование записи в БД по идентификатору пары (владелец и питомец)
     * выполняется замена владельца и питомца в таблице ownership_dogs.
     * Выполняются запросы к БД через сервисы {@link UserDogService} и
     * {@link DogService} в таблицы users_dog и animals_dog.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param id       идентификатор пары (владелец и питомец), не может быть null
     * @param userId   идентификатор пользователя из таблицы users_dog
     * @param animalId идентификатор питомца из таблицы animals_dog
     * @return измененная запись в таблице ownership_dogs
     */
    public OwnershipDogs editOwnershipDog(long id, long userId, long animalId) {
        logger.debug("Was invoked method for edit ownershipDog");
        OwnershipDogs ownershipDog = findOwnershipDogById(id);
        UserDog foundUserDog = userDogService.findUserDogById(userId);
        AnimalDog foundAnimalDog = dogService.findAnimalById(animalId);
        if (ownershipDog == null || foundUserDog == null || foundAnimalDog == null) {
            return null;
        }
        ownershipDog.setUserDog(foundUserDog);
        ownershipDog.setAnimalDog(foundAnimalDog);
        return ownershipDogsRepository.save(ownershipDog);
    }

    /**
     * Редактирование записи в БД по идентификатору пары (владелец и питомец) выполняется изменение
     * полей с колличеством дней испытательного срока и датой его окончания.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param id            идентификатор пары (владелец и питомец), не может быть null
     * @param probationDays назначенное колличество дней испытательного срока
     * @return запись в таблице ownership_dogs с измененными полями probationDays и endDateProbation
     */
    public OwnershipDogs editProbationDays(long id, int probationDays) {
        logger.debug("Was invoked method for edit probationDays");
        OwnershipDogs ownershipDog = findOwnershipDogById(id);
        if (ownershipDog == null) {
            return null;
        }
        String probationText = "Дорогой усыновитель, ваш испытательный срок" +
                "\nбыл продлен на " + probationDays + "дней";
        LocalDate endProbation = LocalDate.now().plusDays(probationDays);
        ownershipDog.setProbationDays(probationDays);
        ownershipDog.setEndDateProbation(endProbation);
        telegramBotUpdatesListener.sendMessageToUser(id, probationText);
        return ownershipDogsRepository.save(ownershipDog);
    }

    /**
     * Удаление записи о паре (владелец и питомец) из таблицы ownership_dogs по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     *
     * @param id идентификатор пары (владелец и питомец), не может быть null
     */
    public void deleteOwnershipDog(long id) {
        logger.debug("Was invoked method for delete OwnershipDog");
        ownershipDogsRepository.deleteById(id);
    }

    /**
     * Редактирование записи в БД по идентификатору пары (владелец и питомец) выполняется изменение
     * поля статуса прохождения испытательного срока.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param id               идентификатор пары (владелец и питомец), не может быть null
     * @param passageProbation переданный статус принимает только: "Пройден", "Не пройден", "Не окончен"
     * @return запись в таблице ownership_dogs с измененным полем passageProbation
     */
    public OwnershipDogs editPassageProbation(long id, String passageProbation) {
        logger.debug("Was invoked method for edit passageProbation");
        OwnershipDogs ownershipDog = findOwnershipDogById(id);
        if (ownershipDog == null) {
            return null;
        }
        ownershipDog.setPassageProbation(passageProbation.toLowerCase(Locale.ROOT));
        return ownershipDogsRepository.save(ownershipDog);
    }

    /**
     * Поиск записией пар (владелецев и питомцев) в БД по дате окончания прохождения испытательного срока (endDateProbation).
     * Исползуется метод репозитория {@link OwnershipDogsRepository#findOwnershipDogsByEndDateProbation(LocalDate)}
     *
     * @param endDateProbation дата окончания прохождения испытательного срока
     * @return список владелецев и их питомцев из БД, в фомате JSON
     */
    public Collection<OwnershipDogs> findOwnershipDogsByEndDateProbation(LocalDate endDateProbation) {
        logger.info("Was invoked method for get ownership dogs info with endDateProbation");
        return ownershipDogsRepository.findOwnershipDogsByEndDateProbation(endDateProbation);
    }

    /**
     * Поиск записией пар (владелецев и питомцев) в БД по статусу прохождения испытательного срока (passageProbation).
     * Исползуется метод репозитория {@link OwnershipDogsRepository#findOwnershipDogsByPassageProbationIgnoreCase(String)}
     *
     * @param passageProbation переданный статус принимает только: "Пройден", "Не пройден", "Не окончен"
     * @return список владелецев и их питомцев из БД, в фомате JSON
     */
    public Collection<OwnershipDogs> findOwnershipDogByPassageProbation(String passageProbation) {
        logger.debug("Was invoked method for get ownership dogs info with passageProbation");
        return ownershipDogsRepository.findOwnershipDogsByPassageProbationIgnoreCase(passageProbation);
    }

    /**
     * Показывает все записи пар (владелецев и питомцев) в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     *
     * @return список всех владелецев и их питомцев из БД, в фомате JSON
     */
    public Collection<OwnershipDogs> findAllOwnershipDogs() {
        logger.debug("Was invoked method for find all");
        return ownershipDogsRepository.findAll();
    }


    /**
     * Метод актуализирует информацию (производит обратный отсчет) в поле
     * probationDays (колличество дней испытательного срока) в таблице ownership_dogs.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * Запуск метода происходит при помощи @Scheduled каждые 4 часа.
     */
    @Scheduled(cron = "0 0 0/4 * * *") // запуск метода каждые 4 часа после 00:00
    public Collection<OwnershipDogs> updateProbationDays() {
        logger.info("Run updateProbationDays method on schedule every 4 hours");

        List<OwnershipDogs> ownershipDogs = ownershipDogsRepository.findAll();
        LocalDate today = LocalDate.now();
        ownershipDogs.forEach(ownershipDog -> {
            LocalDate endDateProbation = ownershipDog.getEndDateProbation();
            String passageProbation = ownershipDog.getPassageProbation();
            int remainingDay = (int) ChronoUnit.DAYS.between(today, endDateProbation);
            if (remainingDay >= 0) {
                ownershipDog.setProbationDays(remainingDay);
                ownershipDogsRepository.save(ownershipDog);
            } else if (passageProbation.equalsIgnoreCase("не окончен")) {
                ownershipDog.setProbationDays(0);
                ownershipDogsRepository.save(ownershipDog);
            }
        });
        return ownershipDogs;
    }
}
