plugins {
    java
    application
}

dependencies {
    // jaxws-ri n'était pas résolu sur Maven Central dans cet environnement.
    // Utiliser jaxws-rt 2.3.3 (runtime) qui fournit Endpoint et est disponible sur Maven Central.
    implementation("com.sun.xml.ws:jaxws-rt:2.3.3")
}

application {
    mainClass.set("ma.projet.server.ServerJWS")
}

// Add necessary --add-opens flags to allow reflective access required by the JAX-WS runtime on Java 16+.
tasks.withType<JavaExec> {
    // Limit JVM memory to avoid native OOM on machines with limited RAM/pagefile
    jvmArgs = listOf(
        "-Xms128m",
        "-Xmx512m",
        "-XX:MaxMetaspaceSize=256m",
        "-XX:ReservedCodeCacheSize=64m",
        "--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens=java.xml/javax.xml.namespace=ALL-UNNAMED",
        "--add-opens=java.base/java.io=ALL-UNNAMED",
        "--add-opens=java.base/java.nio=ALL-UNNAMED",
        "--add-opens=java.base/java.net=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.logging/java.util.logging=ALL-UNNAMED",
        "-Dcom.sun.xml.ws.transport.http.HttpAdapter.dump=true"
    )
}
