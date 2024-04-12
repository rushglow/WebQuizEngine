package engine.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "quizzesCompleted")
class QuizzesCompleted(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    @Column(name = "completed_id")
    var completedId: Long? = null,

    @JsonProperty("id")
    @Column(name = "quiz_id")
    var quizId: Long = 0,

    @Column
    var completedAt: LocalDateTime? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: AppUser? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz")
    val quiz: Quizzes? = null
) {
}