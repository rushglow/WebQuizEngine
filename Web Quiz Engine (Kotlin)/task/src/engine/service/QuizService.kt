package engine.service

import engine.model.AppUser
import engine.model.Quizzes
import engine.repositories.QuizRepository
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

@Configuration
class QuizService(private val quizRepository: QuizRepository) {
    fun findQuizById(id: Long): Quizzes {
        return quizRepository.findQuizById(id)
    }

    fun saveQuiz(toSave: Quizzes, user: AppUser?): Quizzes {
        toSave.user = user
        return quizRepository.save(toSave)
    }

    fun existById(id: Long): Boolean {
        return quizRepository.existsById(id)
    }

    fun findAll(pageNumber: Int, pageSize: Int, sortBy: String?): Page<Quizzes?> {
        val paging: Pageable = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.by(sortBy)
        )
        return quizRepository.findAll(paging)
    }

    fun deleteById(id: Long) {
        quizRepository.deleteById(id)
    }

}