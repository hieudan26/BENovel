package mobile.Service;

public interface EmailService {
    public void sendSimpleMessage(
            String to, String token);

    public void sendMessageWithAttachment(
            String to, String subject, String text, String pathToAttachment);
}
