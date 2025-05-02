package vcmsa.projects.prog7313_poe.core.models

import java.time.Instant

interface AuditableEntity {
    val createdAt: Instant
    val updatedAt: Instant
} 