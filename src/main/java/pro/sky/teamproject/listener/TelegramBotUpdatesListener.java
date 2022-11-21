package pro.sky.teamproject.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.teamproject.configuration.TelegramBotConfiguration;
import pro.sky.teamproject.constant.ConstantMessageEnum;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {

    private final TelegramBotConfiguration configuration;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public TelegramBotUpdatesListener(TelegramBotConfiguration configuration, UserService userService) {
        this.configuration = configuration;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return configuration.getBotName();
    }

    @Override
    public String getBotToken() {
        return configuration.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("start onUpdateReceived");
        if (update.hasMessage() && update.getMessage().hasText()) {
            String firstName = update.getMessage().getChat().getFirstName();
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getUserName();
            User user = new User();

            if ("/start".equals(messageText)) {
                if (userService.findUserByChatId(chatId).isEmpty()) {
                    sendGreetings(chatId, firstName);
                    user.setUserName(userName);
                    user.setChatId(chatId);
                    userService.updateUser(user);
                }
                sendMessageToUser(chatId, ConstantMessageEnum.BOT_INFORMATION.getMessage());
                mainMenuKeyboard(update);
            } else if (messageText.matches("[А-Я][а-я]+\\s[А-Я][а-я]+")) {
                logger.info("пользователь ввел свое имя");
                user = userService.findUserByChatId(chatId).get();
                user.setFullName(messageText);
                userService.updateUser(user);
                String textMessage = "Введите свой номер телефона, только цифры, например '79000000000'";
                sendMessageToUser(chatId, textMessage);
            } else if (messageText.matches("^\\d{5,15}$")) {
                logger.info("пользователь ввел номер телефона");
                Optional<User> userByChatId = userService.findUserByChatId(chatId);
                if (userByChatId.isPresent()) {
                    user = userByChatId.get();
                    user.setPhone(Long.valueOf(messageText));
                    userService.updateUser(user);
                    String textMessage = "Регистрация успешна!";
                    sendMessageToUser(chatId, textMessage);

                    mainMenuKeyboard(update);
                }
            } else {
                sendMessageToUser(chatId, "Sorry, command was not recognized");
            }
        } else if (update.hasCallbackQuery()) {
            checkCallBackQuery(update);
        }
    }

    /**
     * Вызов действий после нажатия кнопок в меню
     */
    private void checkCallBackQuery(Update update) {
        String callBackData = update.getCallbackQuery().getData();
        if (callBackData.equals(ConstantMessageEnum.INFORMATION_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку информации о приюте");
            informationMenuKeyboard(update);
        }

        if (callBackData.equals(ConstantMessageEnum.HOW_TAKE_PET_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку как взять собаку из приюта");
            takeTheDogMenuKeyboard(update);
        }

        if (callBackData.equals(ConstantMessageEnum.SEND_REPORT_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку прислать отчет о питомце");
            String textMessage = "Вывод меню 3-го. этапа - в разработке";
            sendEditMessageToUser(update, textMessage);
            //Здесь будет метод для обработки команды
        }

        if (callBackData.equals(ConstantMessageEnum.CALL_VOLUNTEER_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку связи с волонтером");
            String textMessage = "В ближайшее время с вами свяжется волонтер";
            sendEditMessageToUser(update, textMessage);
        }

        if (callBackData.equals(ConstantMessageEnum.ABOUT_THE_SHELTER.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать о приюте");
            sendShelterInformation(update);
        }

        if (callBackData.equals(ConstantMessageEnum.LOCATION.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать об адресе и режиме работы");
            sendShelterOpeningHoursAndAddress(update);
        }

        if (callBackData.equals(ConstantMessageEnum.SAFETY_AT_THE_SHELTER.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать о правилах безопасности");
            sendShelterSafetyRegulations(update);
        }

        if (callBackData.equals(ConstantMessageEnum.REGISTRATION_VOLUNTEER.getMessage())) {
            logger.info("пользователь нажал на кнопку регистрации для волонтера");
            String textMessage = "Введите свое имя, например 'Иванов Иван'";
            sendEditMessageToUser(update, textMessage);
        }

        if (callBackData.equals(ConstantMessageEnum.BACK_TO_MAIN_MENU.getMessage())) {
            logger.info("пользователь нажал на кнопку назад");
            mainMenuKeyboard(update);
        }

        if (callBackData.equals(ConstantMessageEnum.PUPPY_DATING_RULES_BUTTON.getMessage())) {
            logger.info("пользователь нажад на кнопку правила знакомства с собакой");
            //Здесь будет метод для обработки команды
        }

        if (callBackData.equals(ConstantMessageEnum.DOCUMENTS_FOR_DOGS_BUTTON.getMessage())) {
            logger.info("список документов, необходимых для того, чтобы взять собаку из приюта");
            //Здесь будет метод для обработки команды
        }

        if (callBackData.equals(ConstantMessageEnum.TRANSPORTATION_ADVICE_BUTTON.getMessage())) {
            logger.info("список рекомендаций по транспортировке животного");
            //Здесь будет метод для обработки команды
        }

        if (callBackData.equals(ConstantMessageEnum.HOME_IMPROVEMENT_FOR_PUPPY_BUTTON.getMessage())) {
            logger.info("список рекомендаций по обустройству дома для щенка");
            //Здесь будет метод для обработки команды
        }

        if (callBackData.equals(ConstantMessageEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_DOG_BUTTON.getMessage())) {
            logger.info("список рекомендаций по обустройству дома для взрослой собаки");
            //Здесь будет метод для обработки команды
        }

        if (callBackData.equals(ConstantMessageEnum.HOME_IMPROVEMENT_FOR_DOG_WITH_DISABILITY_BUTTON.getMessage())) {
            logger.info("список рекомендаций по обустройству дома для собаки с ограниченными возможностями");
            //Здесь будет метод для обработки команды
        }

        if (callBackData.equals(ConstantMessageEnum.CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION_BUTTON.getMessage())) {
            logger.info("советы кинолога по первичному общению с собакой");
            //Здесь будет метод для обработки команды
        }
        if (callBackData.equals(ConstantMessageEnum.RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS_BUTTON.getMessage())) {
            logger.info("пользователь нажад на кнопку рекомендации по проверенным кинологам для дальнейшего обращения к ним.");
            //Здесь будет метод для обработки команды
        }
        if (callBackData.equals(ConstantMessageEnum.CANDIDATE_REGISTRATION_BUTTON.getMessage())) {
            logger.info("пользователь нажад на кнопку  записать контактные данные для связи ");
            //Здесь будет метод для обработки команды
        }
        if (callBackData.equals(ConstantMessageEnum.REASON_FOR_REFUSAL_BUTTON.getMessage())) {
            logger.info("пользователь нажад на кнопку почему могут отказать и не дать забрать собаку из приюта");
            //Здесь будет метод для обработки команды
        }
    }

    /**
     * Вывод информации о приюте
     */
    private void sendShelterInformation(Update update) {
        String informationMessage = ConstantMessageEnum.SHELTER_INFORMATION.getMessage();
        infoWithBackButtonToInformationMenu(update, informationMessage);
    }

    /**
     * Вывод информации о времени работы и адресе
     */
    private void sendShelterOpeningHoursAndAddress(Update update) {
        String openingHoursMessage = ConstantMessageEnum.SHELTER_OPENING_HOURS.getMessage();
        String addressMessage = ConstantMessageEnum.SHELTER_ADDRESS.getMessage();
        infoWithBackButtonToInformationMenu(update, openingHoursMessage + addressMessage);
    }

    /**
     * Вывод информации о правилах безопасности
     */
    private void sendShelterSafetyRegulations(Update update) {
        String safetyRegulationsMessage = ConstantMessageEnum.SHELTER_SAFETY_REGULATIONS.getMessage();
        infoWithBackButtonToInformationMenu(update, safetyRegulationsMessage);
    }

    /**
     * Вывод приветственного сообщения по команде /start
     */
    private void sendGreetings(long chatId, String name) {
        logger.info("Method sendWelcome started");
        String textMessage = ConstantMessageEnum.GREETINGS_TEXT.getMessage() + name;
        sendMessageToUser(chatId, textMessage);
    }

    /**
     * Метод для отправки сообщения пользователю
     */
    private void sendMessageToUser(long chatId, String textMessage) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textMessage);
        executeMessage(message);
    }

    /**
     * Метод для изменения и отправки сообщения пользователю после нажатия на кнопку из меню
     */
    private void sendEditMessageToUser(Update update, String textMessage) {
        long messageId;
        long chatId;
        EditMessageText message = new EditMessageText();

        if (update.getCallbackQuery() != null) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
        } else {
            chatId = update.getMessage().getChatId();
            messageId = update.getMessage().getMessageId();
        }

        message.setChatId(String.valueOf(chatId));
        message.setText(textMessage);
        message.setMessageId((int) messageId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Обработка TelegramApiException при отправке сообщений
     */
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Обработка TelegramApiException при отправке измененных сообщений
     */
    private void executeEditMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Вывод главного меню с вопросами под сообщением после вызова команды /start
     */
    private void mainMenuKeyboard(Update update) {
        EditMessageText editMessage = new EditMessageText();
        SendMessage message = new SendMessage();
        if (update.hasCallbackQuery()) {
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            editMessage.setChatId(String.valueOf(chatId));
            editMessage.setText(ConstantMessageEnum.SELECT_REQUEST.getMessage());
            editMessage.setMessageId((int) messageId);
        } else {
            long chatId = update.getMessage().getChatId();
            message.setChatId(String.valueOf(chatId));
            message.setText(ConstantMessageEnum.SELECT_REQUEST.getMessage());
        }

        InlineKeyboardMarkup mainMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> rowFirst = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();
        List<InlineKeyboardButton> rowThird = new ArrayList<>();
        List<InlineKeyboardButton> rowForth = new ArrayList<>();

        var informationButton = new InlineKeyboardButton();
        informationButton.setText("Информация о приюте");
        informationButton.setCallbackData(ConstantMessageEnum.INFORMATION_BUTTON.getMessage());
        rowFirst.add(informationButton);

        var howTakePetButton = new InlineKeyboardButton();
        howTakePetButton.setText("Как взять собаку из приюта");
        howTakePetButton.setCallbackData(ConstantMessageEnum.HOW_TAKE_PET_BUTTON.getMessage());
        rowSecond.add(howTakePetButton);

        var sendReportButton = new InlineKeyboardButton();
        sendReportButton.setText("Прислать отчет о питомце");
        sendReportButton.setCallbackData(ConstantMessageEnum.SEND_REPORT_BUTTON.getMessage());
        rowThird.add(sendReportButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(ConstantMessageEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowForth.add(callVolunteerButton);

        rows.add(rowFirst);
        rows.add(rowSecond);
        rows.add(rowThird);
        rows.add(rowForth);

        mainMenuKeyboard.setKeyboard(rows);

        if (update.hasCallbackQuery()) {
            editMessage.setReplyMarkup(mainMenuKeyboard);
            executeEditMessage(editMessage);
        } else {
            message.setReplyMarkup(mainMenuKeyboard);
            executeMessage(message);
        }
    }

    /**
     * Вывод меню с вопросами под сообщением после нажатия кнопки меню "Информация о приюте"
     */
    private void informationMenuKeyboard(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(ConstantMessageEnum.SELECT_REQUEST.getMessage());
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup informationMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> informationMenuRows = new ArrayList<>();

        List<InlineKeyboardButton> rowFirst = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();
        List<InlineKeyboardButton> rowThird = new ArrayList<>();
        List<InlineKeyboardButton> rowFourth = new ArrayList<>();
        List<InlineKeyboardButton> rowFifth = new ArrayList<>();
        List<InlineKeyboardButton> rowSixth = new ArrayList<>();

        var aboutTheShelterButton = new InlineKeyboardButton();
        aboutTheShelterButton.setText("О приюте");
        aboutTheShelterButton.setCallbackData(ConstantMessageEnum.ABOUT_THE_SHELTER.getMessage());
        rowFirst.add(aboutTheShelterButton);

        var location = new InlineKeyboardButton();
        location.setText("Адрес, схема проезда,\n режим работы");
        location.setCallbackData(ConstantMessageEnum.LOCATION.getMessage());
        rowSecond.add(location);

        var safetyAtTheShelter = new InlineKeyboardButton();
        safetyAtTheShelter.setText("ТБ на территории приюта");
        safetyAtTheShelter.setCallbackData(ConstantMessageEnum.SAFETY_AT_THE_SHELTER.getMessage());
        rowThird.add(safetyAtTheShelter);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(ConstantMessageEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowFourth.add(callVolunteerButton);

        var registration = new InlineKeyboardButton();
        registration.setText("Регистрация волонтера");
        registration.setCallbackData(ConstantMessageEnum.REGISTRATION_VOLUNTEER.getMessage());
        rowFifth.add(registration);

        var backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Назад");
        backToMainMenu.setCallbackData(ConstantMessageEnum.BACK_TO_MAIN_MENU.getMessage());
        rowSixth.add(backToMainMenu);

        informationMenuRows.add(rowFirst);
        informationMenuRows.add(rowSecond);
        informationMenuRows.add(rowThird);
        informationMenuRows.add(rowFourth);
        informationMenuRows.add(rowFifth);
        informationMenuRows.add(rowSixth);

        informationMenuKeyboard.setKeyboard(informationMenuRows);

        message.setReplyMarkup(informationMenuKeyboard);
        executeEditMessage(message);
    }

    /**
     * Вывод меню с вопросами под сообщением после нажатия кнопки меню "Как взять собаку из приюта"
     */
    private void takeTheDogMenuKeyboard(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(ConstantMessageEnum.SELECT_REQUEST.getMessage());
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup takeTheDogMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> theDogMenuRows = new ArrayList<>();

        List<InlineKeyboardButton> rowFirst = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();
        List<InlineKeyboardButton> rowThird = new ArrayList<>();
        List<InlineKeyboardButton> rowFourth = new ArrayList<>();
        List<InlineKeyboardButton> rowFifth = new ArrayList<>();
        List<InlineKeyboardButton> rowSixth = new ArrayList<>();
        List<InlineKeyboardButton> rowSeventh = new ArrayList<>();
        List<InlineKeyboardButton> rowEighth = new ArrayList<>();
        List<InlineKeyboardButton> rowNinth = new ArrayList<>();
        List<InlineKeyboardButton> rowTenth = new ArrayList<>();
        List<InlineKeyboardButton> rowEleventh = new ArrayList<>();
        List<InlineKeyboardButton> rowTwelfth = new ArrayList<>();

        var puppyDatingRulesButton = new InlineKeyboardButton();
        puppyDatingRulesButton.setText("Правила знакомства с собакой");
        puppyDatingRulesButton.setCallbackData(ConstantMessageEnum.PUPPY_DATING_RULES_BUTTON.getMessage());
        rowFirst.add(puppyDatingRulesButton);

        var documentsForDogsButton = new InlineKeyboardButton();
        documentsForDogsButton.setText("Список документов, чтобы взять собаку из приюта");
        documentsForDogsButton.setCallbackData(ConstantMessageEnum.DOCUMENTS_FOR_DOGS_BUTTON.getMessage());
        rowSecond.add(documentsForDogsButton);

        var transportationAdviceButton = new InlineKeyboardButton();
        transportationAdviceButton.setText("Рекомендации по транспортировке животного");
        transportationAdviceButton.setCallbackData(ConstantMessageEnum.TRANSPORTATION_ADVICE_BUTTON.getMessage());
        rowThird.add(transportationAdviceButton);


        var homeImprovementForPuppyButton = new InlineKeyboardButton();
        homeImprovementForPuppyButton.setText("Обустройство дома для щенка");
        homeImprovementForPuppyButton.setCallbackData(ConstantMessageEnum.HOME_IMPROVEMENT_FOR_PUPPY_BUTTON.getMessage());
        rowFourth.add(homeImprovementForPuppyButton);

        var homeImprovementForAnAdultDogButton = new InlineKeyboardButton();
        homeImprovementForAnAdultDogButton.setText("Обустройство дома для взрослой собаки");
        homeImprovementForAnAdultDogButton.setCallbackData(ConstantMessageEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_DOG_BUTTON.getMessage());
        rowFifth.add(homeImprovementForAnAdultDogButton);

        var homeImprovementForDogWithDisability = new InlineKeyboardButton();
        homeImprovementForDogWithDisability.setText("Обустройство дома для собаки с ограниченными возможностями");
        homeImprovementForDogWithDisability.setCallbackData(ConstantMessageEnum.HOME_IMPROVEMENT_FOR_DOG_WITH_DISABILITY_BUTTON.getMessage());
        rowSixth.add(homeImprovementForDogWithDisability);

        var cynologistsAdviceOnInitialCommunicationButton = new InlineKeyboardButton();
        cynologistsAdviceOnInitialCommunicationButton.setText("Советы кинолога по первичному общению с собакой");
        cynologistsAdviceOnInitialCommunicationButton.setCallbackData(ConstantMessageEnum.CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION_BUTTON.getMessage());
        rowSeventh.add(cynologistsAdviceOnInitialCommunicationButton);

        var recommendationsForProvenCynologistsButton = new InlineKeyboardButton();
        recommendationsForProvenCynologistsButton.setText("Рекомендации по проверенным кинологам");
        recommendationsForProvenCynologistsButton.setCallbackData(ConstantMessageEnum.RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS_BUTTON.getMessage());
        rowEighth.add(recommendationsForProvenCynologistsButton);

        var reasonsForRefusalButton = new InlineKeyboardButton();
        reasonsForRefusalButton.setText("Список причин, почему могут отказать");
        reasonsForRefusalButton.setCallbackData(ConstantMessageEnum.REASON_FOR_REFUSAL_BUTTON.getMessage());
        rowNinth.add(reasonsForRefusalButton);

        var candidateRegistrationButton = new InlineKeyboardButton();
        candidateRegistrationButton.setText("Запись контактных данных");
        candidateRegistrationButton.setCallbackData(ConstantMessageEnum.CANDIDATE_REGISTRATION_BUTTON.getMessage());
        rowTenth.add(candidateRegistrationButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(ConstantMessageEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowEleventh.add(callVolunteerButton);

        var backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Назад");
        backToMainMenu.setCallbackData(ConstantMessageEnum.BACK_TO_MAIN_MENU.getMessage());
        rowTwelfth.add(backToMainMenu);

        theDogMenuRows.add(rowFirst);
        theDogMenuRows.add(rowSecond);
        theDogMenuRows.add(rowThird);
        theDogMenuRows.add(rowFourth);
        theDogMenuRows.add(rowFifth);
        theDogMenuRows.add(rowSixth);
        theDogMenuRows.add(rowSeventh);
        theDogMenuRows.add(rowEighth);
        theDogMenuRows.add(rowNinth);
        theDogMenuRows.add(rowTenth);
        theDogMenuRows.add(rowEleventh);
        theDogMenuRows.add(rowTwelfth);

        takeTheDogMenuKeyboard.setKeyboard(theDogMenuRows);

        message.setReplyMarkup(takeTheDogMenuKeyboard);
        executeEditMessage(message);
    }


    /**
     * Вывод кнопки назад в меню "Информация о приюте"
     */
    private void infoWithBackButtonToInformationMenu(Update update, String text) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup informationMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> informationMenuRows = new ArrayList<>();
        List<InlineKeyboardButton> rowOne = new ArrayList<>();

        var backToInformationMenu = new InlineKeyboardButton();
        backToInformationMenu.setText("Назад");
        backToInformationMenu.setCallbackData(ConstantMessageEnum.INFORMATION_BUTTON.getMessage());
        rowOne.add(backToInformationMenu);

        informationMenuRows.add(rowOne);

        informationMenuKeyboard.setKeyboard(informationMenuRows);

        message.setReplyMarkup(informationMenuKeyboard);
        executeEditMessage(message);
    }

    /**
     * Вывод кнопки назад в меню "Как взять собаку из приюта"
     */
    private void infoWithBackButtonToTakeTheDogMenu(Update update, String text) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup takeDogMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> takeDogMenuRows = new ArrayList<>();
        List<InlineKeyboardButton> rowOne = new ArrayList<>();

        var backToTakeDogMenu = new InlineKeyboardButton();
        backToTakeDogMenu.setText("Назад");
        backToTakeDogMenu.setCallbackData(ConstantMessageEnum.HOW_TAKE_PET_BUTTON.getMessage());
        rowOne.add(backToTakeDogMenu);

        takeDogMenuRows.add(rowOne);

        takeDogMenuKeyboard.setKeyboard(takeDogMenuRows);

        message.setReplyMarkup(takeDogMenuKeyboard);
        executeEditMessage(message);
    }
}