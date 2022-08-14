package telega_bot.Vilzevul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telega_bot.Vilzevul.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Repository
public interface BotRepository extends JpaRepository<NotificationTask, Long> {
    Collection<NotificationTask> findByTimeSendBefore(LocalDateTime localDateTime);


}
