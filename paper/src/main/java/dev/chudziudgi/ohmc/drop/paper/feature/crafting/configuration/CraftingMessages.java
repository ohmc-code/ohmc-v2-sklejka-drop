package dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class CraftingMessages extends OkaeriConfig {
    @Comment("# Wiadomość wysyłana gdy nie ma miejsca w ekwipunku")
    public Notice noSpace = Notice.chat("<red>Nie masz miejsca w ekwipunku!");

    @Comment("# Wiadomość wysyłana gdy brak składników")
    public Notice missingIngredients = Notice.chat("<red>Brakuje Ci składników do wykonania tego craftingu!");

    @Comment("# Wiadomość wysyłana po udanym craftowaniu")
    public Notice craftingSuccess = Notice.chat("<green>Pomyślnie scraftowano przedmiot!");

    @Comment("# Wiadomość po utworzeniu nowego craftingu")
    public Notice recipeCreated = Notice.chat("<green>Pomyślnie utworzono nowy crafting: <yellow>{RECIPE_NAME}");

    @Comment("# Wiadomość po usunięciu craftingu")
    public Notice recipeDeleted = Notice.chat("<green>Pomyślnie usunięto crafting: <yellow>{RECIPE_NAME}");

    @Comment("# Wiadomość gdy crafting nie został znaleziony")
    public Notice recipeNotFound = Notice.chat("<red>Nie znaleziono craftingu o nazwie: <yellow>{RECIPE_NAME}");

    @Comment("# Wiadomość gdy crafting o takiej nazwie już istnieje")
    public Notice recipeAlreadyExists = Notice.chat("<red>Crafting o nazwie <yellow>{RECIPE_NAME} <red>już istnieje!");

    @Comment("# Wiadomość gdy gracz nie trzyma przedmiotu w ręce")
    public Notice holdItemForResult = Notice.chat("<red>Musisz trzymać przedmiot w ręce, który będzie wynikiem craftingu!");

    @Comment("# Wiadomość gdy otwarto GUI kreatora craftingów")
    public Notice creatorOpened = Notice.chat("<yellow>Ustaw przedmioty w siatce 3x3. Zamknij GUI aby zapisać crafting.");

    @Comment("# Wiadomość gdy siatka craftingu jest pusta")
    public Notice emptyGrid = Notice.chat("<red>Siatka craftingu jest pusta! Musisz ustawić przynajmniej jeden składnik.");

    @Comment("# Wiadomość gdy lista craftingów jest pusta")
    public Notice recipeListEmpty = Notice.chat("<yellow>Brak zdefiniowanych craftingów.");

    @Comment("# Nagłówek listy craftingów")
    public Notice recipeListHeader = Notice.chat("<green>Lista craftingów <gray>({COUNT}):");

    @Comment("# Wpis na liście craftingów")
    public Notice recipeListEntry = Notice.chat("<gray> - <yellow>{RECIPE_NAME} <dark_gray>| Wynik: <white>{RESULT}");
}
