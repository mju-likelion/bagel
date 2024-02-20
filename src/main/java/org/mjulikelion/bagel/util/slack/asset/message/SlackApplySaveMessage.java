package org.mjulikelion.bagel.util.slack.asset.message;

import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.util.slack.asset.SlackChannel;

public class SlackApplySaveMessage extends SlackMessage {
    public SlackApplySaveMessage(Application application) {
        super(SlackChannel.APPLICATION, String.format("%s\n\n이름: %s\n학과: %s\n학번: %s\n학년: %s\n전화번호: %s\n이메일: %s\n파트: %s",
                SlackChannel.APPLICATION.getMessageTitle(), application.getName(), application.getMajor().getName(),
                application.getStudentId(), application.getGrade(), application.getPhoneNumber(),
                application.getEmail(), application.getPart()));
    }
}
