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

    SHELTER_INFORMATION("Наш приют была основан в Астане, Казахстан, двумя зооэнтузиастами и педагогами, которые увидели необходимость обучения животных в регионе." +
            "\n" +
            "\nМы стремимся информировать и просвещать людей о жестоком обращении с животными, происходящем в их сообществах." +
            "\n" +
            "\nПрограмма предназначена для молодежи с целью информирования их о гуманном воспитании животных." +
            "\n" +
            "\nНаш приют служит обществу, обеспечивая безопасность, лечение и реабилитацию, чтобы помочь бездомным собакам и кошкам стать усыновленными." +
            "\n" +
            "\nМы также работаем с партнерами и ветеринарной промышленностью над улучшением ухода за животными-компаньонами." + "\n"),

    SHELTER_OPENING_HOURS("Время работы:" +
            "\nПн - Пт 9:00 - 18:00" +
            "\nСб - Вс 10:00 - 16:00" + "\n"),
    SHELTER_ADDRESS("Наш адрес на карте: " +
            "\n https://www.google.com/maps/place/PAWS+for+Compassion/@16.0437797,108.2431628,15z/data=!4m5!3m4!1s0x0:0xd2216351d7864319!8m2!3d16.0437797!4d108.2431628" + "\n"),

    SHELTER_SAFETY_REGULATIONS("Правила безопасности:" +
            "\n- Своей едой животных не кормить" +
            "\n- Животных не провоцировать" +
            "\n- Давать только ту еду, что прописана в рационе животного"),
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
