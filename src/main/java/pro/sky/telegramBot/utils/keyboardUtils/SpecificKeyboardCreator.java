package pro.sky.telegramBot.utils.keyboardUtils;

import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.Button;

import java.util.ArrayList;
import java.util.List;

import static pro.sky.telegramBot.entity.Button.CallbackData.*;

/**
 * методы класса получают логику создания конкретной однострочной<br>
 * клавиатуры и задают свойства кнопкам
 */
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificKeyboardCreator {

    private final KeyboardCreator keyboardCreator;
    private final BotConfig config;

    /**
     * метод формирует для сообщения ряд определенных кнопок,<br>
     * с помощью которых пользователь может выбрать тип животного,<br>
     * которым он интересуется
     */
    public Keyboard petSelectionMessageKeyboard() {
        log.info("Creating keyboard markup for cats and dogs");

        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_WANT_CAT(), BUT_WANT_CAT.getCallbackData()));
        buttons.add(new Button(config.getBUT_WANT_DOG(), BUT_WANT_DOG.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }


    /**
     * метод формирует для сообщения ряд определенных кнопок ,<br>
     * в которых отражается функционал приюта, выбранного пользователем
     */
    public Keyboard shelterFunctionalMessageKeyboard() {
        log.info("Creating keyboard markup for shelters functional");

        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_GET_FULL_INFO(), BUT_GET_FULL_INFO.getCallbackData()));
        buttons.add(new Button(config.getBUT_TAKING_PET(), BUT_TAKING_PET.getCallbackData()));
        buttons.add(new Button(config.getBUT_SEND_REPORT(), BUT_SEND_REPORT.getCallbackData()));
        buttons.add(new Button(config.getBUT_CALL_VOLUNTEER(), BUT_CALL_VOLUNTEER.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    public Keyboard takingPetMessageKeyboard() {
        log.info("Creating keyboard markup for taking pet message");
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_CARE_PET_REC(), BUT_CARE_PET_REC.getCallbackData()));
        buttons.add(new Button(config.getBUT_START_REGISTRATION(), BUT_START_REGISTRATION.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * метод формирует для сообщения активную кнопку ,<br>
     * загрузки отчета
     */
    public Keyboard fillOutReportActiveMessageKeyboard() {
        log.info("Creating keyboard markup for taking pet message");
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_FILL_OUT_REPORT_ON(), BUT_FILL_OUT_REPORT_ON.getCallbackData()));
        buttons.add(new Button(config.getBUT_SEND_PET_PHOTO(), BUT_SEND_PET_PHOTO.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * метод формирует для сообщения неактивную кнопку ,<br>
     * загрузки отчета
     */
    public Keyboard fillOutReportNotActiveMessageKeyboard() {
        log.info("Creating keyboard markup for taking pet message");
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_FILL_OUT_REPORT_ON(), BUT_FILL_OUT_REPORT_OFF.getCallbackData()));
        buttons.add(new Button(config.getBUT_SEND_PET_PHOTO(), BUT_SEND_PET_PHOTO.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * функциональная клавиатура для информационного раздела приюта
     */
    public Keyboard shelterInformationFunctionalKeyboard() {
        log.info("Creating keyboard markup for shelters information windows");

        List<Button> buttons = new ArrayList<>(List.of(
                new Button(config.getBUT_MORE_INFORMATION(), BUT_MORE_INFORMATION.getCallbackData()),
                new Button(config.getBUT_GO_TO_MAIN(), BUT_GO_TO_MAIN.getCallbackData()),
                new Button(config.getBUT_GO_TO_SHELTER_SELECT(), BUT_GO_TO_SHELTER_SELECT.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * клавиатура главного окна для информационного раздела приюта
     */
    public Keyboard shelterInformationMainKeyboard() {
        log.info("Creating keyboard markup for shelters information windows");

        List<Button> buttons = new ArrayList<>(List.of(
                new Button(config.getBUT_GO_TO_MAIN(), BUT_GO_TO_MAIN.getCallbackData()),
                new Button(config.getBUT_GO_TO_SHELTER_SELECT(), BUT_GO_TO_SHELTER_SELECT.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    //Метод создает клавиатуру для ответа пользователя на вопросы в отчете
    public Keyboard questionForReportMessageKeyboard(int questionIdentifier, Long reportId) {
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            Button button = new Button(Integer.toString(i), i + "_" + questionIdentifier + "_" + reportId);
            buttons.add(button);
        }
        return keyboardCreator.createInlineKeyboardTwoRow(buttons);
    }

    public Keyboard volunteerMenuMessageKeyboard() {
        log.info("Creating keyboard markup for volunteer");

        List<Button> buttons = new ArrayList<>(List.of(
                new Button(config.getBUT_STATISTIC_NEW_USER(), BUT_STATISTIC_NEW_USER.getCallbackData()),
                new Button(config.getBUT_STATISTIC_SHELTER(), BUT_STATISTIC_SHELTER.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * клавиатура под сообщением, когда пользователь приглашается в приют
     */
    public Keyboard afterRegistrationFinalKeyboard() {
        log.info("Creating after registration final keyboard markup for user");

        List<Button> buttons = new ArrayList<>(List.of(
                new Button(config.getBUT_START_REGISTRATION(), BUT_START_REGISTRATION.getCallbackData()),
                new Button(config.getBUT_GET_FULL_INFO(), BUT_GET_FULL_INFO.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);
    }


    /**
     * клавиатура для сообщения "позвать волонтёра"
     */
    public Keyboard pressTheButtonToCallVolunteerKeyboard() {
        log.info("Creating a keyboard call a volunteer");
        List<Button> buttons = new ArrayList<>(List.of(
           new Button(config.getBUT_CALL_VOLUNTEER(), BUT_CALL_VOLUNTEER.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

//    ...... клавиатуры для других типов сообщений.....

}
