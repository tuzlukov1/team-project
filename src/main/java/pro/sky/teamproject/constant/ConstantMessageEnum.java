package pro.sky.teamproject.constant;

public enum ConstantMessageEnum {

    /**
     * Текст выводимых ботом приветственных и информационных сообщений
     */
    GREETINGS_TEXT("Приветствую "),
    BOT_INFORMATION(" Я бот-помощник приюта животных из Астаны." +
            "\n Здесь ты найдешь информацию о том, что нужно знать " +
            "\n и уметь, чтобы забрать животное из приюта." +
            "\n Также здесь ты можешь отправлять ежедневный отчет о том, " +
            "\n как животное приспосабливается к новой обстановке."),
    SELECT_REQUEST("Выберите интересующий пункт меню "),
    WARNING("Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо." +
            " Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны" +
            " самолично проверять условия содержания животного."),

    SHELTER_INFORMATION(" Наш приют была основан в Астане, Казахстан, двумя зооэнтузиастами и педагогами, которые увидели необходимость обучения животных в регионе." +
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
            "\n https://goo.by/bROaB" + "\n"),

    SHElTER_DOG_SECURITY_NUMBER("Контактные данные охраны для оформления пропуска на машину - +7-900-000-00-00, Евлампиевич Михаил"),


    SHELTER_SAFETY_REGULATIONS(" Правила безопасности:" +
            "\n- Своей едой животных не кормить" +
            "\n- Животных не провоцировать" +
            "\n- Давать только ту еду, что прописана в рационе животного"),

    DOG_ACQUAINTANCE_RULES("Правила знакомства с собакой: " +
            "\n☑ Избегайте взгляда глаза в глаза." +
            "\n☑ Можете погладить по бокам, щечкам, груди, если она не сопротивляется — по спине." +
            "\n☑ Если волонтер приюта не против, можете угостить лакомством на открытой ладони." +
            "\nВ целом, знакомьтесь с собакой так, как вы бы хотели, что бы с вами знакомились на улице другие люди."),
    REASON_FOR_REFUSAL("Почему вам могут отказать в опекунстве над животным?" +
            "\n1 Большое количество животных дома" +
            "\n2 Нестабильные отношения в семье" +
            "\n3 Наличие маленьких детей" +
            "\n4 Съемное жилье" +
            "\n5 Животное в подарок или для работы"),
    CANDIDATE_REGISTRATION("Оставьте свои контактные данные для связи и мы с вами свяжемся!" +
            "\n Введите свое имя:"),
    RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS("Рекомендуем вам обращаться к нашим проверенным кинологам по любым вопросам после взятия животного из нашего приюта" +
            "+71722333133 Анна, +71722331133 Арман"),
    CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION("Как подготовиться владельцу?\n" +
            "Ухаживать за щенком - задача не из легких. Малышу потребуется много вашего внимания, ухода и заботы. В связи с этим лучше, чтобы первое время со щенком постоянно рядом находился человек, ведь собаке надо привыкнуть к новому дому.\n" +
            "\n" +
            "Также нужно следить за безопасностью малыша, ведь, забывшись вы можете случайно придавить его дверью или наступить на хвостик, будьте предельно внимательны.\n" +
            "\n" +
            "Первые несколько дней малыш может скулить, это нормальное поведение для собаки, оказавшейся в непривычных условиях, с новыми владельцами, вдали от мамы. Нужно потерпеть, через несколько дней щенок успокоится.\n" +
            "\n" +
            "Также вам нужно решить заранее, где щенок будет есть, где спать, а где ходить в туалет."),
    HOME_IMPROVEMENT_FOR_DOG_WITH_SPECIAL_NEEDS("Как общаться и дрессировать собак с ограниченными возможностями здоровья\n" +
            "\nКак общаться и дрессировать собак с ограниченными возможностями здоровья\n" +
            "\nБывают ситуации, когда из-за врожденных особенностей, болезни или травмы, собака становится инвалидом." +
            "\nМожет показаться, что жизнь такого питомца будет полна страданий, но это заблуждение." +
            "\nЕсли собака не испытывает болей, а хозяин готов ухаживать и помогать собаке адаптироваться" +
            "\nк новой жизни, то она как правило и не замечет неудобств, связанных с особенностями ее здоровья." +
            "\nК таким животным нужен особенный подход. Рассказываем, как общаться и дрессировать собак с ограниченными возможностями." +
            "\nЧитать дальше shorturl.at/lRX25"),
    HOME_IMPROVEMENT_FOR_AN_ADULT_DOG("Еще немного и в вашем доме появится собака, которую вы так долго ждали. Думаю, волосы уже дыбом!\n" +
            "Куда идти? Что делать? Как сделать территорию безопасной? Давайте все по порядку!\n" +
            "\n" +
            "Взгляните на свою квартиру глазами собаки: что можно достать, что отодрать, что порвать, где стащить, куда достать, почему торчит. И мы начинаем делать «безопасную зону». \n" +
            "убираем провода\n" +
            "поднимаем предметы, которые можно случайно задеть и разбить\n" +
            "если у вас стоят на полу цветы, закройте почву специальной сеткой\n" +
            "книги, журналы убирайте в шкаф\n" +
            "если вдруг у вас где-то есть кусок обоев, что вот-вот отвалится, я советую его приклеить покрепче"),
    HOME_IMPROVEMENT_FOR_PUPPY("Как подготовиться к появлению щенка в семье\n" +
            "Выбрать питомца по своему темпераменту и рекомендациям кинолога.\n" +
            "Приготовить безопасное и спокойное место для щенка, убрать лишние вещи.\n" +
            "Разместить на месте для щенка лежанку и впитывающие пелёнки.\n" +
            "Купить две миски — для еды и воды.\n" +
            "Подобрать ошейник и поводок для прогулок."),
    TRANSPORTATION_ADVICE("Как оборудовать место для перевозки\n" +
            "При размещении собаки в личном автомобиле придерживайтесь базовых правил безопасности.\n" +
            "\nCобака в машине\n" +
            "\nЕсли собака крупная, посадите ее в багажное отделение или в пространство между передним и задним рядом кресел." +
            "\nВ первом случае понадобится железная решетка-перегородка, отделяющая салон от багажника, во втором – прочная клетка-перевозка. Размещение в багажнике наиболее безопасное. В этом положении собака будет надежно зафиксирована и не вылетит из салона через лобовое стекло в случае резкого торможения или удара.\n" +
            "\nЕсли собака средняя или мелкая, посадите ее на заднее кресло, пристегнув к штатному ремню безопасности специальной шлейкой с креплением-адаптером. Такие есть в каталогах фирменных аксессуаров многих автомобилей. Использовать пластиковые или тканевые переноски тоже допускается. Но зафиксировать их на случай удара будет труднее. Да и на подобную нагрузку они не рассчитаны.\n" +
            "\nТребования к месту для перевозки собак:\n" +
            "\nОграничьте животное в перемещении по салону. Проследите, чтобы собака не получила доступ к органам управления автомобилем. Чем меньше животное двигается, тем спокойнее и безопаснее поездка.\n" +
            "Обеспечьте упор для лап. Позаботьтесь, чтобы под лапами был противоскользящий настил, который обеспечит упор, если собака захочет встать.\n" +
            "Проверьте надежность автомобильных аксессуаров для перевозки. При выборе специального оборудования смотрите независимые краш-тесты. Если используете защитные чехлы или гамаки на заднее сиденье, убедитесь, что они не препятствуют фиксации животного ремнем безопасности."),
    DOCUMENTS_FOR_DOGS("Для получения разрешения на взятие питомца из нашего приюта, возьмите пожалуйста свой паспорт или водительские права"),
    DOG_REPORT("Пришлите, пожалуйста, отчет в следующем формате:\n" +
            "\n- Фото животного\n" +
            "\n- Рацион животного\n" +
            "\n- Общее самочувствие и привыкание к новому месту\n" +
            "\n- Изменение в поведении: отказ от старых привычек, приобретение новых.\n");

    private final String message;

    ConstantMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
