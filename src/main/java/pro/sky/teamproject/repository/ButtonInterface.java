package pro.sky.teamproject.repository;

import org.telegram.telegrambots.meta.api.objects.Update;
import pro.sky.teamproject.constant.ConstantCatMessageEnum;
import pro.sky.teamproject.constant.ConstantMessageEnum;

public interface ButtonInterface {

    /**
     * Вывод информации о собачьем приюте
     */
    default void sendShelterInformation(Update update) {
        String informationMessage = ConstantMessageEnum.SHELTER_INFORMATION.getMessage();
        infoWithBackButtonToInformationMenu(update, informationMessage);
    }

    /**
     * Вывод информации о кошачьем приюте
     */
    default void sendCatShelterInformation(Update update) {
        String informationMessage = ConstantCatMessageEnum.SHELTER_CAT_INFORMATION.getMessage();
        infoWithBackButtonToInformationCatMenu(update, informationMessage);
    }

    /**
     * Вывод информации о времени работы и адресе собчачьего приюта
     */
    default void sendShelterOpeningHoursAndAddress(Update update) {
        String openingHoursMessage = ConstantMessageEnum.SHELTER_OPENING_HOURS.getMessage();
        String addressMessage = ConstantMessageEnum.SHELTER_ADDRESS.getMessage();
        infoWithBackButtonToInformationMenu(update, openingHoursMessage + addressMessage);
    }

    /**
     * Вывод информации о времени работы и адресе кошачьего приюта
     */
    default void sendCatShelterOpeningHoursAndAddress(Update update) {
        String openingHoursMessage = ConstantCatMessageEnum.SHELTER_CAT_OPENING_HOURS.getMessage();
        String addressMessage = ConstantCatMessageEnum.SHELTER_CAT_ADDRESS.getMessage();
        infoWithBackButtonToInformationCatMenu(update, openingHoursMessage + addressMessage);
    }

    /**
     * Вывод информации о контактных данных охраны для собачьего приюта
     */
    default void sendDogShelterSecurityNumber(Update update) {
        String securityNumber = ConstantMessageEnum.SHElTER_DOG_SECURITY_NUMBER.getMessage();
        infoWithBackButtonToInformationMenu(update, securityNumber);
    }

    /**
     * Вывод информации о контактных данных охраны для кошачьего приюта
     */
    default void sendCatShelterSecurityNumber(Update update) {
        String securityNumber = ConstantCatMessageEnum.SHElTER_CAT_SECURITY_NUMBER.getMessage();
        infoWithBackButtonToInformationCatMenu(update, securityNumber);
    }

    /**
     * Вывод информации о правилах безопасности собачьего приюта
     */
    default void sendShelterSafetyRegulations(Update update) {
        String safetyRegulationsMessage = ConstantMessageEnum.SHELTER_SAFETY_REGULATIONS.getMessage();
        infoWithBackButtonToInformationMenu(update, safetyRegulationsMessage);
    }

    /**
     * Вывод информации о правилах безопасности кошачьего приюта
     */
    default void sendCatShelterSafetyRegulations(Update update) {
        String safetyRegulationsMessage = ConstantCatMessageEnum.SHELTER_CAT_SAFETY_REGULATIONS.getMessage();
        infoWithBackButtonToInformationCatMenu(update, safetyRegulationsMessage);
    }


    /**
     * Вывод информации о правилах знакомства с собакой
     */
    default void sendDogAcquaintanceRules(Update update) {
        String informationMessage = ConstantMessageEnum.DOG_ACQUAINTANCE_RULES.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод информации о правилах знакомства с кошкой
     */
    default void sendCatAcquaintanceRules(Update update) {
        String informationMessage = ConstantCatMessageEnum.CAT_ACQUAINTANCE_RULES.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод списка документов, необходимых для того, чтобы взять собаку из приюта
     */
    default void sendDocumentsForDogs(Update update) {
        String informationMessage = ConstantMessageEnum.DOCUMENTS_FOR_DOGS.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка документов, необходимых для того, чтобы взять кошку из приюта
     */
    default void sendDocumentsForCats(Update update) {
        String informationMessage = ConstantCatMessageEnum.DOCUMENTS_FOR_CATS.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по транспортировке собаки
     */
    default void sendTransportationAdvice(Update update) {
        String informationMessage = ConstantMessageEnum.TRANSPORTATION_ADVICE.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по транспортировке кошки
     */
    default void sendCatTransportationAdvice(Update update) {
        String informationMessage = ConstantCatMessageEnum.TRANSPORTATION_CAT_ADVICE.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для щенка
     */
    default void sendHomeImprovementForPuppy(Update update) {
        String informationMessage = ConstantMessageEnum.HOME_IMPROVEMENT_FOR_PUPPY.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для котенка
     */
    default void sendHomeImprovementForKitty(Update update) {
        String informationMessage = ConstantCatMessageEnum.HOME_IMPROVEMENT_FOR_KITTY.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }


    /**
     * Вывод списка рекомендаций по обустройству дома для взрослой собаки
     */
    default void sendHomeImprovementForAdultDog(Update update) {
        String informationMessage = ConstantMessageEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_DOG.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для взрослой кошки
     */
    default void sendHomeImprovementForAdultCat(Update update) {
        String informationMessage = ConstantCatMessageEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_CAT.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }


    /**
     * Вывод списка рекомендаций по обустройству дома для собаки с ограниченными возможностями
     */
    default void sendHomeImprovementForDogsWithSpecialNeeds(Update update) {
        String informationMessage = ConstantMessageEnum.HOME_IMPROVEMENT_FOR_DOG_WITH_SPECIAL_NEEDS.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для кошки с ограниченными возможностями
     */
    default void sendHomeImprovementForCatsWithSpecialNeeds(Update update) {
        String informationMessage = ConstantCatMessageEnum.HOME_IMPROVEMENT_FOR_CAT_WITH_SPECIAL_NEEDS.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }


    /**
     * Вывод советов кинолога по первичному общению с собакой
     */
    default void sendCynologistsAdviceOnInitialCommunication(Update update) {
        String informationMessage = ConstantMessageEnum.CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод рекомендаций по проверенным кинологам для дальнейшего обращения к ним
     */
    default void sendRecommendationsForProvenCynologists(Update update) {
        String informationMessage = ConstantMessageEnum.RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Запись контактных данных для связи
     */
    default void registrationUserDog(Update update) {
        String informationMessage = ConstantMessageEnum.CANDIDATE_REGISTRATION.getMessage();
//        startRegistration = true;
        infoWithBackButtonToInformationMenu(update, informationMessage);
    }

    /**
     * Запись контактных данных в кошачий приют для связи
     */
    default void registrationUserCat(Update update) {
        String informationMessage = ConstantCatMessageEnum.CANDIDATE_CAT_REGISTRATION.getMessage();
//        startCatRegistration = true;
        infoWithBackButtonToInformationCatMenu(update, informationMessage);
    }

    /**
     * Вывод информации почему могут отказать и не дать забрать собаку из приюта
     */
    default void sendReasonForRefusal(Update update) {
        String informationMessage = ConstantMessageEnum.REASON_FOR_REFUSAL.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод информации почему могут отказать и не дать забрать кошку из приюта
     */
    default void sendCatReasonForRefusal(Update update) {
        String informationMessage = ConstantCatMessageEnum.REASON_CAT_FOR_REFUSAL.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод информации по отправке отчета в собачий приют
     */
    default void sendDogReport(Update update) {
        String informationMessage = ConstantMessageEnum.DOG_REPORT.getMessage();
        infoWithBackButtonToDogMainMenu(update, informationMessage);
    }

    /**
     * Вывод информации по отправке отчета в кошачий приют
     */
    default void sendCatReport(Update update) {
        String informationMessage = ConstantCatMessageEnum.CAT_REPORT.getMessage();
        infoWithBackButtonToCatMainMenu(update, informationMessage);
    }

    /**
     * Вывод меню с выбором приюта
     */
    void shelterMenuKeyboard(Update update);

    /**
     * Вывод главного меню для кошек с вопросами под сообщением после выбора приюта
     */
    void catMainMenuKeyboard(Update update);

    /**
     * Вывод главного меню для собак с вопросами под сообщением после выбора приюта
     */
    void dogMainMenuKeyboard(Update update);

    /**
     * Вывод меню с вопросами под сообщением после нажатия кнопки меню "Информация о приюте"
     */
    void dogInformationMenuKeyboard(Update update);
    /**
     * Вывод меню кошчьего приюта с вопросами под сообщением после нажатия кнопки меню "Информация о приюте"
     */
    void catInformationMenuKeyboard(Update update);

    /**
     * Вывод меню с вопросами под сообщением после нажатия кнопки меню "Как взять собаку из приюта"
     */
    void takeTheDogMenuKeyboard(Update update);


    /**
     * Вывод меню с вопросами под сообщением после нажатия кнопки меню "Как взять кошку из приюта"
     */
    void takeTheCatMenuKeyboard(Update update);

    /**
     * Вывод кнопки назад в меню "Информация о приюте"(для собак)
     */
    void infoWithBackButtonToInformationMenu(Update update, String text);

    /**
     * Вывод кнопки назад в меню "Информация о приюте"(для кошек)
     */
    void infoWithBackButtonToInformationCatMenu(Update update, String text);

    /**
     * Вывод кнопки назад в меню "Как взять собаку из приюта"
     */
    void infoWithBackButtonToTakeTheDogMenu(Update update, String text);

    /**
     * Вывод кнопки назад в меню "Как взять кошку из приюта"
     */
    void infoWithBackButtonToTakeTheCatMenu(Update update, String text);

    /**
     * Вывод кнопки назад в меню c запросами в собачьем приюте
     */
    void infoWithBackButtonToDogMainMenu(Update update, String text);

    void infoWithBackButtonToCatMainMenu(Update update, String text);

}
