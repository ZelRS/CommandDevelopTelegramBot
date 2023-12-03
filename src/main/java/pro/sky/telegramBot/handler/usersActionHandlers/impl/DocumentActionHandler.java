package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import com.pengrad.telegrambot.model.Document;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.handler.usersActionHandlers.DocumentHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.sender.DocumentMessageSender;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.UserState.PROBATION;
/**
 * класс для обработки сообщения, которое должно быть выслано пользователю<br>
 * при отправке им какого-либо документа
 */
@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j
public class DocumentActionHandler implements DocumentHandler {

    private final DocumentMessageSender documentMessageSender;
    private final UserService userService;
    private final MessageSender messageSender;

    @FunctionalInterface
    interface DocumentProcessor {
        void processDocument(Document document, Long chatId);
    }

    private final Map<String, DocumentProcessor> documentMap = new HashMap<>();
    /**
     * при запуске приложения происходит наполнение {@link #documentMap} документами,<br>
     * при получении которых должен высылаться конкретный ответ: подтверждение получения, <br>
     * указание на ошибки при заполнении или другие сообщения
     */
    @PostConstruct
    public void init() {
        // Получаем отчет в формате .xlsx
        documentMap.put("report.xlsx", (document, chatId) -> {
            log.info("Processing report.xlsx document");
            // Проверяем, есть ли пользователь и может ли он присылать отчеты
            User user = userService.findUserByChatId(chatId);
            if (user.getState().equals(PROBATION)) {
                try {
                    documentMessageSender.sendReportResponseMessage(document, chatId);
                } catch (IOException e) {
                    throw new UserNotFoundException("Пользователь не найден");
                }
            } else {
                //Сообщаем, что функция недоступна
                messageSender.sendNotSupportedMessage(chatId);
            }
        });
    }
    @Override
    public void handle(Document document, Long chatId) {
        String fileName = document.fileName();
        DocumentProcessor documentProcessor = documentMap.get(fileName);
        if (documentProcessor != null) {
            documentProcessor.processDocument(document, chatId);
        } else {
            //Если не нашли обработчик для присланного документа
            log.warn("No handler found for the document: {}", fileName);
        }
    }
}
