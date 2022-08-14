package telega_bot.Vilzevul.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import telega_bot.Vilzevul.model.NotificationTask;
import telega_bot.Vilzevul.service.BotService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotUpdate implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(BotUpdate.class);

    @Autowired
    private TelegramBot telegramBot;
    private final BotService botService;

    public BotUpdate(BotService botService) {
        this.botService = botService;
    }


    @PostConstruct
    public void init() {
        logger.info("-Processing update: {init}");
        telegramBot.setUpdatesListener(this);
    }

    Message message;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            message = update.message();

            String idChat = update.message().chat().id().toString();
            String chatName = update.message().chat().firstName();
            if (update.message().text().equals("/start")) {
                SendResponse response = telegramBot.execute(new SendMessage(idChat, chatName + ", привет!"));

            } else {
                try {
                    telegramBot.execute(new SendMessage(idChat, setAlarm(update.message().text())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(fixedDelay = 60 * 1000)
    private SendResponse sendTask() {
        SendResponse response = null;
        Collection<NotificationTask> alarmList = new ArrayList(botService.findByTime());

        for (NotificationTask task : alarmList) {
            Long chat_id = task.getIdChat();
            String chat_msg = task.getMessage();

            response = telegramBot.execute(new SendMessage(chat_id, chat_msg));

            botService.delNotification(task.getId());
        }
        return response;

    }

    private String setAlarm(String textAlarm) throws IOException {

        final String regex = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";
        final String string = textAlarm;

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        String groupDateTime = null;
        if (matcher.find()) {
            groupDateTime = matcher.group(1);
            final String groupMessage = matcher.group(3);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(groupDateTime, format);
            botService.saveTask(new NotificationTask(message.chat().id(), groupMessage, dateTime));
            return "Напоминание создано";
        } else {
            return "Неверный формат ввода строки";
        }
    }

}