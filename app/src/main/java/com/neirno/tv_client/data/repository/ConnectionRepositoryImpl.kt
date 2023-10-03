package com.neirno.tv_client.data.repository

import android.util.Log
import com.neirno.tv_client.data.data_source.ConnectionDao
import com.neirno.tv_client.data.data_source.entity.Connection as DataConnection
import com.neirno.tv_client.domain.entity.Connection as DomainConnection
import com.neirno.tv_client.domain.repository.ConnectionRepository
import com.neirno.tv_client.core.extension.toDomain
import com.neirno.tv_client.core.extension.toData
import com.neirno.tv_client.data.api.ConnectionApiService
import com.neirno.tv_client.data.api.interceptors.DynamicUrlInterceptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import com.neirno.tv_client.core.network.Result
import okio.IOException

class ConnectionRepositoryImpl(
    private val retrofit: Retrofit, // Зависимость от Retrofit
    private val connectionDao: ConnectionDao,
    private val dynamicUrlInterceptor: DynamicUrlInterceptor
) : ConnectionRepository {

    override fun getConnections(): Flow<List<DomainConnection>> {
        return connectionDao.getConnections()
            .map { dataList -> dataList.map { it.toDomain() } }
    }

    override suspend fun saveConnection(connection: DomainConnection) {
        connectionDao.insertConnection(connection.toData())
    }

    override suspend fun getConnectionById(id: Long): DomainConnection? {
        val dataConnection = connectionDao.getConnectionById(id)
        return dataConnection?.toDomain()
    }

    override suspend fun deleteConnection(id: Long) {
        return connectionDao.deleteConnection(id)
    }

    override suspend fun checkConnection(ip: String): Result<Boolean> {
        return try {
            val apiService = retrofit.newBuilder()
                .baseUrl("http://$ip/")
                .build()
                .create(ConnectionApiService::class.java)

            val response = apiService.checkConnection()
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(IOException("Неудачное соединение с $ip"))
            }
        } catch (e: Exception) {
            Log.e("Connect to IP", e.toString())
            Result.Error(e)
        }
    }

    override suspend fun checkAndSaveConnection(ip: String): Result<Boolean> {
        return try {
            val apiService = retrofit.newBuilder()
                .baseUrl("http://$ip/")
                .build()
                .create(ConnectionApiService::class.java)

            val response = apiService.checkConnection()
            if (response.isSuccessful) {
                Log.i("Connection DAO (impl)", "Insert ip = $ip")
                connectionDao.insertConnection(DataConnection(ip = ip))
                Result.Success(true)
            } else {
                Result.Error(IOException("Неудачное соединение с $ip при попытке сохранения"))
            }
        } catch (e: Exception) {
            Log.e("Connect to IP", e.toString())
            Result.Error(e)
        }
    }

    override suspend fun setInterceptorUrl(ip: String) {
        Log.d("DynamicUrlInterceptor", "Setting new host: $ip")
        dynamicUrlInterceptor.newHost = ip
    }

}
