package engine.service

import engine.model.AppUser
import engine.repositories.AppUserRepository
import org.springframework.context.annotation.Configuration

@Configuration
class AppUserService(private val appUserRepository: AppUserRepository) {
    fun findAppUserByUsername(username: String?): AppUser? {
        return appUserRepository.findAppUserByUsername(username)
    }

    fun existsByUsername(username: String?): Boolean {
        return appUserRepository.existsByUsername(username)
    }

    fun saveUser(user: AppUser){
        appUserRepository.save(user)
    }
}