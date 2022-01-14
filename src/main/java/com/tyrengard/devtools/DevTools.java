package com.tyrengard.devtools;

import com.google.common.base.Predicates;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Predicates.not;

public final class DevTools extends JavaPlugin {
    private static final List<String> EXCLUDED_EVENT_CLASS_SIMPLE_NAMES = Arrays.asList(
            "GenericGameEvent",
            "EntityAirChangeEvent",
            "VehicleUpdateEvent",
            "TimeSkipEvent"
    );

    private static boolean loggingEnabled = false;

    @Override
    public void onEnable() {
        Logger logger = Bukkit.getLogger();

        Listener listener = new Listener() {};
        EventExecutor executor = (l, e) -> {
            if (loggingEnabled)
                getLogger().info("Event fired: " + e.getEventName());
        };

        try {
            ClassInfoList eventClasses = findAllBukkitEventClasses();

            logger.log(Level.INFO, "Found " + eventClasses.size() + " event classes.");
            logger.log(Level.INFO, "Registering listeners:");

            for (ClassInfo event : findAllBukkitEventClasses()) {
                @SuppressWarnings("unchecked")
                Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(event.getName());

                if (Arrays.stream(eventClass.getDeclaredMethods()).anyMatch(method ->
                        method.getParameterCount() == 0 && method.getName().equals("getHandlers"))) {
                    Bukkit.getPluginManager().registerEvent(eventClass, listener,
                            EventPriority.LOWEST, executor, this);
                    logger.log(Level.INFO, eventClass.getSimpleName());
                }
            }
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Scanned class wasn't found", e);
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "devtools" -> {
                if (args.length > 0) {
                    if (args[0].equals("event-logging")) {
                        loggingEnabled = !loggingEnabled;
                        sender.sendMessage("DevTools event logging set to " + loggingEnabled);
                    }
                }
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private ClassInfoList findAllBukkitEventClasses() {
        return new ClassGraph()
                .enableClassInfo()
                .scan() //you should use try-catch-resources instead
                .getClassInfo(Event.class.getName())
                .getSubclasses()
                .filter(info -> !EXCLUDED_EVENT_CLASS_SIMPLE_NAMES.contains(info.getSimpleName()))
                .filter(info -> !info.isAbstract());
    }
}
