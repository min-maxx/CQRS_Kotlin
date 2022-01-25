package cqrs.app.domain.model.member

@JvmInline
value class Password private constructor(val hash: String){

    companion object{
       operator fun invoke(value: String): Password {
           require(value.isNotBlank()) { "Password is missing" }
           return Password(value.hashCode().toString())
       }
   }

}