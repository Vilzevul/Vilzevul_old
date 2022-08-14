package telega_bot.Vilzevul.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificationtask")
public class NotificationTask {
    @Id
    @GeneratedValue
    private Long Id;

    private Long IdChat;

    private String message;
    private LocalDateTime timeSend;

    public NotificationTask(Long idChat, String message, LocalDateTime timeSend) {
        IdChat = idChat;
        this.message = message;
        this.timeSend = timeSend;
    }

    public NotificationTask() {

    }

    public Long getId() {
        return Id;
    }

    public void setGetId(Long getId) {
        this.Id = getId;
    }

    public Long getIdChat() {
        return IdChat;
    }

    public void setIdChat(Long idChat) {
        IdChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(LocalDateTime timeSend) {
        this.timeSend = timeSend;
    }
}
