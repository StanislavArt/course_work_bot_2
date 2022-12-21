package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotificationTaskServiceTest {

    @Value("${chat_id_for_test}")
    private Long chat_id;
    @Autowired
    private NotificationTaskRepository notificationTaskRepository;
    private NotificationTaskService out;
    @Autowired
    private TelegramBot telegramBot;

    @BeforeAll
    public void init() {
        out = new NotificationTaskService(notificationTaskRepository, telegramBot);
        out.add("Тестовое сообщение", chat_id, LocalDateTime.now().minusMinutes(5));
    }

    @Test
    public void notificationTaskSenderTest() {
        List<NotificationTask> notificationTasks = notificationTaskRepository.findAll();
        Assertions.assertEquals(notificationTasks.size(), 1);
        Assertions.assertFalse(notificationTasks.get(0).isSent());

        out.notificationTaskSender();

        notificationTasks = notificationTaskRepository.findAll();
        Assertions.assertEquals(notificationTasks.size(), 1);
        Assertions.assertTrue(notificationTasks.get(0).isSent());
    }


}

