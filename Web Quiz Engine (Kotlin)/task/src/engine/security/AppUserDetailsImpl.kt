package engine.security

import engine.repositories.AppUserRepository
import engine.security.AppUserAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppUserDetailsImpl() : UserDetailsService {
    @Autowired
    private val repository: AppUserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository?.findAppUserByUsername(username)
            ?: throw UsernameNotFoundException("Not found")

        return AppUserAdapter(user)
    }
}


