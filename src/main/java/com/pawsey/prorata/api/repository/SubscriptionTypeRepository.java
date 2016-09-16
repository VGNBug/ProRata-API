package com.pawsey.prorata.api.repository;

import com.pawsey.api.repository.BaseRepository;
import com.pawsey.prorata.model.SubscriptionTypeEntity;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository : SubscriptionType.
 */
public interface SubscriptionTypeRepository extends BaseRepository<SubscriptionTypeEntity, Integer> {
    @Query("SELECT p FROM SubscriptionTypeEntity p WHERE p.name = ?1")
    SubscriptionTypeEntity findByName(String name);
}
