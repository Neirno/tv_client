package com.neirno.tv_client.data.util

import com.neirno.tv_client.data.data_source.entity.History as DataHistory
import com.neirno.tv_client.domain.entity.History as DomainHistory

fun DataHistory.toDomain(): DomainHistory {
    return DomainHistory(id = this.id, title = this.title)
}

fun DomainHistory.toData(): DataHistory {
    return DataHistory(id = this.id, title = this.title)
}
