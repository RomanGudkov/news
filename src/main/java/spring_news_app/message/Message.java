package spring_news_app.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class Message {
    private String message;

    public Message notFound(Long id) {
        String firstText = " с ID ";
        String secondText = " не найдена.";
        message = message + firstText + id + secondText;
        return new Message(message);
    }
}