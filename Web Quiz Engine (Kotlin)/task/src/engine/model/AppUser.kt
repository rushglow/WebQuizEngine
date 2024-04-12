package engine.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "appUsers")
class AppUser (
    @Id
    @GeneratedValue
    var id: Long? = null,

    @NotNull
    @field:Email(regexp = "\\w+@\\w+\\.\\w+")
    var username: String? = null,

    @NotBlank
    @field:Size(min = 5, max = 255)
    var password: String? = null,

    @JsonIgnore
    var authority: String? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val quizzes: Set<Quizzes>? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val completed: Set<QuizzesCompleted>? = null

)