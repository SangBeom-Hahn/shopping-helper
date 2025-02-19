package com.sketch2fashion.backend.repository.history;

import com.sketch2fashion.backend.domain.history.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionLog, Long> {
}
