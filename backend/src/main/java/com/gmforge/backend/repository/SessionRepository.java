package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findAllByParty(Party party);
    List<Session> findAllByPartyOrderBySessionNumberAsc(Party party);
}