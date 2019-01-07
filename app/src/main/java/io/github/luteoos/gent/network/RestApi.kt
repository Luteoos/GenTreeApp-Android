package io.github.luteoos.gent.network

import android.util.Base64
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import io.github.luteoos.gent.BuildConfig
import io.github.luteoos.gent.session.SessionManager
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RestApi {
        private val httpClient = OkHttpClient.Builder()
        val gson = GsonBuilder()
            .addSerializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(fieldAttributes: FieldAttributes): Boolean {
                    val expose = fieldAttributes.getAnnotation(Expose::class.java)
                    return expose != null && !expose.serialize
                }

                override fun shouldSkipClass(aClass: Class<*>): Boolean = false
            })
            .addDeserializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(fieldAttributes: FieldAttributes): Boolean {
                    val expose = fieldAttributes.getAnnotation(Expose::class.java)
                    return expose != null && !expose.deserialize
                }

                override fun shouldSkipClass(aClass: Class<*>): Boolean = false
            })
            .create()

    private val builder = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)

    fun <S> createService(serviceClass: Class<S>): S {
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "charset=utf-8; application/x-www-form-urlencoded")
                .header("Authorization","")
                .header("Accept", "application/json")
                .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        val client = httpClient.build()
        val retrofit = builder.client(client).build()
        return retrofit.create(serviceClass)
    }

    fun <S> createService(serviceClass: Class<S>, accessToken: String): S{
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type","application/json")
                .header("Authorization", "Bearer " +  accessToken)
                .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        val client = httpClient.build()
        val retrofit = builder.client(client).build()
        return retrofit.create(serviceClass)

    }

    fun <S> createService(serviceClass: Class<S>, accessToken: String, acceptHeader: String): S {
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Accept", acceptHeader)
                .header("Content-Type","application/json")
                .header("Authorization", "Bearer " +  accessToken)
                .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        val client = httpClient.build()
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
        return retrofit.create(serviceClass)
    }

    //avoid crsh wehn null response
    class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *> {
            val delegate = retrofit.nextResponseBodyConverter<Converter<ResponseBody, *>>(this, type, annotations)
            return Converter<ResponseBody, Any> { body ->
                if (body.contentLength() == 0.toLong()) return@Converter null
                delegate.convert(body)
            }
        }
    }
}