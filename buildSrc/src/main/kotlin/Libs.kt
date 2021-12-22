object Libs {

    private const val ktorClient = "io.ktor:ktor-client"

    const val ktorClientCore = "$ktorClient-core:${Versions.ktor}"
    const val ktorClientCio = "$ktorClient-cio:${Versions.ktor}"
    const val ktorClientSerialization = "$ktorClient-serialization:${Versions.ktor}"
    const val ktorClientMock = "$ktorClient-mock:${Versions.ktor}"

    const val ktxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.ktxSerialization}"
    const val ktxCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.ktxCoroutines}"

}