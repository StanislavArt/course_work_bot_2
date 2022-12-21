package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationTaskService {
    private final Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    public NotificationTask add(String message, Long chat_id, LocalDateTime localDateTime) {
        NotificationTask notificationTask = new NotificationTask(message, chat_id, localDateTime);
        NotificationTask notificationTaskSaved = notificationTaskRepository.save(notificationTask);
        logger.info("В базу данных внесена запись: {}", notificationTaskSaved);
        return notificationTaskSaved;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void notificationTaskSender() {
        List<NotificationTask> tasks = notificationTaskRepository.getNotificationTasks(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        for (NotificationTask notificationTask : tasks) {
            SendMessage sendMessage = new SendMessage(notificationTask.getChatId(), notificationTask.getMessage());
            SendResponse response = telegramBot.execute(sendMessage);
            if (response.isOk()) {
                logger.info("Message is sent into chat <{}> successfully", notificationTask.getChatId());
                notificationTask.setSent(true);
                notificationTaskRepository.save(notificationTask);
            } else {
                logger.error("Error: message is not sent into chat <{}> (command '/start')", notificationTask.getChatId());
            }
        }
    }

}
