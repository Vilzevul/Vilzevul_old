package telega_bot.Vilzevul.service;

import org.springframework.stereotype.Service;
import telega_bot.Vilzevul.model.NotificationTask;
import telega_bot.Vilzevul.repository.BotRepository;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Service
public class BotService {
    private final BotRepository botRepository;

    public BotService(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    public NotificationTask saveTask(NotificationTask notificationTask) {
        return botRepository.save(notificationTask);
    }

    public Collection<NotificationTask> findByTime() {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return botRepository.findByTimeSendBefore(dateTime);
    }

    public void delNotification(Long id) {
        botRepository.deleteById(id);
    }
}

