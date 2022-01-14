# DevTools
DevTools is a plugin that has a bunch of utilities that Spigot plugin developers may find useful. It is designed to be run in local test servers.

**WARNING: This plugin may use up a hefty amount of resources depending on what you're doing with it.**

# Features
* [**Event logging**](https://github.com/Team-Tyrengard/DevTools/wiki/Event-logging) - Ever wondered if an event is firing when a player does something in a niche situation? Ever wondered which events fire when a player does something specific? **DevTools allows you to log whenever a specific event class fires.** No more adding a bunch of listeners to your code just to find out which event to use!
* _Other features will be added!_

# Disclaimer
This plugin has only been tested for Spigot. Using Paper or other forks of Spigot may not work as intended, as [they may load Bukkit classes in differently](https://github.com/classgraph/classgraph/discussions/537#discussioncomment-1152567). Currently, there are no plans to support Spigot forks.

# Credits
- [Trigary](https://www.spigotmc.org/members/trigary.350074/) for providing the non-Reflection way of registering a Listener for all events in a [SpigotMC forum post](https://www.spigotmc.org/threads/listening-to-all-events-listing-all-events.337466/)