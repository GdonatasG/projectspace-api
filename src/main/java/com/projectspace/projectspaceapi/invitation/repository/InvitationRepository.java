package com.projectspace.projectspaceapi.invitation.repository;

import com.projectspace.projectspaceapi.invitation.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByUserEmail(String email);

    List<Invitation> findAllByUser_Id(Long userId);
}
