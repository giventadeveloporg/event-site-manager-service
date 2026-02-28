package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventAttendeeAttachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAttendeeAttachmentRepository extends JpaRepository<EventAttendeeAttachment, Long> {
    List<EventAttendeeAttachment> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<EventAttendeeAttachment> findByTenantIdAndAttendeeIdOrderByCreatedAtDesc(String tenantId, Long attendeeId);

    List<EventAttendeeAttachment> findByTenantIdAndEventIdOrderByCreatedAtDesc(String tenantId, Long eventId);

    List<EventAttendeeAttachment> findByTenantIdAndAttendeeIdAndEventIdOrderByCreatedAtDesc(String tenantId, Long attendeeId, Long eventId);
}
