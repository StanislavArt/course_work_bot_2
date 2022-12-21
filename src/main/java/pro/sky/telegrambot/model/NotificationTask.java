package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    private String message;

    @Column(name = "datetime_plan")
    private LocalDateTime planDateTime;

    @Column(name = "sent")
    private boolean sent;

    public Long getId() {
        return id;
    }

    public NotificationTask() {}

    public NotificationTask(String message, Long chatId, LocalDateTime planDateTime) {
        this.chatId = chatId;
        this.message = message;
        this.planDateTime = planDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getPlanDateTime() {
        return planDateTime;
    }

    public void setPlanDateTime(LocalDateTime planDateTime) {
        this.planDateTime = planDateTime;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", planDateTime=" + planDateTime +
                '}';
    }
}
