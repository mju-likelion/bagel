package org.mjulikelion.bagel.util.slack.asset.message;

import org.mjulikelion.bagel.util.slack.asset.SlackChannel;


public class SlackApplyMessage extends SlackMessage {
    public SlackApplyMessage(String studentId) {
        super(SlackChannel.APPLY, String.format("%s\n\n학번: %s", SlackChannel.APPLY.getMessageTitle(), studentId));
    }
}
