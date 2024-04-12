package engine.repositories

import engine.model.AppUser
import engine.model.QuizzesCompleted
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface QuizzesCompletedRepository: JpaRepository<QuizzesCompleted, Long> {
    fun findAllByUserId(id: Long?, pageable: Pageable): Page<QuizzesCompleted?>
}