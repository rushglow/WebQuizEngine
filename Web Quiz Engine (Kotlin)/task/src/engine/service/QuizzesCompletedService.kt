package engine.service


import engine.model.AppUser
import engine.model.Quizzes
import engine.model.QuizzesCompleted
import engine.repositories.QuizzesCompletedRepository
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.userdetails.UserDetails

@Configuration
@Transactional
class QuizzesCompletedService(private val quizCompletedRepository: QuizzesCompletedRepository) {

    fun findAllByUserId(
        id: Long?, pageNumber: Int,
        pageSize: Int, sortBy: String?
    ): Page<QuizzesCompleted?> {
        val paging: Pageable = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.by(sortBy).descending()
        )
        return quizCompletedRepository.findAllByUserId(id, paging)
    }

    fun saveQuizCompleted(toSave: QuizzesCompleted) {
        quizCompletedRepository.save(toSave)
    }
}