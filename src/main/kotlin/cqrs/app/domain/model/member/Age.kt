package cqrs.app.domain.model.member

@JvmInline
value class Age private constructor(val value: Int){

    companion object{
        operator fun invoke(value: Int): Age {
            require(value > 0) { "Age should be greater than 0" }
            return Age(value)
        }

//        internal fun from(value: Int): Age {
//            return Age(value)
//        }
//
//        internal fun zero(): Age {
//            return Age.from(0)
//        }
    }
}