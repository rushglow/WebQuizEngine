package engine.controllers

import engine.BadQuizException
import engine.QuizDeleted
import engine.UserExistException
import engine.UserMatchException
import engine.model.*
import engine.service.AppUserService
import engine.service.QuizService
import engine.service.QuizzesCompletedService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RestController
class Controller(
    private val quizService: QuizService,
    private val quizCompletedService: QuizzesCompletedService,
    private val appUserService: AppUserService
) {

    @PostMapping(value = ["/api/quizzes"], consumes = ["application/json"])
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun saveQuiz(
        @RequestBody quiz: @Valid Quizzes,
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<Quizzes> {
        if (quiz.options == null) return ResponseEntity.badRequest().build()
        return try {
            println(">>>QUIZ SAVED ${quiz.id} BY USER ${user.username}")
            quiz.user = appUserService.findAppUserByUsername(user.username)
            ResponseEntity.ok(quizService.saveQuiz(quiz, quiz.user))
        } catch (e: Exception){
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping(value = ["/api/quizzes/{id}/solve"], consumes = ["application/json"])
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun postAnswer(
        @PathVariable id:Long,
        @RequestBody answer: Answer,
        @AuthenticationPrincipal user: UserDetails
    ): AnswerResponse {
        if (quizService.existById(id)){
            return if (quizService.findQuizById(id).answer?.toMutableList() == (answer.answer)) {
                val quizUser = appUserService.findAppUserByUsername(user.username)
                val quiz = quizService.findQuizById(id)
                quizCompletedService.saveQuizCompleted(
                    QuizzesCompleted(
                        quizId = id,
                        completedAt = LocalDateTime.now(),
                        user = quizUser,
                        quiz = quiz
                    )
                )
                println(">>>ANSWER POSTED BY ${appUserService.findAppUserByUsername(user.username)?.username} ON QUIZ $id")
                AnswerResponse(true,"Good job")
            }
            else {
                println("${quizService.findQuizById(id).answer?.toMutableList()} == ${answer.answer}")
                AnswerResponse(false, "Wrong answer! Please, try again.")
            }
        }

        throw BadQuizException("Quiz with id = $id not found")
    }

    @GetMapping("/api/quizzes")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun getAllQuizzes(
        @RequestParam(defaultValue = "0") page: @Min(0) Int,
        @RequestParam(defaultValue = "10") pageSize: @Min(10) @Max(30) Int,
        @RequestParam(defaultValue = "id") sortBy: String?
    ): Page<Quizzes?> {
        return quizService.findAll(page, pageSize, sortBy)
    }

    @GetMapping("/api/quizzes/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun getQuiz(@PathVariable id: Long): Quizzes {
        if (quizService.existById(id)) return quizService.findQuizById(id)
        throw BadQuizException("Quiz with id = $id not found")
    }

    @GetMapping("/api/quizzes/completed")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun completedQuiz(
        @RequestParam(defaultValue = "0") page: @Min(0) Int,
        @RequestParam(defaultValue = "10") pageSize: @Min(10) @Max(30) Int,
        @RequestParam(defaultValue = "completedAt") sortBy: String?,
        @AuthenticationPrincipal user: UserDetails): Page<QuizzesCompleted?>? {
        println(">>>COMPLETED QUIZZES BY USER ${user.username}")
        return quizCompletedService.findAllByUserId(appUserService.findAppUserByUsername(user.username)?.id, page, pageSize, sortBy)
    }

    @DeleteMapping("/api/quizzes/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun deleteQuiz(@PathVariable id: Long, @AuthenticationPrincipal user: UserDetails) {
        if (quizService.existById(id)) {
            if (quizService.findQuizById(id).user?.username == user.username) {
                println(">>>QUIZ DELETED $id BY ${user.username}")
                quizService.deleteById(id)
                throw QuizDeleted("Quiz $id has been deleted")
            }
            throw UserMatchException("Its not u quiz")
        }
        throw BadQuizException("Quiz with id = $id not found")
    }
}

@RestController
class AppUserController(
    private val repository: AppUserService,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/api/register")
    fun regUser(@RequestBody @Valid request: RegistrationRequest):String {
        if (request.password.length < 5 ) throw UserExistException("Password length must be more than 5 char")
        if (!request.email.contains('@') || !request.email.contains('.')) throw UserExistException("Wrong email")
        if (repository.existsByUsername(request.email)) throw UserExistException("User ${request.email} already registered")
        val user = AppUser(
            username = request.email,
            password = passwordEncoder.encode(request.password),
            authority = "ROLE_USER"
        )
        repository.saveUser(user)

        return "User ${request.email}, ${request.password} registered"
    }

    data class RegistrationRequest(val email: String, val password: String,)
}
