package engine

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

class BadQuizException(message: String): RuntimeException(message)
class UserExistException(message: String): RuntimeException(message)
class QuizDeleted(message: String): RuntimeException(message)
class UserMatchException(message: String): RuntimeException(message)

class CustomErrorMessage(
    val statusCode:Int,
    val timestamp: LocalDateTime,
    val message: String?,
    val description: String
)

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(BadQuizException::class)
    fun handleQuizNotFound(e: BadQuizException, request: WebRequest): ResponseEntity<CustomErrorMessage> {
        val body = CustomErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now(),
            e.message,
            request.getDescription(false)
        )
        return ResponseEntity<CustomErrorMessage>(body, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserExistException::class)
    fun userExist(e: UserExistException, request: WebRequest): ResponseEntity<CustomErrorMessage> {
        val body = CustomErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            e.message,
            request.getDescription(false)
        )
        return ResponseEntity<CustomErrorMessage>(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(QuizDeleted::class)
    fun quizDeleted(e: QuizDeleted, request: WebRequest): ResponseEntity<CustomErrorMessage> {
        val body = CustomErrorMessage(
            HttpStatus.NO_CONTENT.value(),
            LocalDateTime.now(),
            e.message,
            request.getDescription(false)
        )
        return ResponseEntity<CustomErrorMessage>(body, HttpStatus.NO_CONTENT)
    }

    @ExceptionHandler(UserMatchException::class)
    fun userNotMatch(e: UserMatchException, request: WebRequest): ResponseEntity<CustomErrorMessage> {
        val body = CustomErrorMessage(
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now(),
            e.message,
            request.getDescription(false)
        )
        return ResponseEntity<CustomErrorMessage>(body, HttpStatus.FORBIDDEN)
    }
}