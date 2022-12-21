package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final NotificationTaskService notificationTaskService;

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(NotificationTaskService notificationTaskService, TelegramBot telegramBot) {
        this.notificationTaskService = notificationTaskService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            switch (update.message().text()) {
                case "/start":
                    SendMessage message = new SendMessage(update.message().chat().id(), "Добро пожаловать в чат 'Notification_bot'!");
                    SendResponse response = telegramBot.execute(message);
                    if (response.isOk()) {
                        logger.info("Message is sent into chat successfully (command '/start')");
                    } else {
                        logger.error("Error: message is not sent into chat (command '/start')");
                    }
                    break;
                default:
                    String messageRow = update.message().text();
                    String regex = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(messageRow);
                    if (matcher.matches()) {
                        logger.info("Message '{}' is parsed successfully", messageRow);
                        String dateString = messageRow.substring(0, 16);
                        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                        String messageForNotify = messageRow.substring(17);
                        Long chatId = update.message().chat().id();
                        notificationTaskService.add(messageForNotify, chatId, localDateTime);
                    } else {
                        logger.error("Message '{}' can't be parsing", messageRow);
                    }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
