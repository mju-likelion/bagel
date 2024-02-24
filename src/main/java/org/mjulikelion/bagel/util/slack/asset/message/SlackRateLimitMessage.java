package org.mjulikelion.bagel.util.slack.asset.message;

import org.mjulikelion.bagel.util.slack.asset.SlackChannel;

public class SlackRateLimitMessage extends SlackMessage {
    public SlackRateLimitMessage(String ipAddress, String url, String requestBody) {
        super(SlackChannel.RATE_LIMIT,
                String.format("%s\n\nIP: %s\nURL: %s\n request body: %s", SlackChannel.RATE_LIMIT.getMessageTitle(),
                        ipAddress, url,
                        requestBody));
    }
}
