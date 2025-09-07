package com.example.membership_api.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


@Data
@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id",length = 36, updatable = false, nullable = false)
    private String id;
    

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Convert entity to Map (similar to to_dict in Python).
     * Excludes passwordHash field if class is User.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();

        for (var field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();

                // Skip passwordHash if class is User
                if (this.getClass().getSimpleName().equals("User") && fieldName.equals("passwordHash")) {
                    continue;
                }

                Object value = field.get(this);

                if (value instanceof Enum<?> aEnum) {
                    value = aEnum.name();
                }

                result.put(fieldName, value);
            } catch (IllegalAccessException e) {
                // Handle exception or ignore
            }
        }
        return result;
    }
}
