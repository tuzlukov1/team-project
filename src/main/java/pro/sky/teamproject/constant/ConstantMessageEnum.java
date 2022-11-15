package pro.sky.teamproject.constant;

public enum ConstantMessageEnum {

    /**
     * Текст выводимых ботом приветственных и информационных сообщений
     */
    GREETINGS_TEXT("Приветствую "),
    BOT_INFORMATION("Я бот помошник приюта животных из Астаны." +
            "\n Здесь ты найдешь информацию о том, что нужно знать " +
            "\n и уметь, чтобы забрать животное из приюта." +
            "\n Также здесь ты можешь отправлять ежедневный отчет о том, " +
            "\n как животное приспосабливается к новой обстановке."),
    SELECT_REQUEST("Выберите что вас интересует"),

    /**
     * Возвращаемое значение "callBackData" при нажатии кнопок меню.
     */
    INFORMATION_BUTTON("informationButton"),
    HOW_TAKE_PET_BUTTON("howTakePetButton"),
    SEND_REPORT_BUTTON("sendReportButton"),
    CALL_VOLUNTEER_BUTTON("callVolunteerButton"),
    ABOUT_THE_SHELTER("aboutTheShelterButton"),
    LOCATION("location"),
    SAFETY_AT_THE_SHELTER("safetyAtTheShelter"),
    REGISTRATION("registration"),
    BACK_TO_MAIN_MENU("backToMainMenu"),
    ;

    private final String message;

    ConstantMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
