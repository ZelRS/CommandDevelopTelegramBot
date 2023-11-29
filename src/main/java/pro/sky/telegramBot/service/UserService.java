package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.users.User;


/**
 * интерфейс сервиса для обработки запросов к БД пользователей и информации о пользователях
 */
//  предполагается, что данным интерфейсом будут обрабатываться оба репозитория - User и UserInfo
public interface UserService {
    /**
     * получить пользователя из БД по chatId
     */
    User findUserByChatId(Long chatId);

    /**
     * получить пользователя из БД по id
     */
    User getById(Long id);

    /**
     * создать и сохранить пользователя в БД
     */
    User create(User user);
}
