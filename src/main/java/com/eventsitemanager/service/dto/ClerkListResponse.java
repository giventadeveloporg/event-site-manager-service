package com.eventsitemanager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Generic wrapper for Clerk API list responses.
 *
 * <p>All Clerk API list endpoints return responses in this format:</p>
 * <pre>
 * {
 *   "data": [...],
 *   "total_count": N
 * }
 * </pre>
 *
 * <p>Usage example:</p>
 * <pre>
 * ClerkListResponse&lt;OrganizationMembership&gt; response =
 *     objectMapper.readValue(json,
 *         new TypeReference&lt;ClerkListResponse&lt;OrganizationMembership&gt;&gt;() {});
 *
 * List&lt;OrganizationMembership&gt; memberships = response.getData();
 * </pre>
 *
 * @param <T> The type of items in the data list
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClerkListResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private List<T> data;

    @JsonProperty("total_count")
    private Integer totalCount;

    /**
     * Default constructor.
     */
    public ClerkListResponse() {
        this.data = Collections.emptyList();
        this.totalCount = 0;
    }

    /**
     * Constructor with parameters.
     *
     * @param data the list of data items
     * @param totalCount the total count
     */
    public ClerkListResponse(List<T> data, Integer totalCount) {
        this.data = data != null ? data : Collections.emptyList();
        this.totalCount = totalCount != null ? totalCount : 0;
    }

    /**
     * Get data list with null safety.
     *
     * @return List of data items, never null
     */
    public List<T> getData() {
        return data != null ? data : Collections.emptyList();
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * Get total count with null safety.
     *
     * @return Total count, defaults to 0 if null
     */
    public Integer getTotalCount() {
        return totalCount != null ? totalCount : 0;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * Check if the data list is empty.
     *
     * @return true if data list is empty, false otherwise
     */
    public boolean isEmpty() {
        return getData().isEmpty();
    }

    /**
     * Get the size of the data list.
     *
     * @return size of the data list
     */
    public int size() {
        return getData().size();
    }

    @Override
    public String toString() {
        return "ClerkListResponse{" + "data=" + data + ", totalCount=" + totalCount + '}';
    }
}
