package org.mjulikelion.bagel.util.slack.asset.message;

import lombok.Getter;
import org.joda.time.DateTime;
import org.mjulikelion.bagel.util.slack.asset.SlackChannel;

@Getter
public class SlackMessage {
    protected SlackChannel channel;
    protected String message;

    public SlackMessage(SlackChannel channel, String message) {
        this.channel = channel;
        this.message = message + "\n\n요청 일시: " + buildTimestamp();
    }

    protected String buildTimestamp() {
        return new DateTime().toString("yyyy-MM-dd HH:mm:ss");
    }
}
