## Injector

Mit diesem Tool ist es möglich ein infiziertes Plugin (dazu später mehr) in ein Bukkitplugin zu injezieren. Dieses Plugin wird
dann bei dem Start des Bukkitplugins geladen.

## Das infizierte Plugin

Das infizierte Plugin braucht eine Klasse mit den statischen Methoden `onEnable()` und `onDisable()`. Im Rootverzeichnismuss eine
extension.yml liegen, in der die Main-Class angeben ist (`main: de.infectedplugin.Main`).

## TODO

- GUI hinzufügen (WIP)
