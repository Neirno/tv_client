package com.neirno.tv_client.domain.use_case.connection

data class ConnectionUseCase (
    val addConnection: AddConnection,
    val getConnection: GetConnection,
    val getConnections: GetConnections,
    val deleteConnection: DeleteConnection,
    val checkAndSaveConnection: CheckAndSaveConnection,
    val checkConnection: CheckConnection
)