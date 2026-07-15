import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `profile-java`
    `profile-repositories`

    id("de.eldoria.plugin-yml.paper")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${Versions.PAPER_API}")

    implementation("com.eternalcode:eternalcode-commons-adventure:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-bukkit:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-shared:${Versions.ETERNALCODE_COMMONS}")

    implementation("dev.rollczi:litecommands-bukkit:${Versions.LITE_COMMANDS}")
    implementation("dev.rollczi:litecommands-adventure:${Versions.LITE_COMMANDS}")

    implementation("com.eternalcode:multification-bukkit:${Versions.MULTIFICATION}")
    implementation("com.eternalcode:multification-okaeri:${Versions.MULTIFICATION}")

    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${Versions.OKAERI_CONFIGS}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_CONFIGS}")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:${Versions.OKAERI_CONFIGS}")

    implementation("de.eldoria.jacksonbukkit", "paper", Versions.JACKSON_BUKKIT)

    implementation("xyz.xenondevs.invui:invui:${Versions.INV_UI}")

    // database
    implementation("org.mariadb.jdbc:mariadb-java-client:${Versions.MARIA_DB}")
    implementation("org.postgresql:postgresql:${Versions.POSTGRESQL}")
    implementation("com.h2database:h2:${Versions.H2}")
    implementation("com.j256.ormlite:ormlite-core:${Versions.ORMLITE}")
    implementation("com.j256.ormlite:ormlite-jdbc:${Versions.ORMLITE}")
    implementation("com.zaxxer:HikariCP:${Versions.HIKARI_CP}")
}

tasks.test {
    useJUnitPlatform()
}

paper {
    main = "dev.chudziudgi.ohmc.drop.paper.DropPlugin"
    apiVersion = "26.1.2"
    prefix = "ohmc-drop"
    authors = listOf("Chudziudgi")
    description = "Pozdro Kerl cipeczko spłakana"
    name = "ohmc-drop"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    version = "${project.version}"

    generateLibrariesJson = true

    serverDependencies {
    }
    foliaSupported = false
}

tasks.runServer {
    minecraftVersion("26.1.2")
}

tasks.shadowJar {
    archiveFileName.set("ohmc-drop v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**"
    )

    val prefix = "dev.chudziudgi.ohmc.drop.paper"
    listOf(
        "dev.rollczi",
        "eu.okaeri",
        "org.yaml",
        "com.eternalcode",
        "com.zaxxer.hikari",
        "com.j256.ormlite"
    ).forEach { relocate(it, prefix) }
}