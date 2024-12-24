package br.com.challenge.backend.feature.fee.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object InterestRate : Table("interest_rates") {
    val id = integer("id").autoIncrement()
    val interestRate = decimal("interest_rate", 5, 2)
    val interestRateType = varchar("interest_rate_type", 15)
    val ageRangeStart = integer("age_range_start")
    val ageRangeEnd = integer("age_range_end")
    val effectiveDateStart = date("effective_date_start")
    val effectiveDateEnd = date("effective_date_end").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    override val primaryKey = PrimaryKey(id)
}