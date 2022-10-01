package com.loudbook.minestom.api.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
@AllArgsConstructor
public enum TeamColors {
    RED(NamedTextColor.RED),
    BLUE(NamedTextColor.BLUE),
    YELLOW(NamedTextColor.YELLOW),
    GREEN(NamedTextColor.GREEN),
    PINK(NamedTextColor.LIGHT_PURPLE),
    PURPLE(NamedTextColor.DARK_PURPLE),
    AQUA(NamedTextColor.AQUA),
    WHITE(NamedTextColor.WHITE);

    @Getter
    private final NamedTextColor color;
}
