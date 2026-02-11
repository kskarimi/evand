package com.kkarimi.eventmanagement.datashipper;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ChangeHistoryRepository extends MongoRepository<ChangeHistoryDocument, String> {
}
