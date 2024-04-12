package engine.repositories

import engine.model.Quizzes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizRepository: JpaRepository<Quizzes, Long> {
    fun findQuizById(id: Long): Quizzes
}
