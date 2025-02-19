package com.sketch2fashion.backend.repository.history;

import com.sketch2fashion.backend.domain.history.CommonLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonHistoryRepository extends JpaRepository<CommonLog, Long> {
}
