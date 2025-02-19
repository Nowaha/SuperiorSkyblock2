package com.bgsoftware.superiorskyblock.service.message;

import com.bgsoftware.superiorskyblock.api.service.bossbar.BossBar;
import com.bgsoftware.superiorskyblock.api.service.message.IMessageComponent;
import com.bgsoftware.superiorskyblock.api.service.message.MessagesService;
import com.bgsoftware.superiorskyblock.formatting.Formatters;
import com.bgsoftware.superiorskyblock.lang.Message;
import com.bgsoftware.superiorskyblock.lang.component.MultipleComponents;
import com.bgsoftware.superiorskyblock.lang.component.impl.ActionBarComponent;
import com.bgsoftware.superiorskyblock.lang.component.impl.BossBarComponent;
import com.bgsoftware.superiorskyblock.lang.component.impl.ComplexMessageComponent;
import com.bgsoftware.superiorskyblock.lang.component.impl.RawMessageComponent;
import com.bgsoftware.superiorskyblock.lang.component.impl.SoundComponent;
import com.bgsoftware.superiorskyblock.lang.component.impl.TitleComponent;
import com.bgsoftware.superiorskyblock.wrappers.SoundWrapper;
import com.google.common.base.Preconditions;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MessagesServiceImpl implements MessagesService {

    public MessagesServiceImpl() {

    }

    @Nullable
    @Override
    public IMessageComponent parseComponent(YamlConfiguration config, String path) {
        if (config.isConfigurationSection(path)) {
            return MultipleComponents.parseSection(config.getConfigurationSection(path));
        } else {
            return RawMessageComponent.of(Formatters.COLOR_FORMATTER.format(config.getString(path, "")));
        }
    }

    @Nullable
    @Override
    public IMessageComponent getComponent(String messageName, Locale locale) {
        Message message;

        try {
            message = Message.valueOf(messageName.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException error) {
            // The given name was invalid.
            return null;
        }

        return message.isCustom() ? null : message.getComponent(locale);
    }

    @Override
    public Builder newBuilder() {
        return new BuilderImpl();
    }

    private static final class BuilderImpl implements Builder {

        private final List<IMessageComponent> messageComponents = new ArrayList<>();

        @Override
        public boolean addActionBar(@Nullable String message) {
            return addMessageComponent(ActionBarComponent.of(message));
        }

        @Override
        public boolean addBossBar(@Nullable String message, BossBar.Color color, int ticks) {
            return addMessageComponent(BossBarComponent.of(message, color, ticks));
        }

        @Override
        public boolean addComplexMessage(@Nullable TextComponent textComponent) {
            return addMessageComponent(ComplexMessageComponent.of(textComponent));
        }

        @Override
        public boolean addRawMessage(@Nullable String message) {
            return addMessageComponent(RawMessageComponent.of(message));
        }

        @Override
        public boolean addSound(Sound sound, float volume, float pitch) {
            return addMessageComponent(SoundComponent.of(new SoundWrapper(sound, volume, pitch)));
        }

        @Override
        public boolean addTitle(@Nullable String titleMessage, @Nullable String subtitleMessage, int fadeIn, int duration, int fadeOut) {
            return addMessageComponent(TitleComponent.of(titleMessage, subtitleMessage, fadeIn, duration, fadeOut));
        }

        @Override
        public boolean addMessageComponent(IMessageComponent messageComponent) {
            Preconditions.checkNotNull(messageComponent, "Cannot add null message components.");

            if (messageComponent.getType() != IMessageComponent.Type.EMPTY) {
                messageComponents.add(messageComponent);
                return true;
            }

            return false;
        }

        @Override
        public IMessageComponent build() {
            return MultipleComponents.of(messageComponents);
        }

    }

}
