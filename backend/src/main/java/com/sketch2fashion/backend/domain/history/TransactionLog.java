package com.sketch2fashion.backend.domain.history;

import com.sketch2fashion.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "transaction_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionLog extends BaseEntity {

    @Column(name = "transaction_log_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", length = 255)
    private Long messageId;

    @Column(name = "http_method", length = 50, nullable = false)
    private String httpMethod;

    @Column(name = "request_uri", length = 100, nullable = false)
    private String requestUri;

    @Enumerated(EnumType.STRING)
    @Column(name = "site_name", length = 100)
    private SiteName siteName;

    public TransactionLog(Long messageId, String httpMethod, String requestUri) {
        this.messageId = messageId;
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
    }

    public TransactionLog(String httpMethod, String requestUri, SiteName siteName) {
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.siteName = siteName;
    }
}
