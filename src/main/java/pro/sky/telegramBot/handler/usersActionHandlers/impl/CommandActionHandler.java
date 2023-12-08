package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.handler.specificHandlers.impl.ShelterCommandHandler;
import pro.sky.telegramBot.handler.specificHandlers.impl.WelcomeMessageHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.ActionHandler;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.ShelterService;
import pro.sky.telegramBot.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pro.sky.telegramBot.enums.Command.*;
import static pro.sky.telegramBot.enums.PetType.CAT;
import static pro.sky.telegramBot.enums.PetType.DOG;
import static pro.sky.telegramBot.enums.UserState.BLOCKED;
import static pro.sky.telegramBot.enums.UserState.PROBATION;

/**
 * класс для обработки сообщения, которое должно быть выслано пользователю<br>
 * при отправке им какой-либо определенной команды
 */
@Service
//@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class CommandActionHandler implements ActionHandler {
    private final MessageSender messageSender;
    private final WelcomeMessageHandler welcomeMessageHandler;
    private final ShelterService shelterService;
    private final UserService userService;
    private final BlockedUserHandler blockedUserHandler;
    private final ShelterCommandHandler shelterCommandHandler;

    @FunctionalInterface
    interface Command {
        void run(String firstName, String lastName, Long chatId);
    }

    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * при запуске приложения происходит наполнение {@link #commandMap} с командами,<br>
     * при получении которых должен высылаться конкретный ответ
     */
    @PostConstruct
    public void init() {

        commandMap.put(START.getName(), (firstName, lastName, chatId) -> {
            log.info("Received START command");
            welcomeMessageHandler.handleStartCommand(firstName, chatId);
        });

        commandMap.put(REPORT.getName(), (firstName, lastName, chatId) -> {
            log.info("Received REPORT command");
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getState().equals(PROBATION)) {
                messageSender.sendReportToUserDocumentMessage(chatId);
            } else {
                messageSender.sendNotSupportedMessage(chatId);
            }
        });

        commandMap.put(INFO_TABLE.getName(), (firstName, lastName, chatId) -> {
            log.info("Received INFO_TABLE command");
            messageSender.sendInfoTableToUserDocumentMessage(chatId);
        });

        //Меню для дополнительной информации по приюту
        //Узнать дополнительную информацию о приюте
        commandMap.put("/details", (firstName, lastName, chatId) -> {
            log.info("Received /details command");
            messageSender.menuInformationHandler(chatId, "/details");
        });

        // Получить одрес приюта
        commandMap.put("/address", (firstName, lastName, chatId) -> {
            log.info("Received /address command");
            messageSender.menuInformationHandler(chatId, "/address");
        });

        // Получить график работы приюта
        commandMap.put("/schedule", (firstName, lastName, chatId) -> {
            log.info("Received /schedule command");
            messageSender.menuInformationHandler(chatId, "/schedule");
        });

        // Посмотреть схему проезда к приюту
        commandMap.put("/schema", (firstName, lastName, chatId) -> {
            log.info("Received /schema command");
            messageSender.menuInformationHandler(chatId, "/schema");
        });

        // Узнать номер телефона охраны для оформления пропуска
        commandMap.put("/sec_phone", (firstName, lastName, chatId) -> {
            log.info("Received /sec_phone command");
            messageSender.menuInformationHandler(chatId, "/sec_phone");
        });

        // Прочитать правила техники безопасности приюта
        commandMap.put("/safety", (firstName, lastName, chatId) -> {
            log.info("Received /safety command");
            messageSender.menuInformationHandler(chatId, "/safety");
        });

        // Оставить заявку на обратный звонок
        commandMap.put("/callme", (firstName, lastName, chatId) -> {
            log.info("Received /callMe command");
            messageSender.menuInformationHandler(chatId, "/callMe");
        });

        // Связаться с волонтером
        commandMap.put("/volunteer", (firstName, lastName, chatId) -> {
            log.info("Received /volunteer command");
            messageSender.sendShelterFullInfoHTMLMessage(firstName, lastName, chatId);
        });

        int refRecDocCount = 9; // число равно максимальному количеству(n) документов в /{n}rec в application.properties
        for (int i = 1; i <= refRecDocCount; i++) {

            String command = "/" + i + "rec";
            int refNum = i;
            commandMap.put(command, (firstName, lastName, chatId) -> {
                log.info("Received getting {} RecDoc file command", refNum);
                messageSender.sendRecDocDocumentMessage(refNum, chatId);
            });
        }
    }

    /**
     * Метод ищет, есть ли в {@link #commandMap} кнопка по ключу.
     * Если кнопка найдена, совершается логика, лежащая по значению этого ключа.
     * Если такой команды нет, отправляется дефолтное сообщение
     */
    @Override
    public void handle(String command, String firstName, String lastName, Long chatId) {
        User user = userService.findUserByChatId(chatId);
        if (user != null && user.getState().equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
            return;
        }
        if (command.startsWith("/phone")) {
            String phone = command.split(" ")[1];
            messageSender.addPhoneNumberToPersonInfo(firstName, lastName, chatId, phone);
            return;
        }
        if (command.matches("^/\\d+_((DOG)|(CAT))$")) {
            shelterCommandHandler.handle(command, firstName, lastName, chatId);
            return;
        }
        Command commandToRun = commandMap.get(command.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for command: {}", command);
            // отправка дефолтного сообщения
            messageSender.sendDefaultHTMLMessage(chatId);
        }
    }

}
