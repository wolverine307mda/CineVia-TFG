package org.wolve.geofilm.auth.services.autentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.users.dto.CreateUsuarioRequest
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.users.mappers.UsuarioMapper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Service
class AuthenticationServiceImpl(
    private val userRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val usuarioMapper: UsuarioMapper
) : AuthenticationService {

    override fun signup(request: SignUpRequest): JwtAuthenticationResponse {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val fechaNacimientoParseada: LocalDate? = request.fechaNacimiento?.let {
            try {
                LocalDate.parse(it, formatter)
            } catch (e: DateTimeParseException) {
                null
            }
        }

        val avatar = (1..6).random().toString()

        val createDto = CreateUsuarioRequest(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nombre = request.firstName,
            apellido = request.lastName,
            telefono = request.phone,
            avatar = "https://ffa86d19e86d1943b46f5b8abc7aa4ed56613dc2f9fc0a5ecc49759-apidata.googleusercontent.com/download/storage/v1/b/movietrip-e3a91.firebasestorage.app/o/fotosPerfil%2Ffoto_perfil_${avatar}.png?jk=AXbWWmnkz5b-HOga_FVaB-SR7PUT_bq6jg1UQpFlbeigxiSANQJnyhEFeLTpdQ7_9hhBB8sBAAhWQyEqHutwGmNTPC0JEIjkmQ2yugUvePBQn4Kq6JEqjEzGACLDt5qpNw5NF5PPAZMiqaJhY4MNWBNuarUmATm3nu70s5w1F_F_iSLrgDj2PbptoACenxbgTo8S5p7UxF9nFrp5ngM6Gn-ZC_D9X7n1RK-qEzs-3TqrmrzgjeuYHFAZu4pyIZubDfnxCZL_ijMTgqHYjnVDJItaktS-LZPz7DmjjyrYCnaptbASqyEPWupnpZX4A6gicqNBqD5PZNuLJ5GarSlGX06LHNskK2NoYEsz4phiLAKfeBQnZIo6gjFwTQbCfZARZEM8_4iQyDIFGiY7CWUJJTW7z3zAdcldoi-pwfrEMFT_sbRGugh5r7PKfJqtJ3DEIZXq3uUXvdSWqN861AsDUo9XWy0SrdSm7XCJS2fUh-Vw2VRFph-zIV6MsxnxH0PPzOcFrLxiMrPmW9R60xenA8YB3VdHJvX9vN5xMeGej_M8g4gpxm6r-ooiBagh8CvWWUMxr3Grmnietsc2a5AL7ZJ6S-xcEZ8IEhlCu1pDM0hmFDsbSfq0vPju55_RfgpxPNKuLUVwbF-8C-UIuU8CV4MQ3mlNpxd80twunBtGzChw00pE6t6sXOX8ELXoz0agT6hAt6eYIzb6V77mZvPucY23rQeNlxCOo6Nyc3TppQRDf9jkW3sTV0gnCkjfg89nJZevzGPTPosgwf33A7-C_6wPOmQSNhvHgU48lVkB5hhMzZYzOAh-lFbdDE_CThnrxDcH1MdEX2oLN5TeDspW_fYgRl03_98WQ4BU_YfLTYAOl6h0OhP95KIcyDoj24WrfmRQy5GWyQE6-J_GOQG6ql5k8DQisXemSn9tTxh2Br5xG2l-gbcaHGCqU74ZYwGlcSKDBROO3EEcMBiMYQ78HbmO2CyChjliJfPdePjfhtg5Q5NrFd94jO5z1vEWS47ltwlvAanz46-H7U7Ljq-Zy26QzZQbxBpVVAFdt_sSScJfs7b2HqtfLKI8oQUyNP00ZIJhJfs48MfVcw7-XvBzivtlDAjmOWj5cjGTC_rODAGIkY9mTOK5pmcxoi6dKLEWYTIWayTAmEgUEeaPkqX2KmD2VzFmL7ScvkxLLcrEInV_BxEom8L6OgiocOn0XRoPHMATcwFtBTPyxnMbc1xfxEQDkGPLi_GJu6Y&isca=1",
            fechaNacimiento = fechaNacimientoParseada ?: LocalDate.now(),
            rol = RolUsuario.USUARIO
        )
        val user = usuarioMapper.toEntity(createDto)
        userRepository.save(user)
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.name)
    }

    override fun signin(request: SigninRequest): JwtAuthenticationResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
        } catch (e: BadCredentialsException) {
            throw IllegalArgumentException("Correo o contraseña incorrectos")
        }

        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }

        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.name)
    }
}