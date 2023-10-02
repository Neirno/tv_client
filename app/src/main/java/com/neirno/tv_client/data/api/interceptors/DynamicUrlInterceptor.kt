package com.neirno.tv_client.data.api.interceptors

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class DynamicUrlInterceptor : Interceptor {
    var newHost: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val modifiedUrl = newHost?.let {
            // Пытаемся создать полный URL из нового хоста для проверки его валидности
            val validationUrl = "http://$it".toHttpUrlOrNull()
                ?: // Если новый хост не валидный, возвращаем исходный запрос
                return chain.proceed(originalRequest)

            // Если новый хост валидный, меняем только хост в исходном URL
            originalRequest.url.newBuilder()
                .host(it)
                .build()
        } ?: originalRequest.url

        val newRequest = originalRequest.newBuilder().url(modifiedUrl).build()
        return chain.proceed(newRequest)
    }
}
