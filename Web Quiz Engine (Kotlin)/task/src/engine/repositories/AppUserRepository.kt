package engine.repositories

import engine.model.AppUser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository: CrudRepository<AppUser, Long> {
    fun findAppUserByUsername(username: String?): AppUser?

    fun existsByUsername(username: String?): Boolean
}