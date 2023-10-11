package com.neirno.tv_client.data.util

import com.neirno.tv_client.data.data_source.entity.Connection as DataConnection
import com.neirno.tv_client.domain.entity.Connection as DomainConnection

fun DataConnection.toDomain(): DomainConnection {
    return DomainConnection(id = this.id, ip = this.ip)
}

fun DomainConnection.toData(): DataConnection {
    return DataConnection(id = this.id, ip = this.ip)
}
