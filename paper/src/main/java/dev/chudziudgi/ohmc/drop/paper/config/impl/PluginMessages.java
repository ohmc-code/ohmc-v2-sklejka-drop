package dev.chudziudgi.ohmc.drop.paper.config.impl;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class PluginMessages extends OkaeriConfig {

    @Comment("# Wiadomość gdy komenda jest tylko dla graczy")
    public Notice playerOnly = Notice.chat("<red>Ta komenda jest dostępna tylko dla graczy!");

    @Comment("# Wiadomość gdy nie znaleziono gracza")
    public Notice playerNotFound = Notice.chat("<red>Nie znaleziono podanego gracza!");

    @Comment("# Wiadomość gdy brak uprawnień. {PERMISSIONS} - brakujące uprawnienia")
    public Notice noPermission = Notice.chat("<red>Nie masz uprawnień do tej komendy! <dark_red>({PERMISSIONS})");

    @Comment("# Wiadomość gdy błędne użycie komendy. {USAGE} - poprawne użycie")
    public Notice correctUsage = Notice.chat("<red>Poprawne użycie: <white>{USAGE}");

    @Comment("# Nagłówek listy poprawnych użyć")
    public Notice correctUsageHeader = Notice.chat("<red>Poprawne użycie komendy:");

    @Comment("# Pojedynczy wpis listy poprawnych użyć. {USAGE} - poprawne użycie")
    public Notice correctUsageEntry = Notice.chat("<dark_gray> - <white>{USAGE}");

    @Comment("# Wiadomość po przeładowaniu konfiguracji")
    public Notice reloadSuccess = Notice.chat("<green>Konfiguracja została przeładowana!");

    @Comment("# Wiadomość gdy graczowi wypadnie drop. {ITEM} - nazwa przedmiotu, {AMOUNT} - ilość")
    public Notice dropReceived = Notice.actionbar("<green>+ <white>{AMOUNT}x <green>{ITEM}");

    @Comment("# Wiadomość po włączeniu dropu w menu. {ITEM} - nazwa przedmiotu")
    public Notice dropEnabled = Notice.chat("<green>Włączono drop dla: <white>{ITEM}");

    @Comment("# Wiadomość po wyłączeniu dropu w menu. {ITEM} - nazwa przedmiotu")
    public Notice dropDisabled = Notice.chat("<red>Wyłączono drop dla: <white>{ITEM}");
}
