package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.PartyMember;
import com.gmforge.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    List<PartyMember> findAllByParty(Party party);
    List<PartyMember> findAllByUser(User user);
    boolean existsByPartyAndUser(Party party, User user);
}