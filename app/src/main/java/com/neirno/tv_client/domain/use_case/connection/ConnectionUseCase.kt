package com.neirno.tv_client.domain.use_case.connection

data class ConnectionUseCase (
    val insertConnection: InsertConnection,
    val getConnection: GetConnection,
    val getConnections: GetConnections,
    val deleteConnection: DeleteConnection,
    val checkAndSaveConnection: CheckAndSaveConnection,
    val checkConnection: CheckConnection
)