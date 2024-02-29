package org.mjulikelion.bagel.util.slack.asset.message;

import org.mjulikelion.bagel.util.slack.asset.SlackChannel;

public class SlackFileMessage extends SlackMessage {
    public SlackFileMessage(String fileName, String fileUrl) {
        super(SlackChannel.FILE, String.format("%s\n\n업로드 요청 파일명: %s\n파일 url: %s",
                SlackChannel.FILE.getMessageTitle(), fileName, fileUrl));
    }
}
