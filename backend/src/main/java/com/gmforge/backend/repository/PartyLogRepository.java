package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.PartyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartyLogRepository extends JpaRepository<PartyLog, Long> {
    List<PartyLog> findAllByPartyOrderByCreatedAtDesc(Party party);
}