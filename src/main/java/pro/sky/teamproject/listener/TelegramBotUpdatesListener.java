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

import java.util.ArrayList;
import java.util.List;


@Service
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {

    static final String GREETINGS_TEXT = "Приветствую ";

    static final String BOT_INFORMATION = "Я бот помошник приюта животных из Астаны." +
            "\n Здесь ты найдешь информацию о том, что нужно знать " +
            "\n и уметь, чтобы забрать животное из приюта." +
            "\n Также здесь ты можешь отправлять ежедневный отчет о том, " +
            "\n как животное приспосабливается к новой обстановке.";

    static final String SELECT_REQUEST = "Выберите что вас интересует";

    static final String INFORMATION_BUTTON = "informationButton";

    static final String HOW_TAKE_PET_BUTTON = "howTakePetButton";

    static final String SEND_REPORT_BUTTON = "sendReportButton";

    static final String CALL_VOLUNTEER_BUTTON = "callVolunteerButton";


    final TelegramBotConfiguration configuration;

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public TelegramBotUpdatesListener(TelegramBotConfiguration configuration) {
        this.configuration = configuration;
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
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    sendGreetings(chatId, update.getMessage().getChat().getFirstName());
                    sendMessageToUser(chatId, BOT_INFORMATION);
                    mainMenuKeyboard(chatId);
                    break;
                default:
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
        String callBackQData = update.getCallbackQuery().getData();
        if (callBackQData.equals(INFORMATION_BUTTON)) {
            String textMessage = "Вывод меню 1-го. этапа - в разработке";
            sendEditMessageToUser(update, textMessage);
        }
        if (callBackQData.equals(HOW_TAKE_PET_BUTTON)) {
            String textMessage = "Вывод меню 2-го. этапа - в разработке";
            sendEditMessageToUser(update, textMessage);
        }
        if (callBackQData.equals(SEND_REPORT_BUTTON)) {
            String textMessage = "Вывод меню 3-го. этапа - в разработке";
            sendEditMessageToUser(update, textMessage);
        }
        if (callBackQData.equals(CALL_VOLUNTEER_BUTTON)) {
            String textMessage = "Действия при вызове волонтера - в разработке";
            sendEditMessageToUser(update, textMessage);
        }
    }

    /**
     * Вывод приветственного сообщения по команде /start
     */
    private void sendGreetings(long chatId, String name) {
        logger.info("Method sendWelcome started");
        String textMessage = GREETINGS_TEXT + name;
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
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText message = new EditMessageText();
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
     * Вывод главного меню с вопросами под сообщением после вызова команды /start
     */
    private void mainMenuKeyboard(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(SELECT_REQUEST);

        InlineKeyboardMarkup mainMenuKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> rowFirst = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();
        List<InlineKeyboardButton> rowThird = new ArrayList<>();
        List<InlineKeyboardButton> rowForth = new ArrayList<>();

        var informationButton = new InlineKeyboardButton();
        informationButton.setText("Информация о приюте");
        informationButton.setCallbackData(INFORMATION_BUTTON);
        rowFirst.add(informationButton);

        var howTakePetButton = new InlineKeyboardButton();
        howTakePetButton.setText("Как взять собаку из приюта");
        howTakePetButton.setCallbackData(HOW_TAKE_PET_BUTTON);
        rowSecond.add(howTakePetButton);

        var sendReportButton = new InlineKeyboardButton();
        sendReportButton.setText("Прислать отчет о питомце");
        sendReportButton.setCallbackData(SEND_REPORT_BUTTON);
        rowThird.add(sendReportButton);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CALL_VOLUNTEER_BUTTON);
        rowForth.add(callVolunteerButton);

        rows.add(rowFirst);
        rows.add(rowSecond);
        rows.add(rowThird);
        rows.add(rowForth);

        mainMenuKeyboard.setKeyboard(rows);

        message.setReplyMarkup(mainMenuKeyboard);
        executeMessage(message);
    }
}
