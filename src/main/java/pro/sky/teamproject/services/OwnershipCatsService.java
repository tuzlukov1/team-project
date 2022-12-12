package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.AnimalCat;
import pro.sky.teamproject.entity.OwnershipCats;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.repository.OwnershipCatsRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
public class OwnershipCatsService {

    static final String DEFAULT_PASSAGE_PROBATION = "не окончен";

    private final Logger logger = LoggerFactory.getLogger(OwnershipCatsService.class);

    private final OwnershipCatsRepository ownershipCatsRepository;
    private final UserCatService userCatService;
    private final CatService catService;

    public OwnershipCatsService(OwnershipCatsRepository ownershipCatsRepository,
                                UserCatService userCatService,
                                CatService catService) {
        this.ownershipCatsRepository = ownershipCatsRepository;
        this.userCatService = userCatService;
        this.catService = catService;
    }

    /**
     * Запись в БД идентификаторов нового владельца, выбранного питомца и
     * колличество дней испытательного срока. Выполняются запросы к БД через
     * сервисы {@link UserCatService} и {@link CatService} в таблицы users_cat и animals_cat.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param userId        идентификатор пользователя из таблицы users_cat
     * @param animalId      идентификатор питомца из таблицы animals_cat
     * @param probationDays назначенное колличество дней испытательного срока
     * @return созданная новая запись в таблице ownership_cats
     */
    public OwnershipCats addOwnershipCat(long userId, long animalId, int probationDays) {
        logger.debug("Was invoked method for create ownershipCat");
        UserCat foundUserCat = userCatService.findUserCatById(userId);
        AnimalCat foundAnimalCat = catService.findAnimalById(animalId);
        if (foundUserCat == null || foundAnimalCat == null) {
            return null;
        }
        LocalDate endProbation = LocalDate.now().plusDays(probationDays);

        OwnershipCats ownershipCat = new OwnershipCats();
        ownershipCat.setUserCat(foundUserCat);
        ownershipCat.setAnimalCat(foundAnimalCat);
        ownershipCat.setEndDateProbation(endProbation);
        ownershipCat.setProbationDays(probationDays);
        ownershipCat.setPassageProbation(DEFAULT_PASSAGE_PROBATION);

        return ownershipCatsRepository.save(ownershipCat);
    }

    /**
     * Поиск владельца и взятого им питомца в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     *
     * @param id идентификатор пары (владелец и питомец), не может быть null
     * @return данные о паре (владелец и питомец) в формате JSON
     */
    public OwnershipCats findOwnershipCatById(Long id) {
        logger.debug("Was invoked method for get ownership cat info with id = {} ", id);
        OwnershipCats foundOwnershipCat = ownershipCatsRepository.findById(id).orElse(null);
        logger.warn("Response {} ", foundOwnershipCat);
        return foundOwnershipCat;
    }

    /**
     * Редактирование записи в БД по идентификатору пары (владелец и питомец)
     * выполняется замена владельца и питомца в таблице ownership_cats.
     * Выполняются запросы к БД через сервисы {@link UserCatService} и
     * {@link CatService} в таблицы users_cat и animals_cat.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param id       идентификатор пары (владелец и питомец), не может быть null
     * @param userId   идентификатор пользователя из таблицы users_cat
     * @param animalId идентификатор питомца из таблицы animals_cat
     * @return измененная запись в таблице ownership_cats
     */
    public OwnershipCats editOwnershipCat(long id, long userId, long animalId) {
        logger.debug("Was invoked method for edit ownershipCat");
        OwnershipCats ownershipCat = findOwnershipCatById(id);
        UserCat foundUserCat = userCatService.findUserCatById(userId);
        AnimalCat foundAnimalCat = catService.findAnimalById(animalId);
        if (ownershipCat == null || foundUserCat == null || foundAnimalCat == null) {
            return null;
        }
        ownershipCat.setUserCat(foundUserCat);
        ownershipCat.setAnimalCat(foundAnimalCat);
        return ownershipCatsRepository.save(ownershipCat);
    }

    /**
     * Редактирование записи в БД по идентификатору пары (владелец и питомец) выполняется изменение
     * полей с колличеством дней испытательного срока и датой его окончания.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param id            идентификатор пары (владелец и питомец), не может быть null
     * @param probationDays назначенное колличество дней испытательного срока
     * @return запись в таблице ownership_cats с измененными полями probationDays и endDateProbation
     */
    public OwnershipCats editProbationDays(long id, int probationDays) {
        logger.debug("Was invoked method for edit probationDays");
        OwnershipCats ownershipCat = findOwnershipCatById(id);
        if (ownershipCat == null) {
            return null;
        }
        LocalDate endProbation = LocalDate.now().plusDays(probationDays);
        ownershipCat.setProbationDays(probationDays);
        ownershipCat.setEndDateProbation(endProbation);
        return ownershipCatsRepository.save(ownershipCat);
    }

    /**
     * Удаление записи о паре (владелец и питомец) из таблицы ownership_cats по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     *
     * @param id идентификатор пары (владелец и питомец), не может быть null
     */
    public void deleteOwnershipCat(long id) {
        logger.debug("Was invoked method for delete OwnershipCat");
        ownershipCatsRepository.deleteById(id);
    }

    /**
     * Редактирование записи в БД по идентификатору пары (владелец и питомец) выполняется изменение
     * поля статуса прохождения испытательного срока.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param id               идентификатор пары (владелец и питомец), не может быть null
     * @param passageProbation переданный статус принимает только: "Пройден", "Не пройден", "Не окончен"
     * @return запись в таблице ownership_cats с измененным полем passageProbation
     */
    public OwnershipCats editPassageProbation(long id, String passageProbation) {
        logger.debug("Was invoked method for edit passageProbation");
        OwnershipCats ownershipCat = findOwnershipCatById(id);
        if (ownershipCat == null) {
            return null;
        }
        ownershipCat.setPassageProbation(passageProbation.toLowerCase(Locale.ROOT));
        return ownershipCatsRepository.save(ownershipCat);
    }

    /**
     * Поиск записией пар (владелецев и питомцев) в БД по дате окончания прохождения испытательного срока (endDateProbation).
     * Исползуется метод репозитория {@link OwnershipCatsRepository#findOwnershipCatsByEndDateProbation(LocalDate)}
     *
     * @param endDateProbation дата окончания прохождения испытательного срока
     * @return список владелецев и их питомцев из БД, в фомате JSON
     */
    public Collection<OwnershipCats> findOwnershipCatsByEndDateProbation(LocalDate endDateProbation) {
        logger.info("Was invoked method for get ownership cats info with endDateProbation");
        return ownershipCatsRepository.findOwnershipCatsByEndDateProbation(endDateProbation);
    }

    /**
     * Поиск записией пар (владелецев и питомцев) в БД по статусу прохождения испытательного срока (passageProbation).
     * Исползуется метод репозитория {@link OwnershipCatsRepository#findOwnershipCatsByPassageProbationIgnoreCase(String)}
     *
     * @param passageProbation переданный статус принимает только: "Пройден", "Не пройден", "Не окончен"
     * @return список владелецев и их питомцев из БД, в фомате JSON
     */
    public Collection<OwnershipCats> findOwnershipCatByPassageProbation(String passageProbation) {
        logger.debug("Was invoked method for get ownership cats info with passageProbation");
        return ownershipCatsRepository.findOwnershipCatsByPassageProbationIgnoreCase(passageProbation);
    }

    /**
     * Показывает все записи пар (владелецев и питомцев) в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     *
     * @return список всех владелецев и их питомцев из БД, в фомате JSON
     */
    public Collection<OwnershipCats> findAllOwnershipCats() {
        logger.debug("Was invoked method for find all");
        return ownershipCatsRepository.findAll();
    }

    /**
     * Метод актуализирует информацию (производит обратный отсчет) в поле
     * probationDays (колличество дней испытательного срока) в таблице ownership_cats.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * Запуск метода происходит при помощи @Scheduled каждые 4 часа.
     */
    @Scheduled(cron = "0 0 0/4 * * *") // запуск метода каждые 4 часа после 00:00
    public void updateProbationDays() {
        logger.info("Run updateProbationDays method on schedule every 4 hours");

        List<OwnershipCats> ownershipCats = ownershipCatsRepository.findAll();
        LocalDate today = LocalDate.now();

        ownershipCats.forEach(ownershipCat -> {
            LocalDate endDateProbation = ownershipCat.getEndDateProbation();
            String passageProbation = ownershipCat.getPassageProbation();
            int remainingDay = (int) ChronoUnit.DAYS.between(today, endDateProbation);
            if (remainingDay >= 0) {
                ownershipCat.setProbationDays(remainingDay);
                ownershipCatsRepository.save(ownershipCat);
            } else if (passageProbation.equalsIgnoreCase("не окончен")) {
                ownershipCat.setProbationDays(0);
                ownershipCatsRepository.save(ownershipCat);
            }
        });
    }
}
