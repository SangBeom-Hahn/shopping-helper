package com.sketch2fashion.backend.domain.history;

import com.sketch2fashion.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Getter
@Entity
@Table(name = "common_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonLog extends BaseEntity {

    @Column(name = "common_log_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", length = 100, nullable = false)
    private Long messageId;

    @Column(name = "search_lookup_time", length = 150)
    private String searchLookupTime;

    @Column(name = "search_click_count", length = 255)
    private String searchClickCount;

    @Column(name = "search_category_type", length = 50)
    private String searchCategoryType;

    @Column(name = "queue_out_time", length = 150)
    private String queueOutTime;

    @Column(name = "inference_server_name", length = 100)
    private String inferenceServerName;

    @Column(name = "inference_end_time", length = 150)
    private String inferenceEndTime;

    @Builder
    public CommonLog(
            Long messageId,
            String searchLookupTime,
            String searchClickCount,
            String searchCategoryType,
            String queueOutTime,
            String inferenceServerName,
            String inferenceEndTime
    ) {
        this.messageId = messageId;
        this.searchLookupTime = searchLookupTime;
        this.searchClickCount = searchClickCount;
        this.searchCategoryType = searchCategoryType;
        this.queueOutTime = queueOutTime;
        this.inferenceServerName = inferenceServerName;
        this.inferenceEndTime = inferenceEndTime;
    }
}
