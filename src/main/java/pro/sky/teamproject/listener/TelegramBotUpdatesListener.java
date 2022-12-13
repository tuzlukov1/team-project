package pro.sky.teamproject.listener;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.teamproject.configuration.TelegramBotConfiguration;
import pro.sky.teamproject.constant.CallBackDataEnum;
import pro.sky.teamproject.constant.ConstantCatMessageEnum;
import pro.sky.teamproject.constant.ConstantMessageEnum;
import pro.sky.teamproject.entity.*;
import pro.sky.teamproject.repository.UsersCatRepository;
import pro.sky.teamproject.repository.UsersDogRepository;
import pro.sky.teamproject.repository.UsersRepository;
import pro.sky.teamproject.services.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {
    private final UsersRepository usersRepository;

    private final TelegramBotConfiguration configuration;
    private final UserService userService;
    private final UserReportService userReportService;
    private final UserReportPhotoService userReportPhotoService;
    private final UserDogService userDogService;
    private final UserCatService userCatService;
    private final UsersCatRepository usersCatRepository;
    private final UsersDogRepository usersDogRepository;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public TelegramBotUpdatesListener(TelegramBotConfiguration configuration, UserService userService,
                                      UserReportService userReportService, UserReportPhotoService userReportPhotoService, UserDogService userDogService, UserCatService userCatService,
                                      UsersCatRepository usersCatRepository, UsersDogRepository usersDogRepository,
                                      UsersRepository usersRepository) {
        this.configuration = configuration;
        this.userService = userService;
        this.userReportService = userReportService;
        this.userReportPhotoService = userReportPhotoService;
        this.userDogService = userDogService;
        this.userCatService = userCatService;
        this.usersCatRepository = usersCatRepository;
        this.usersDogRepository = usersDogRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public String getBotUsername() {
        return configuration.getBotName();
    }

    @Override
    public String getBotToken() {
        return configuration.getToken();
    }


    /**
     * Вывод напоминания о необходимости отправки отчета ежедневно в 10 утра,
     * для тех, кто забывает его отправлять
     * для тех, кто прошел/не прошел испытательный срок
     * кому продлили испытательный срок
     */
    @Scheduled(cron = "0 0 10 * * *")
    public void sendCatDogReport() {

        String greetingMessage = "Поздравляем, вы прошли испытательный срок!";
        String sadMessage = "К сожалению, вы не прошли спытательный срок." +
                "\nВ ближайшие сутки подготовьте животное для транспартировки " +
                "\nС вами дополнительно свяжется волонтер для назначения времени привоза животного";
        String extraTimeMessage = "Вам назначено дополнительный испытательный срок." +
                "\nОн составляет - ";

        List<User> userList = usersRepository.findAll();
        userList.forEach(
                user -> {
                    if (user.getHaveWarning()) {
                        long chatId = user.getChatId();
                        sendMessageToUser(chatId, ConstantMessageEnum.WARNING.getMessage());
                        logger.info("Пользователь с ID {} было отправлено предупреждение" +
                                " о необходимости отправки отчета", chatId);
                        user.setHaveWarning(false);
                        userService.updateUser(user);
                    }
                }
        );

        // Для усыновителей собак:

        List<UserDog> sendDogReport = usersDogRepository.findAll();

        sendDogReport.forEach(userDog -> {
            long chatId = userDog.getId();
            //Здесь нужно внести проверку со столбиком количества дней оставшихся у усыновителя
            // в ownerShipDog и если они кончились и волонтер одобрил на завершение, то отправлять сообщение
            if (greetingMessage.equals("rr")) {
                sendMessageToUser(chatId, greetingMessage);
                logger.info("Пользователь с ID {} было отправлено уведомление " +
                        "о прохождении испытательного срока", chatId);
            }
            //Здесь нужно внести проверку со столбиком количества дней оставшихся у усыновителя
            // в ownerShipDog и если они кончились и волонтер не одобрил на завершение, то отправлять сообщение
            if (sadMessage.equals("qq")) {
                sendMessageToUser(chatId, sadMessage);
                logger.info("Пользователь с ID {} было отправлено уведомление " +
                        "о провале испытательного срока", chatId);
            }
            //Здесь нужно внести проверку со столбиком количества дней оставшихся у усыновителя
            // в ownerShipDog и если они кончились и волонтер их продлил, то отправлять сообщение с указанием
            // количеством доп дней(лучше создать доп таблицу с булевой переменной продлил/не продлил)
            if (extraTimeMessage.equals("yy")) {
                sendMessageToUser(chatId, extraTimeMessage + 14);
                logger.info("Пользователь с ID {} было отправлено уведомление " +
                        "о продлении испытательно срока", chatId);
            }

        });

        //Для усыновителей кошек:

        List<UserCat> sendWarningCatReport = usersCatRepository.findAll();
        sendWarningCatReport.forEach(userCat -> {
            long chatId = userCat.getId();

            if (greetingMessage.equals("rr")) {
                sendMessageToUser(chatId, greetingMessage);
                logger.info("Пользователь с ID {} было отправлено уведомление " +
                        "о прохождении испытательного срока", chatId);
            }
            if (sadMessage.equals("qq")) {
                sendMessageToUser(chatId, sadMessage);
                logger.info("Пользователь с ID {} было отправлено уведомление " +
                        "о провале испытательного срока", chatId);
            }
            if (extraTimeMessage.equals("yy")) {
                sendMessageToUser(chatId, extraTimeMessage + 14);
                logger.info("Пользователь с ID {} было отправлено уведомление " +
                        "о продлении испытательно срока", chatId);
            }
        });
    }

    public UserDog DogRegister(String messageText, UserDog userDog, Long chatId, Update update, User user) {
        if (messageText.matches("[А-Я][а-я]+\\s[А-Я][а-я]+")) {
            logger.info("пользователь ввел свое имя");
            userDog.setFullName(messageText);
            userDogService.updateUserDog(userDog);
            String textMessage = "Введите свой номер телефона, только цифры, например '79000000000'";
            sendMessageToUser(chatId, textMessage);
        } else if (messageText.matches("^\\d{5,15}$")) {
            logger.info("пользователь ввел номер телефона");
            userDog.setPhone(Long.valueOf(messageText));
            userDogService.updateUserDog(userDog);
            String textMessage = "Регистрация успешна!";
            user.setStartRegistration(false);
            userService.updateUser(user);
            sendMessageToUser(chatId, textMessage);
            dogMainMenuKeyboard(update);
        }
        return userDog;
}


    @Override
    public void onUpdateReceived(Update update) {
        logger.info("start onUpdateReceived");

        if (update.hasMessage()) {
            String firstName = update.getMessage().getChat().getFirstName();
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getUserName();
            Optional<User> optionalUser = userService.findUserByChatId(chatId);
            User user = optionalUser.isPresent() ? optionalUser.get() : new User();
            UserDog userDog = optionalUser.isPresent() ? userDogService.findUserDogByChatId(user.getChatId()) : new UserDog();
            UserCat userCat =  optionalUser.isPresent() ? userCatService.findUserCatById(user.getId()) : new UserCat();

            if ("/start".equals(messageText)) {
                if (userService.findUserByChatId(chatId).isEmpty()) {
                    sendGreetings(chatId, firstName);
                    user.setUserName(userName);
                    user.setChatId(chatId);
                    user.setStartRegistration(false);
                    user.setStartReport(false);
                    user.setHaveWarning(false);
                    user = userService.updateUser(user);
                }
                sendMessageToUser(chatId, ConstantMessageEnum.BOT_INFORMATION.getMessage());
                shelterMenuKeyboard(update);
            } else if (user.getStartRegistration()) {
                DogRegister(messageText, userDog, chatId, update, user);
//                if (messageText.matches("[А-Я][а-я]+\\s[А-Я][а-я]+")) {
//                    logger.info("пользователь ввел свое имя");
//                    if (userDog == null) {
//                        userDog.setFullName(messageText);
//                        userDogService.updateUserDog(userDog);
//                    }
//                    else {
//                        userCat.setFullName(messageText);
//                        userCat.setUserId(chatId);
//                        userCatService.updateUserCat(userCat);
//                    }
//                    String textMessage = "Введите свой номер телефона, только цифры, например '79000000000'";
//                    sendMessageToUser(chatId, textMessage);
//                } else if (messageText.matches("^\\d{5,15}$")) {
//                    logger.info("пользователь ввел номер телефона");
//                    if (userDog != null) {
//                        userDog.setPhone(Long.valueOf(messageText));
//                        userDogService.updateUserDog(userDog);
//                        String textMessage = "Регистрация успешна!";
//                        user.setStartRegistration(false);
//                        userService.updateUser(user);
//                        sendMessageToUser(chatId, textMessage);
//                        dogMainMenuKeyboard(update);
//                    }
//                    else {
//                        userCat.setPhone(Long.valueOf(messageText));
//                        userCatService.updateUserCat(userCat);
//                        String textMessage = "Регистрация успешна!";
//                        user.setStartRegistration(false);
//                        userService.updateUser(user);
//                        sendMessageToUser(chatId, textMessage);
//                        catMainMenuKeyboard(update);
//                    }
//                }
            } else if (user.getStartReport()) {
                Optional<String> captionText = Optional.ofNullable(update.getMessage().getCaption());
                Optional<List<PhotoSize>> messageImg = Optional.ofNullable(update.getMessage().getPhoto());
                UserReport userReport = new UserReport();

                if (captionText.isEmpty()) {
                    String textMessage = "Напишите текст отчета";
                    sendMessageToUser(chatId, textMessage);
                }

                if (messageImg.isEmpty()) {
                    String textMessage = "Загрузите фото";
                    sendMessageToUser(chatId, textMessage);
                }

                if (captionText.isPresent() && messageImg.isPresent()) {
                    logger.info("Пользователь написал текст и фото отчета");
                    userReport.setReportText(captionText.get());
                    userReport.setUserId(user.getId());
                    userReport.setReportDate(LocalDate.now());
                    UserReport userReport1 = userReportService.updateReport(userReport);
                    Long reportId = userReport1.getId();
                    List<PhotoSize> photos = update.getMessage().getPhoto();
                    String fileId = photos.get(photos.size() - 1).getFileId();

                    URL FILE_GET = null;
                    try {
                        FILE_GET = new URL("https://api.telegram.org/bot" + getBotToken() + "/getFile?file_id=" + fileId);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }

                    JSONObject jsonObject;
                    try {
                        jsonObject = getJson(FILE_GET);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    var result = jsonObject.getJSONObject("result");
                    String path = result.getString("file_path");

                    String FILE_URL = "https://api.telegram.org/file/bot" + getBotToken() + "/" + path;

                    String name = "src/main/resources/report/photo" + reportId + ".jpg";
                    try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream(name)) {
                        byte dataBuffer[] = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    File file = new File(name);
                    FileInputStream input = null;
                    try {
                        input = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    MultipartFile multipartFile;
                    try {
                        multipartFile = new MockMultipartFile("file",
                                file.getName(), "image/jpg", IOUtils.toByteArray(input));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        userReportPhotoService.uploadPhoto(reportId, multipartFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String textMessage = "Спасибо за ваш отчет!";
                    sendMessageToUser(chatId, textMessage);
                    if (userDog != null) {
                        dogMainMenuKeyboard(update);
                    }

                    if (userCat != null) {
                        catMainMenuKeyboard(update);
                    }
                }
            } else {
                sendMessageToUser(chatId, "Некорректныый ввод, попробуйте снова");
            }
        } else if (update.hasCallbackQuery()) {
            checkCallBackQuery(update);
        }

    }

    public static JSONObject getJson(URL url) throws IOException {
        String json = IOUtils.toString(url, Charset.forName("UTF-8"));
        return new JSONObject(json);
    }

    /**
     * Вызов действий после нажатия кнопок в меню
     */
    private void checkCallBackQuery(Update update) {
        String callBackData = update.getCallbackQuery().getData();
        Long userId = update.getCallbackQuery().getMessage().getChatId();

        if (callBackData.equals(CallBackDataEnum.CAT_SHELTER_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку приют для кошек");
//            if (userCatService.findUserCatByUserId(userId).equals(null)) {
//                UserCat userCat = new UserCat();
//                userCat.setUserId(userId);
//                userCatService.updateUserCat(userCat);
//            }
            catMainMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.DOG_SHELTER_BUTTON.getMessage())) {
//            logger.info("пользователь нажал на кнопку приют для собак");
//            if (userDogService.findUserDogByUserId(userId).isEmpty()) {
//                UserDog userDog = new UserDog();
//                userDog.setUserId(userId);
//                userDogService.updateUserDog(userDog);
//            }
            dogMainMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.SHELTER_MENU_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку выхода в меню приюта");
            shelterMenuKeyboard(update);
        }


        if (callBackData.equals(CallBackDataEnum.INFORMATION_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку информации о собачьем приюте");
            dogInformationMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.SHELTER_CAT_INFORMATION_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку информации о кошачьем приюте");
            catInformationMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOW_TAKE_PET_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку как взять собаку из приюта");
            takeTheDogMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOW_TAKE_CAT_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку как взять кошку из приюта");
            takeTheCatMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.SEND_REPORT_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку прислать отчет о собаке");
            sendDogReport(update);
        }

        if (callBackData.equals(CallBackDataEnum.SEND_CAT_REPORT_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку прислать отчет о кошке");
            sendCatReport(update);
        }

        if (callBackData.equals(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку связи с волонтером");
            String textMessage = "В ближайшее время с вами свяжется волонтер";
            sendEditMessageToUser(update, textMessage);
        }

        if (callBackData.equals(CallBackDataEnum.ABOUT_THE_SHELTER.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать о приюте(собачьем)");
            sendShelterInformation(update);
        }

        if (callBackData.equals(CallBackDataEnum.ABOUT_CAT_SHELTER_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать о приюте(кошачьем)");
            sendCatShelterInformation(update);
        }

        if (callBackData.equals(CallBackDataEnum.LOCATION.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать об адресе и режиме работы собачьего приюта");
            sendShelterOpeningHoursAndAddress(update);
        }

        if (callBackData.equals(CallBackDataEnum.CAT_LOCATIONS.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать об адресе и режиме работы кошачьего приюта");
            sendCatShelterOpeningHoursAndAddress(update);
        }

        if (callBackData.equals(CallBackDataEnum.SAFETY_AT_THE_SHELTER.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать о правилах безопасности собачьего приюта");
            sendShelterSafetyRegulations(update);
        }

        if (callBackData.equals(CallBackDataEnum.SHELTER_CAT_SAFETY_REGULATIONS_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку узнать о правилах безопасности кошачьего приюта");
            sendCatShelterSafetyRegulations(update);
        }

        if (callBackData.equals(CallBackDataEnum.BACK_TO_MAIN_MENU.getMessage())) {
            logger.info("пользователь нажал на кнопку назад");
            dogMainMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.BACK_TO_CAT_MAIN_MENU.getMessage())) {
            logger.info("пользователь нажал кнопку назад (кошки)");
            catMainMenuKeyboard(update);
        }

        if (callBackData.equals(CallBackDataEnum.DOG_ACQUAINTANCE_RULES_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку правила знакомства с собакой");
            sendDogAcquaintanceRules(update);
        }

        if (callBackData.equals(CallBackDataEnum.CAT_ACQUAINTANCE_RULES.getMessage())) {
            logger.info("пользователь нажал на кнопку правила знакомства с кошкой");
            sendCatAcquaintanceRules(update);
        }

        if (callBackData.equals(CallBackDataEnum.DOCUMENTS_FOR_DOGS_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список документов, необходимых для того, чтобы взять собаку из приюта");
            sendDocumentsForDogs(update);
        }

        if (callBackData.equals(CallBackDataEnum.DOCUMENTS_FOR_CATS_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список документов, необходимых для того, чтобы взять кошку из приюта");
            sendDocumentsForCats(update);
        }

        if (callBackData.equals(CallBackDataEnum.TRANSPORTATION_ADVICE_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по транспортировке собаки");
            sendTransportationAdvice(update);
        }

        if (callBackData.equals(CallBackDataEnum.TRANSPORTATION_CAT_ADVICE_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по транспортировке кошки");
            sendCatTransportationAdvice(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOME_IMPROVEMENT_FOR_PUPPY_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по обустройству дома для щенка");
            sendHomeImprovementForPuppy(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOME_IMPROVEMENT_FOR_KITTY_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по обустройству дома для котенка");
            sendHomeImprovementForKitty(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_DOG_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по обустройству дома для взрослой собаки");
            sendHomeImprovementForAdultDog(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_CAT_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по обустройству дома для взрослой кошки");
            sendHomeImprovementForAdultCat(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOME_IMPROVEMENT_FOR_DOG_WITH_SPECIAL_NEEDS_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по обустройству дома для собаки с ограниченными возможностями");
            sendHomeImprovementForDogsWithSpecialNeeds(update);
        }

        if (callBackData.equals(CallBackDataEnum.HOME_IMPROVEMENT_FOR_CAT_WITH_SPECIAL_NEEDS_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку список рекомендаций по обустройству дома для кошки с ограниченными возможностями");
            sendHomeImprovementForCatsWithSpecialNeeds(update);
        }

        if (callBackData.equals(CallBackDataEnum.CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку советы кинолога по первичному общению с собакой");
            sendCynologistsAdviceOnInitialCommunication(update);
        }
        if (callBackData.equals(CallBackDataEnum.RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку рекомендаций по проверенным кинологам для дальнейшего обращения к ним.");
            sendRecommendationsForProvenCynologists(update);
        }
        if (callBackData.equals(CallBackDataEnum.CANDIDATE_REGISTRATION_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку записи контактных данных для связи в собачьем приюте");
            User userByChatId = userService.findUserByChatId(update.getCallbackQuery().getFrom().getId()).get();
            userByChatId.setStartRegistration(true);
            userService.updateUser(userByChatId);
            registrationUserDog(update);
        }

        if (callBackData.equals(CallBackDataEnum.CANDIDATE_CAT_REGISTRATION.getMessage())) {
            logger.info("пользователь нажал на кнопку записи контактных данных для связи в кошачьем приюте");
            User userByChatId = userService.findUserByChatId(update.getCallbackQuery().getFrom().getId()).get();
            userByChatId.setStartRegistration(true);
            userService.updateUser(userByChatId);
            registrationUserCat(update);

        }
        if (callBackData.equals(CallBackDataEnum.REASON_FOR_REFUSAL_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку почему могут отказать и не дать забрать собаку из приюта");
            sendReasonForRefusal(update);
        }

        if (callBackData.equals(CallBackDataEnum.REASON_CAT_FOR_REFUSAL_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку почему могут отказать и не дать забрать кошку из приюта");
            sendCatReasonForRefusal(update);
        }

        if (callBackData.equals(CallBackDataEnum.SHElTER_CAT_SECURITY_NUMBER_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку контактные данные охраны для оформления пропуска на машину в кошачий приют");
            sendCatShelterSecurityNumber(update);
        }

        if (callBackData.equals(CallBackDataEnum.SHElTER_DOG_SECURITY_NUMBER_BUTTON.getMessage())) {
            logger.info("пользователь нажал на кнопку контактные данные охраны для оформления пропуска на машину в собачий приют");
            sendDogShelterSecurityNumber(update);
        }
    }

    /**
     * Вывод информации о собачьем приюте
     */
    private void sendShelterInformation(Update update) {
        String informationMessage = ConstantMessageEnum.SHELTER_INFORMATION.getMessage();
        infoWithBackButtonToInformationMenu(update, informationMessage);
    }

    /**
     * Вывод информации о кошачьем приюте
     */
    private void sendCatShelterInformation(Update update) {
        String informationMessage = ConstantCatMessageEnum.SHELTER_CAT_INFORMATION.getMessage();
        infoWithBackButtonToInformationCatMenu(update, informationMessage);
    }

    /**
     * Вывод информации о времени работы и адресе собчачьего приюта
     */
    private void sendShelterOpeningHoursAndAddress(Update update) {
        String openingHoursMessage = ConstantMessageEnum.SHELTER_OPENING_HOURS.getMessage();
        String addressMessage = ConstantMessageEnum.SHELTER_ADDRESS.getMessage();
        infoWithBackButtonToInformationMenu(update, openingHoursMessage + addressMessage);
    }

    /**
     * Вывод информации о времени работы и адресе кошачьего приюта
     */
    private void sendCatShelterOpeningHoursAndAddress(Update update) {
        String openingHoursMessage = ConstantCatMessageEnum.SHELTER_CAT_OPENING_HOURS.getMessage();
        String addressMessage = ConstantCatMessageEnum.SHELTER_CAT_ADDRESS.getMessage();
        infoWithBackButtonToInformationCatMenu(update, openingHoursMessage + addressMessage);
    }

    /**
     * Вывод информации о контактных данных охраны для собачьего приюта
     */
    private void sendDogShelterSecurityNumber(Update update) {
        String securityNumber = ConstantMessageEnum.SHElTER_DOG_SECURITY_NUMBER.getMessage();
        infoWithBackButtonToInformationMenu(update, securityNumber);
    }

    /**
     * Вывод информации о контактных данных охраны для кошачьего приюта
     */
    private void sendCatShelterSecurityNumber(Update update) {
        String securityNumber = ConstantCatMessageEnum.SHElTER_CAT_SECURITY_NUMBER.getMessage();
        infoWithBackButtonToInformationCatMenu(update, securityNumber);
    }

    /**
     * Вывод информации о правилах безопасности собачьего приюта
     */
    private void sendShelterSafetyRegulations(Update update) {
        String safetyRegulationsMessage = ConstantMessageEnum.SHELTER_SAFETY_REGULATIONS.getMessage();
        infoWithBackButtonToInformationMenu(update, safetyRegulationsMessage);
    }

    /**
     * Вывод информации о правилах безопасности кошачьего приюта
     */
    private void sendCatShelterSafetyRegulations(Update update) {
        String safetyRegulationsMessage = ConstantCatMessageEnum.SHELTER_CAT_SAFETY_REGULATIONS.getMessage();
        infoWithBackButtonToInformationCatMenu(update, safetyRegulationsMessage);
    }


    /**
     * Вывод информации о правилах знакомства с собакой
     */
    private void sendDogAcquaintanceRules(Update update) {
        String informationMessage = ConstantMessageEnum.DOG_ACQUAINTANCE_RULES.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод информации о правилах знакомства с кошкой
     */
    private void sendCatAcquaintanceRules(Update update) {
        String informationMessage = ConstantCatMessageEnum.CAT_ACQUAINTANCE_RULES.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод списка документов, необходимых для того, чтобы взять собаку из приюта
     */
    private void sendDocumentsForDogs(Update update) {
        String informationMessage = ConstantMessageEnum.DOCUMENTS_FOR_DOGS.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка документов, необходимых для того, чтобы взять кошку из приюта
     */
    private void sendDocumentsForCats(Update update) {
        String informationMessage = ConstantCatMessageEnum.DOCUMENTS_FOR_CATS.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по транспортировке собаки
     */
    private void sendTransportationAdvice(Update update) {
        String informationMessage = ConstantMessageEnum.TRANSPORTATION_ADVICE.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по транспортировке кошки
     */
    private void sendCatTransportationAdvice(Update update) {
        String informationMessage = ConstantCatMessageEnum.TRANSPORTATION_CAT_ADVICE.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для щенка
     */
    private void sendHomeImprovementForPuppy(Update update) {
        String informationMessage = ConstantMessageEnum.HOME_IMPROVEMENT_FOR_PUPPY.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для котенка
     */
    private void sendHomeImprovementForKitty(Update update) {
        String informationMessage = ConstantCatMessageEnum.HOME_IMPROVEMENT_FOR_KITTY.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }


    /**
     * Вывод списка рекомендаций по обустройству дома для взрослой собаки
     */
    private void sendHomeImprovementForAdultDog(Update update) {
        String informationMessage = ConstantMessageEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_DOG.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для взрослой кошки
     */
    private void sendHomeImprovementForAdultCat(Update update) {
        String informationMessage = ConstantCatMessageEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_CAT.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }


    /**
     * Вывод списка рекомендаций по обустройству дома для собаки с ограниченными возможностями
     */
    private void sendHomeImprovementForDogsWithSpecialNeeds(Update update) {
        String informationMessage = ConstantMessageEnum.HOME_IMPROVEMENT_FOR_DOG_WITH_SPECIAL_NEEDS.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод списка рекомендаций по обустройству дома для кошки с ограниченными возможностями
     */
    private void sendHomeImprovementForCatsWithSpecialNeeds(Update update) {
        String informationMessage = ConstantCatMessageEnum.HOME_IMPROVEMENT_FOR_CAT_WITH_SPECIAL_NEEDS.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }


    /**
     * Вывод советов кинолога по первичному общению с собакой
     */
    private void sendCynologistsAdviceOnInitialCommunication(Update update) {
        String informationMessage = ConstantMessageEnum.CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод рекомендаций по проверенным кинологам для дальнейшего обращения к ним
     */
    private void sendRecommendationsForProvenCynologists(Update update) {
        String informationMessage = ConstantMessageEnum.RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Запись контактных данных для связи
     */
    private void registrationUserDog(Update update) {
        String informationMessage = ConstantMessageEnum.CANDIDATE_REGISTRATION.getMessage();
        infoWithBackButtonToInformationMenu(update, informationMessage);
    }

    /**
     * Запись контактных данных в кошачий приют для связи
     */
    private void registrationUserCat(Update update) {
        String informationMessage = ConstantCatMessageEnum.CANDIDATE_CAT_REGISTRATION.getMessage();
        infoWithBackButtonToInformationCatMenu(update, informationMessage);
    }

    /**
     * Вывод информации почему могут отказать и не дать забрать собаку из приюта
     */
    private void sendReasonForRefusal(Update update) {
        String informationMessage = ConstantMessageEnum.REASON_FOR_REFUSAL.getMessage();
        infoWithBackButtonToTakeTheDogMenu(update, informationMessage);
    }

    /**
     * Вывод информации почему могут отказать и не дать забрать кошку из приюта
     */
    private void sendCatReasonForRefusal(Update update) {
        String informationMessage = ConstantCatMessageEnum.REASON_CAT_FOR_REFUSAL.getMessage();
        infoWithBackButtonToTakeTheCatMenu(update, informationMessage);
    }

    /**
     * Вывод информации по отправке отчета в собачий приют
     */
    private void sendDogReport(Update update) {
        String informationMessage = ConstantMessageEnum.DOG_REPORT.getMessage();
        User userByChatId = userService.findUserByChatId(update.getCallbackQuery().getFrom().getId()).get();
        userByChatId.setStartReport(true);
        userService.updateUser(userByChatId);
        infoWithBackButtonToDogMainMenu(update, informationMessage);
    }

    /**
     * Вывод информации по отправке отчета в кошачий приют
     */
    private void sendCatReport(Update update) {
        String informationMessage = ConstantCatMessageEnum.CAT_REPORT.getMessage();
        User userByChatId = userService.findUserByChatId(update.getCallbackQuery().getFrom().getId()).get();
        userByChatId.setStartReport(true);
        userService.updateUser(userByChatId);
        infoWithBackButtonToCatMainMenu(update, informationMessage);
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
     * Вывод меню с выбором приюта
     */
    private void shelterMenuKeyboard(Update update) {
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


        var informationButton = new InlineKeyboardButton();
        informationButton.setText("Приют для кошек");
        informationButton.setCallbackData(CallBackDataEnum.CAT_SHELTER_BUTTON.getMessage());
        rowFirst.add(informationButton);

        var howTakePetButton = new InlineKeyboardButton();
        howTakePetButton.setText("Приют для собак");
        howTakePetButton.setCallbackData(CallBackDataEnum.DOG_SHELTER_BUTTON.getMessage());
        rowSecond.add(howTakePetButton);

        rows.add(rowFirst);
        rows.add(rowSecond);

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
     * Вывод главного меню для кошек с вопросами под сообщением после выбора приюта
     */
    private void catMainMenuKeyboard(Update update) {
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
        List<InlineKeyboardButton> rowFifth = new ArrayList<>();

        var informationButton = new InlineKeyboardButton();
        informationButton.setText("Информация о приюте");
        informationButton.setCallbackData(CallBackDataEnum.SHELTER_CAT_INFORMATION_BUTTON.getMessage());
        rowFirst.add(informationButton);

        var howTakePetButton = new InlineKeyboardButton();
        howTakePetButton.setText("Как взять кошку из приюта");
        howTakePetButton.setCallbackData(CallBackDataEnum.HOW_TAKE_CAT_BUTTON.getMessage());
        rowSecond.add(howTakePetButton);

        var sendReportButton = new InlineKeyboardButton();
        sendReportButton.setText("Прислать отчет о питомце");
        sendReportButton.setCallbackData(CallBackDataEnum.SEND_CAT_REPORT_BUTTON.getMessage());
        rowThird.add(sendReportButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowForth.add(callVolunteerButton);

        var shelterMenuButton = new InlineKeyboardButton();
        shelterMenuButton.setText("Назад");
        shelterMenuButton.setCallbackData(CallBackDataEnum.SHELTER_MENU_BUTTON.getMessage());
        rowFifth.add(shelterMenuButton);

        rows.add(rowFirst);
        rows.add(rowSecond);
        rows.add(rowThird);
        rows.add(rowForth);
        rows.add(rowFifth);

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
     * Вывод главного меню для собак с вопросами под сообщением после выбора приюта
     */
    private void dogMainMenuKeyboard(Update update) {
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
        List<InlineKeyboardButton> rowFifth = new ArrayList<>();

        var informationButton = new InlineKeyboardButton();
        informationButton.setText("Информация о приюте");
        informationButton.setCallbackData(CallBackDataEnum.INFORMATION_BUTTON.getMessage());
        rowFirst.add(informationButton);

        var howTakePetButton = new InlineKeyboardButton();
        howTakePetButton.setText("Как взять собаку из приюта");
        howTakePetButton.setCallbackData(CallBackDataEnum.HOW_TAKE_PET_BUTTON.getMessage());
        rowSecond.add(howTakePetButton);

        var sendReportButton = new InlineKeyboardButton();
        sendReportButton.setText("Прислать отчет о питомце");
        sendReportButton.setCallbackData(CallBackDataEnum.SEND_REPORT_BUTTON.getMessage());
        rowThird.add(sendReportButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowForth.add(callVolunteerButton);

        var shelterMenuButton = new InlineKeyboardButton();
        shelterMenuButton.setText("Назад");
        shelterMenuButton.setCallbackData(CallBackDataEnum.SHELTER_MENU_BUTTON.getMessage());
        rowFifth.add(shelterMenuButton);

        rows.add(rowFirst);
        rows.add(rowSecond);
        rows.add(rowThird);
        rows.add(rowForth);
        rows.add(rowFifth);

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
    private void dogInformationMenuKeyboard(Update update) {
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
        List<InlineKeyboardButton> rowSeventh = new ArrayList<>();

        var aboutTheShelterButton = new InlineKeyboardButton();
        aboutTheShelterButton.setText("О приюте");
        aboutTheShelterButton.setCallbackData(CallBackDataEnum.ABOUT_THE_SHELTER.getMessage());
        rowFirst.add(aboutTheShelterButton);

        var location = new InlineKeyboardButton();
        location.setText("Адрес, схема проезда,\n режим работы");
        location.setCallbackData(CallBackDataEnum.LOCATION.getMessage());
        rowSecond.add(location);

        var safetyAtTheShelter = new InlineKeyboardButton();
        safetyAtTheShelter.setText("ТБ на территории приюта");
        safetyAtTheShelter.setCallbackData(CallBackDataEnum.SAFETY_AT_THE_SHELTER.getMessage());
        rowThird.add(safetyAtTheShelter);

        var candidateRegistrationButton = new InlineKeyboardButton();
        candidateRegistrationButton.setText("Запись контактных данных");
        candidateRegistrationButton.setCallbackData(CallBackDataEnum.CANDIDATE_REGISTRATION_BUTTON.getMessage());
        rowFourth.add(candidateRegistrationButton);

        var shelterDogSecurityNumberButton = new InlineKeyboardButton();
        shelterDogSecurityNumberButton.setText("Контактные данные охраны для оформления пропуска на машину");
        shelterDogSecurityNumberButton.setCallbackData(CallBackDataEnum.SHElTER_DOG_SECURITY_NUMBER_BUTTON.getMessage());
        rowFifth.add(shelterDogSecurityNumberButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowSixth.add(callVolunteerButton);

        var backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Назад");
        backToMainMenu.setCallbackData(CallBackDataEnum.BACK_TO_MAIN_MENU.getMessage());
        rowSeventh.add(backToMainMenu);

        informationMenuRows.add(rowFirst);
        informationMenuRows.add(rowSecond);
        informationMenuRows.add(rowThird);
        informationMenuRows.add(rowFourth);
        informationMenuRows.add(rowFifth);
        informationMenuRows.add(rowSixth);
        informationMenuRows.add(rowSeventh);

        informationMenuKeyboard.setKeyboard(informationMenuRows);

        message.setReplyMarkup(informationMenuKeyboard);
        executeEditMessage(message);
    }

    /**
     * Вывод меню кошчьего приюта с вопросами под сообщением после нажатия кнопки меню "Информация о приюте"
     */
    private void catInformationMenuKeyboard(Update update) {
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
        List<InlineKeyboardButton> rowSeventh = new ArrayList<>();


        var aboutTheShelterButton = new InlineKeyboardButton();
        aboutTheShelterButton.setText("О приюте");
        aboutTheShelterButton.setCallbackData(CallBackDataEnum.ABOUT_CAT_SHELTER_BUTTON.getMessage());
        rowFirst.add(aboutTheShelterButton);

        var location = new InlineKeyboardButton();
        location.setText("Адрес, схема проезда,\n режим работы");
        location.setCallbackData(CallBackDataEnum.CAT_LOCATIONS.getMessage());
        rowSecond.add(location);

        var safetyAtTheShelter = new InlineKeyboardButton();
        safetyAtTheShelter.setText("ТБ на территории приюта");
        safetyAtTheShelter.setCallbackData(CallBackDataEnum.SHELTER_CAT_SAFETY_REGULATIONS_BUTTON.getMessage());
        rowThird.add(safetyAtTheShelter);

        var candidateRegistrationButton = new InlineKeyboardButton();
        candidateRegistrationButton.setText("Запись контактных данных");
        candidateRegistrationButton.setCallbackData(CallBackDataEnum.CANDIDATE_CAT_REGISTRATION.getMessage());
        rowFourth.add(candidateRegistrationButton);

        var shelterCatSecurityNumberButton = new InlineKeyboardButton();
        shelterCatSecurityNumberButton.setText("Контактные данные охраны для оформления пропуска на машину");
        shelterCatSecurityNumberButton.setCallbackData(CallBackDataEnum.SHElTER_CAT_SECURITY_NUMBER_BUTTON.getMessage());
        rowFifth.add(shelterCatSecurityNumberButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowSixth.add(callVolunteerButton);

        var backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Назад");
        backToMainMenu.setCallbackData(CallBackDataEnum.BACK_TO_CAT_MAIN_MENU.getMessage());
        rowSeventh.add(backToMainMenu);

        informationMenuRows.add(rowFirst);
        informationMenuRows.add(rowSecond);
        informationMenuRows.add(rowThird);
        informationMenuRows.add(rowFourth);
        informationMenuRows.add(rowFifth);
        informationMenuRows.add(rowSixth);
        informationMenuRows.add(rowSeventh);

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
        puppyDatingRulesButton.setCallbackData(CallBackDataEnum.DOG_ACQUAINTANCE_RULES_BUTTON.getMessage());
        rowFirst.add(puppyDatingRulesButton);

        var documentsForDogsButton = new InlineKeyboardButton();
        documentsForDogsButton.setText("Список документов, чтобы взять собаку из приюта");
        documentsForDogsButton.setCallbackData(CallBackDataEnum.DOCUMENTS_FOR_DOGS_BUTTON.getMessage());
        rowSecond.add(documentsForDogsButton);

        var transportationAdviceButton = new InlineKeyboardButton();
        transportationAdviceButton.setText("Рекомендации по транспортировке животного");
        transportationAdviceButton.setCallbackData(CallBackDataEnum.TRANSPORTATION_ADVICE_BUTTON.getMessage());
        rowThird.add(transportationAdviceButton);

        var homeImprovementForPuppyButton = new InlineKeyboardButton();
        homeImprovementForPuppyButton.setText("Обустройство дома для щенка");
        homeImprovementForPuppyButton.setCallbackData(CallBackDataEnum.HOME_IMPROVEMENT_FOR_PUPPY_BUTTON.getMessage());
        rowFourth.add(homeImprovementForPuppyButton);

        var homeImprovementForAnAdultDogButton = new InlineKeyboardButton();
        homeImprovementForAnAdultDogButton.setText("Обустройство дома для взрослой собаки");
        homeImprovementForAnAdultDogButton.setCallbackData(CallBackDataEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_DOG_BUTTON.getMessage());
        rowFifth.add(homeImprovementForAnAdultDogButton);

        var homeImprovementForDogWithDisability = new InlineKeyboardButton();
        homeImprovementForDogWithDisability.setText("Обустройство дома для собаки с ограниченными возможностями");
        homeImprovementForDogWithDisability.setCallbackData(CallBackDataEnum.HOME_IMPROVEMENT_FOR_DOG_WITH_SPECIAL_NEEDS_BUTTON.getMessage());
        rowSixth.add(homeImprovementForDogWithDisability);

        var cynologistsAdviceOnInitialCommunicationButton = new InlineKeyboardButton();
        cynologistsAdviceOnInitialCommunicationButton.setText("Советы кинолога по первичному общению с собакой");
        cynologistsAdviceOnInitialCommunicationButton.setCallbackData(CallBackDataEnum.CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION_BUTTON.getMessage());
        rowSeventh.add(cynologistsAdviceOnInitialCommunicationButton);

        var recommendationsForProvenCynologistsButton = new InlineKeyboardButton();
        recommendationsForProvenCynologistsButton.setText("Рекомендации по проверенным кинологам");
        recommendationsForProvenCynologistsButton.setCallbackData(CallBackDataEnum.RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS_BUTTON.getMessage());
        rowEighth.add(recommendationsForProvenCynologistsButton);

        var reasonsForRefusalButton = new InlineKeyboardButton();
        reasonsForRefusalButton.setText("Список причин, почему могут отказать");
        reasonsForRefusalButton.setCallbackData(CallBackDataEnum.REASON_FOR_REFUSAL_BUTTON.getMessage());
        rowNinth.add(reasonsForRefusalButton);

        var candidateRegistrationButton = new InlineKeyboardButton();
        candidateRegistrationButton.setText("Запись контактных данных");
        candidateRegistrationButton.setCallbackData(CallBackDataEnum.CANDIDATE_REGISTRATION_BUTTON.getMessage());
        rowTenth.add(candidateRegistrationButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowEleventh.add(callVolunteerButton);

        var backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Назад");
        backToMainMenu.setCallbackData(CallBackDataEnum.BACK_TO_MAIN_MENU.getMessage());
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
     * Вывод меню с вопросами под сообщением после нажатия кнопки меню "Как взять кошку из приюта"
     */
    private void takeTheCatMenuKeyboard(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(ConstantMessageEnum.SELECT_REQUEST.getMessage());
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup takeCatMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> CatMenuRows = new ArrayList<>();

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

        var puppyDatingRulesButton = new InlineKeyboardButton();
        puppyDatingRulesButton.setText("Правила знакомства с кошку");
        puppyDatingRulesButton.setCallbackData(CallBackDataEnum.CAT_ACQUAINTANCE_RULES.getMessage());
        rowFirst.add(puppyDatingRulesButton);

        var documentsForDogsButton = new InlineKeyboardButton();
        documentsForDogsButton.setText("Список документов, чтобы взять собаку из приюта");
        documentsForDogsButton.setCallbackData(CallBackDataEnum.DOCUMENTS_FOR_CATS_BUTTON.getMessage());
        rowSecond.add(documentsForDogsButton);

        var transportationAdviceButton = new InlineKeyboardButton();
        transportationAdviceButton.setText("Рекомендации по транспортировке животного");
        transportationAdviceButton.setCallbackData(CallBackDataEnum.TRANSPORTATION_CAT_ADVICE_BUTTON.getMessage());
        rowThird.add(transportationAdviceButton);

        var homeImprovementForPuppyButton = new InlineKeyboardButton();
        homeImprovementForPuppyButton.setText("Обустройство дома для котенка");
        homeImprovementForPuppyButton.setCallbackData(CallBackDataEnum.HOME_IMPROVEMENT_FOR_KITTY_BUTTON.getMessage());
        rowFourth.add(homeImprovementForPuppyButton);

        var homeImprovementForAnAdultDogButton = new InlineKeyboardButton();
        homeImprovementForAnAdultDogButton.setText("Обустройство дома для взрослой кошки");
        homeImprovementForAnAdultDogButton.setCallbackData(CallBackDataEnum.HOME_IMPROVEMENT_FOR_AN_ADULT_CAT_BUTTON.getMessage());
        rowFifth.add(homeImprovementForAnAdultDogButton);

        var homeImprovementForDogWithDisability = new InlineKeyboardButton();
        homeImprovementForDogWithDisability.setText("Обустройство дома для кошки с ограниченными возможностями");
        homeImprovementForDogWithDisability.setCallbackData(CallBackDataEnum.HOME_IMPROVEMENT_FOR_CAT_WITH_SPECIAL_NEEDS_BUTTON.getMessage());
        rowSixth.add(homeImprovementForDogWithDisability);

        var reasonsForRefusalButton = new InlineKeyboardButton();
        reasonsForRefusalButton.setText("Список причин, почему могут отказать");
        reasonsForRefusalButton.setCallbackData(CallBackDataEnum.REASON_CAT_FOR_REFUSAL_BUTTON.getMessage());
        rowSeventh.add(reasonsForRefusalButton);

        var candidateRegistrationButton = new InlineKeyboardButton();
        candidateRegistrationButton.setText("Запись контактных данных");
        candidateRegistrationButton.setCallbackData(CallBackDataEnum.CANDIDATE_CAT_REGISTRATION.getMessage());
        rowEighth.add(candidateRegistrationButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowNinth.add(callVolunteerButton);

        var backToMainMenu = new InlineKeyboardButton();
        backToMainMenu.setText("Назад");
        backToMainMenu.setCallbackData(CallBackDataEnum.BACK_TO_CAT_MAIN_MENU.getMessage());
        rowTenth.add(backToMainMenu);

        CatMenuRows.add(rowFirst);
        CatMenuRows.add(rowSecond);
        CatMenuRows.add(rowThird);
        CatMenuRows.add(rowFourth);
        CatMenuRows.add(rowFifth);
        CatMenuRows.add(rowSixth);
        CatMenuRows.add(rowSeventh);
        CatMenuRows.add(rowEighth);
        CatMenuRows.add(rowNinth);
        CatMenuRows.add(rowTenth);

        takeCatMenuKeyboard.setKeyboard(CatMenuRows);

        message.setReplyMarkup(takeCatMenuKeyboard);
        executeEditMessage(message);
    }

    /**
     * Вывод кнопки назад в меню "Информация о приюте"(для собак)
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
        backToInformationMenu.setCallbackData(CallBackDataEnum.INFORMATION_BUTTON.getMessage());
        rowOne.add(backToInformationMenu);

        informationMenuRows.add(rowOne);

        informationMenuKeyboard.setKeyboard(informationMenuRows);

        message.setReplyMarkup(informationMenuKeyboard);
        executeEditMessage(message);
    }

    /**
     * Вывод кнопки назад в меню "Информация о приюте"(для кошек)
     */
    private void infoWithBackButtonToInformationCatMenu(Update update, String text) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup catInformationMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> catInformationMenuRows = new ArrayList<>();
        List<InlineKeyboardButton> rowOne = new ArrayList<>();

        var backToCatInformationMenu = new InlineKeyboardButton();
        backToCatInformationMenu.setText("Назад");
        backToCatInformationMenu.setCallbackData(CallBackDataEnum.SHELTER_CAT_INFORMATION_BUTTON.getMessage());
        rowOne.add(backToCatInformationMenu);

        catInformationMenuRows.add(rowOne);

        catInformationMenuKeyboard.setKeyboard(catInformationMenuRows);

        message.setReplyMarkup(catInformationMenuKeyboard);
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
        backToTakeDogMenu.setCallbackData(CallBackDataEnum.HOW_TAKE_PET_BUTTON.getMessage());
        rowOne.add(backToTakeDogMenu);

        takeDogMenuRows.add(rowOne);

        takeDogMenuKeyboard.setKeyboard(takeDogMenuRows);

        message.setReplyMarkup(takeDogMenuKeyboard);
        executeEditMessage(message);
    }

    /**
     * Вывод кнопки назад в меню "Как взять кошку из приюта"
     */
    private void infoWithBackButtonToTakeTheCatMenu(Update update, String text) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup takeCatMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> takeCatMenuRows = new ArrayList<>();
        List<InlineKeyboardButton> rowOne = new ArrayList<>();

        var backToTakeDogMenu = new InlineKeyboardButton();
        backToTakeDogMenu.setText("Назад");
        backToTakeDogMenu.setCallbackData(CallBackDataEnum.HOW_TAKE_CAT_BUTTON.getMessage());
        rowOne.add(backToTakeDogMenu);

        takeCatMenuRows.add(rowOne);

        takeCatMenuKeyboard.setKeyboard(takeCatMenuRows);

        message.setReplyMarkup(takeCatMenuKeyboard);
        executeEditMessage(message);
    }

    /**
     * Вывод кнопки назад в меню c запросами в собачьем приюте
     */
    private void infoWithBackButtonToDogMainMenu(Update update, String text) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup DogMainMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> DogMainMenuRows = new ArrayList<>();
        List<InlineKeyboardButton> rowFirst = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        var backToTakeDogMenu = new InlineKeyboardButton();
        backToTakeDogMenu.setText("Назад");
        backToTakeDogMenu.setCallbackData(CallBackDataEnum.DOG_SHELTER_BUTTON.getMessage());
        rowFirst.add(backToTakeDogMenu);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowSecond.add(callVolunteerButton);


        DogMainMenuRows.add(rowFirst);
        DogMainMenuRows.add(rowSecond);

        DogMainMenuKeyboard.setKeyboard(DogMainMenuRows);

        message.setReplyMarkup(DogMainMenuKeyboard);
        executeEditMessage(message);
    }

    private void infoWithBackButtonToCatMainMenu(Update update, String text) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup CatMainMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> CatMainMenuRows = new ArrayList<>();
        List<InlineKeyboardButton> rowFirst = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        var backToTakeDogMenu = new InlineKeyboardButton();
        backToTakeDogMenu.setText("Назад");
        backToTakeDogMenu.setCallbackData(CallBackDataEnum.CAT_SHELTER_BUTTON.getMessage());
        rowFirst.add(backToTakeDogMenu);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CallBackDataEnum.CALL_VOLUNTEER_BUTTON.getMessage());
        rowSecond.add(callVolunteerButton);


        CatMainMenuRows.add(rowFirst);
        CatMainMenuRows.add(rowSecond);

        CatMainMenuKeyboard.setKeyboard(CatMainMenuRows);

        message.setReplyMarkup(CatMainMenuKeyboard);
        executeEditMessage(message);
    }
}