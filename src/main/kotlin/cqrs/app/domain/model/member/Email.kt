package cqrs.app.domain.model.member

@JvmInline
value class Email private constructor(val address: String){

    companion object{
        operator fun invoke(address: String): Email {
            require(address.contains("@")) { "Email should be a valid Email" }
            return Email(address)
        }
    }
}