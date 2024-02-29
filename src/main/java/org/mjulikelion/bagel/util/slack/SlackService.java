package org.mjulikelion.bagel.util.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.bagel.util.slack.asset.message.SlackMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SlackService {
    @Value(value = "${slack.token}")
    String slackToken;

    public void sendSlackMessage(SlackMessage slackMessage) {

        String channelAddress = slackMessage.getChannel().getChannelName();

        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelAddress)
                    .text(slackMessage.getMessage())
                    .build();

            methods.chatPostMessage(request);

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
