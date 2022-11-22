package pro.sky.teamproject.constant;

public enum CallBackDataEnum {

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
    BACK_TO_MAIN_MENU("backToMainMenu"),

    DOG_ACQUAINTANCE_RULES_BUTTON("puppyDatingRulesButton"),
    DOCUMENTS_FOR_DOGS_BUTTON("documentsForDogsButton"),
    TRANSPORTATION_ADVICE_BUTTON("transportationAdviceButton"),
    HOME_IMPROVEMENT_FOR_PUPPY_BUTTON("homeImprovementForPuppyButton"),
    HOME_IMPROVEMENT_FOR_AN_ADULT_DOG_BUTTON("homeImprovementForAnAdultDogButton"),
    HOME_IMPROVEMENT_FOR_DOG_WITH_SPECIAL_NEEDS_BUTTON("homeImprovementForDogWithDisability"),
    CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION_BUTTON("cynologistsAdviceOnInitialCommunicationButton"),
    RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS_BUTTON("recommendationsForProvenCynologistsButton"),
    REASON_FOR_REFUSAL_BUTTON("reasonsForRefusalButton"),
    CANDIDATE_REGISTRATION_BUTTON("candidateRegistrationButton");

    private final String message;

    CallBackDataEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
