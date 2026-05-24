package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import com.nextjstemplate.domain.EventDetails;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetitionContentBlock.
 */
@Entity
@Table(name = "event_competition_content_block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionContentBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id")
    private String tenantId;

    @NotNull
    @Size(max = 32)
    @Column(name = "block_type")
    private String blockType;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Lob
    @NotNull
    @Column(name = "body_markdown")
    private String bodyMarkdown;

    @NotNull
    @Column(name = "sort_order")
    private Integer sortOrder;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetitionContentBlock tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBlockType() {
        return this.blockType;
    }

    public EventCompetitionContentBlock blockType(String blockType) {
        this.setBlockType(blockType);
        return this;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getTitle() {
        return this.title;
    }

    public EventCompetitionContentBlock title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyMarkdown() {
        return this.bodyMarkdown;
    }

    public EventCompetitionContentBlock bodyMarkdown(String bodyMarkdown) {
        this.setBodyMarkdown(bodyMarkdown);
        return this;
    }

    public void setBodyMarkdown(String bodyMarkdown) {
        this.bodyMarkdown = bodyMarkdown;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public EventCompetitionContentBlock sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetitionContentBlock createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCompetitionContentBlock updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails event) {
        this.event = event;
    }

    public EventCompetitionContentBlock event(EventDetails event) {
        this.setEvent(event);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetitionContentBlock id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionContentBlock)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetitionContentBlock) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
