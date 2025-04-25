//package com.bentfores.gateway.server.filter
//
//import com.bentfores.gateway.server.config.properties.RedisProperties
//import com.nimbusds.jose.util.StandardCharset
//import java.nio.ByteBuffer
//import org.reactivestreams.Publisher
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.cloud.gateway.filter.GatewayFilter
//import org.springframework.cloud.gateway.filter.GatewayFilterChain
//import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
//import org.springframework.core.Ordered
//import org.springframework.core.io.buffer.DataBuffer
//import org.springframework.data.redis.cache.RedisCacheManager
//import org.springframework.http.server.PathContainer
//import org.springframework.http.server.reactive.ServerHttpResponse
//import org.springframework.http.server.reactive.ServerHttpResponseDecorator
//import org.springframework.stereotype.Component
//import org.springframework.web.server.ServerWebExchange
//import org.springframework.web.util.pattern.PathPatternParser
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Mono
//import reactor.kotlin.core.publisher.toFlux
//
//@Component
//class RedisFilterGatewayFilterFactory(
//  private val cacheManager: RedisCacheManager,
//  @Value("\${spring.cache.enabled}")
//  private val isCacheEnabled: Boolean,
//  private val cacheProperties: RedisProperties
//) : AbstractGatewayFilterFactory<Any>() {
//
//  override fun apply(config: Any): GatewayFilter {
//    return ResponseCacheGatewayFilter()
//  }
//
//  inner class ResponseCacheGatewayFilter : GatewayFilter, Ordered {
//
//    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
//      val path = exchange.request.path.toString()
//      var matchedPathPattern = cacheProperties.specs.keys.firstOrNull { permittedPath ->
//        PathPatternParser().parse(permittedPath).matches(
//          PathContainer.parsePath(path)
//        )
//      }
//
//      if (
//        matchedPathPattern != null
//        && cacheProperties.specs[matchedPathPattern]?.methodCachingEnabled == false
//      ) {
//        matchedPathPattern = null
//      }
//
//      val queryParamString = exchange.request.queryParams
//        .toSortedMap()
//        .entries
//        .joinToString("&") { queryParam ->
//          queryParam.value.toSortedSet().joinToString("&") { queryParamValue ->
//            "${queryParam.key}=${queryParamValue}"
//          }
//        }
//      val fullPath = "$path?$queryParamString"
//
//      return if (isCacheEnabled && matchedPathPattern != null) {
//        val cache = cacheManager.getCache(matchedPathPattern)
//        val requestCache = cache?.get(fullPath)
//        if (requestCache != null) {
//          exchange.response.writeWith(
//            Flux.fromIterable(
//              listOf(ByteBuffer.wrap(requestCache.get().toString().toByteArray()))
//            ).map {
//              exchange.response.bufferFactory().wrap(it)
//            }
//          )
//        } else {
//          chain.filter(
//            exchange.mutate().response(
//              CachedResponse(
//                exchange.response,
//                cacheManager,
//                matchedPathPattern,
//                fullPath
//              )
//            )
//              .build()
//          )
//        }
//      } else {
//        chain.filter(exchange)
//      }
//    }
//
//    override fun getOrder(): Int {
//      return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1
//    }
//  }
//
//  class CachedResponse(
//    delegate: ServerHttpResponse,
//    private val cacheManager: RedisCacheManager,
//    private val permitted: String,
//    private val path: String
//  ) : ServerHttpResponseDecorator(delegate) {
//
//    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
//      return super.writeWith(
//        Flux.from(body)
//          .flatMap {
//            it.readableByteBuffers().iterator().toFlux()
//              .doOnNext { byteBuffer ->
//                val cache = cacheManager.getCache(permitted)
//                cache!!.put(path, StandardCharset.UTF_8.decode(byteBuffer).toString())
//              }
//              .collectList()
//              .thenReturn(it)
//          }
//      )
//    }
//  }
//}