package com.focusforge.app.core.ai

interface AiGateway {
    suspend fun health(): AiGatewayResult
}
sealed interface AiGatewayResult {
    data object Healthy : AiGatewayResult
    data class Unavailable(val reason: String) : AiGatewayResult
}
