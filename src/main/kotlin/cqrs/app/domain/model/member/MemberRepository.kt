package cqrs.app.domain.model.member

import cqrs.app.domain.model.base.Repository


typealias MemberRepository = Repository<Member>
//{

//    fun convert(from: ItemDeactivated): ItemDeactivated_2 {
//        return ItemDeactivated_2(from.id, "")
//    }
//}