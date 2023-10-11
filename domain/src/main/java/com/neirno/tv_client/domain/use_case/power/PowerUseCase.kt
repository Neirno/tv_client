package com.neirno.tv_client.domain.use_case.power

data class PowerUseCase (
    val displayOn: DisplayOn,
    val displayOff: DisplayOff,
    val lightOn: LightOn,
    val lightOff: LightOff
)
