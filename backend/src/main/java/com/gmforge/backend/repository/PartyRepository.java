package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {
    List<Party> findAllByGameMaster(User gameMaster);
    List<Party> findDistinctByGameMasterOrMembers_User(
            User gameMaster,
            User member
    );
}