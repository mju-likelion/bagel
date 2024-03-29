package org.mjulikelion.bagel.util.slack.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackChannel {
    FILE("#12기-지원-파일", "--- 파일이 업로드 되었습니다 ---"),
    APPLICATION("#12기-지원-지원서", "--- 지원서가 업로드 되었습니다 ---"),
    APPLY("#12기-지원-지원", "--- 새 지원 요청이 있습니다 ---");

    private final String channelName;
    private final String messageTitle;
}
