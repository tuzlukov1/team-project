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
    BACK_TO_CAT_MAIN_MENU("backToCatMainMenu"),

    DOG_ACQUAINTANCE_RULES_BUTTON("puppyDatingRulesButton"),
    DOCUMENTS_FOR_DOGS_BUTTON("documentsForDogsButton"),
    TRANSPORTATION_ADVICE_BUTTON("transportationAdviceButton"),
    HOME_IMPROVEMENT_FOR_PUPPY_BUTTON("homeImprovementForPuppyButton"),
    HOME_IMPROVEMENT_FOR_AN_ADULT_DOG_BUTTON("homeImprovementForAnAdultDogButton"),
    HOME_IMPROVEMENT_FOR_DOG_WITH_SPECIAL_NEEDS_BUTTON("homeImprovementForDogWithDisabilityButton"),
    CYNOLOGISTS_ADVICE_ON_INITIAL_COMMUNICATION_BUTTON("cynologistsAdviceOnInitialCommunicationButton"),
    RECOMMENDATIONS_FOR_PROVEN_CYNOLOGISTS_BUTTON("recommendationsForProvenCynologistsButton"),
    REASON_FOR_REFUSAL_BUTTON("reasonsForRefusalButton"),
    DOG_SHELTER_BUTTON("dogShelterButton"),
    CANDIDATE_REGISTRATION_BUTTON("candidateRegistrationButton"),
    SHElTER_DOG_SECURITY_NUMBER_BUTTON("shelterDogSecurityNumber"),


    SHELTER_MENU_BUTTON("ShelterMenuButton"),
    CAT_SHELTER_BUTTON("catShelterButton"),
    ABOUT_CAT_SHELTER_BUTTON("aboutCatShelterButton"),
    SEND_CAT_REPORT_BUTTON("sendCatReportButton"),
    CANDIDATE_CAT_REGISTRATION("candidateCAtRegistration"),
    CAT_LOCATIONS("CatLocations"),
    SHELTER_CAT_INFORMATION_BUTTON("shelterCatInformationButton"),
    HOW_TAKE_CAT_BUTTON("howTakeCatButton"),
    SHELTER_CAT_SAFETY_REGULATIONS_BUTTON("shelterCatSafetyRegulationsButton"),
    CAT_ACQUAINTANCE_RULES("catDatingRulesButton"),
    REASON_CAT_FOR_REFUSAL_BUTTON("reasonCatForRefusalButton"),
    HOME_IMPROVEMENT_FOR_CAT_WITH_SPECIAL_NEEDS_BUTTON("homeImprovementForCatWithDisabilityButton"),
    HOME_IMPROVEMENT_FOR_AN_ADULT_CAT_BUTTON("homeImprovementForAnAdultCatButton"),
    HOME_IMPROVEMENT_FOR_KITTY_BUTTON("homeImprovementForKittyButton"),
    TRANSPORTATION_CAT_ADVICE_BUTTON("transportationCatAdviceButton"),
    DOCUMENTS_FOR_CATS_BUTTON("documentsForCatsButton"),
    SHElTER_CAT_SECURITY_NUMBER_BUTTON("shelterCatSecurityNumber");

    private final String message;


    CallBackDataEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }
