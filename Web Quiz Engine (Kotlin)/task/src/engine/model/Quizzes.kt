package engine.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "quizzes")
class Quizzes(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,

    @NotNull
    @NotBlank
    @Column
    val title: String = "",

    @NotBlank
    @Column
    val text: String = "",

    @NotNull
    @Column(name = "quiz_option")
    @field:Size(min = 2)
    val options: MutableList<String>? = null,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    val answer: MutableList<Int>? = mutableListOf(),

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    var user: AppUser? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = [CascadeType.ALL])
    val completed: MutableList<QuizzesCompleted> = mutableListOf()
)