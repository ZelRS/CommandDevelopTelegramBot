package pro.sky.telegramBot.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    @Value("${telegram.bot.token}")
    private String token;


// предлагается разбить на разные конфиг классы поля, хранящие в себе строки для различных целей

    @Value("${BACK_MES}")
    private String back_mes;



    @Value("${WELCOME_MES}")
    private String WELCOME_MES;
    @Value("${DEFAULT_MES}")
    private String DEFAULT_MES;
    @Value("${SHELTER_INTRO_MES}")
    private String SHELTER_INTRO_MES;




    @Value("${DOGS_BUT}")
    private String dogs_but;

    @Value("${CATS_BUT}")
    private String cats_but;





    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
