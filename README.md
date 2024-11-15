# DetivenChat

**Feature:**
- Everyting is customizable.
- AntiSwear:
  - 1. detection with font
  - 2. anti bypass system
  - 3. staff message
- Mention:
  - 1. title & subtitle
  - 2. sound
  - 3. action bar message
  - 4. chat message
- Connection:
  - 1. join & quit message
  - 2. with permission
  - 3. welcome message
- AntiSpam:
  - 1. chat anti spam 
  - 2. [command anti spam coming soon.]
- ChatFormat:
  - 1. format changable
  - 2. support placeholder api

**Permission:**
- `detivenchat.commands` : access for use `/detivenchat`
- `detivenchat.command.reload` : access for use `/detivenchat reload`
- `detivenchat.command.help` : access for use `/detivenchat help`
- `detivenchat.antiswear.notify` for staff notify.
- `detivenchat.antiswear.bypass` for swear bypass.
- `detivenchat.antispam.bypass` for spam bypass.

**DetivenChat API**
---
- api for detect the swear.
```java
import ir.detiven.detivenchat.api.API;
import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.modules.antiswear.objects.SwearObject;

import org.bukkit.plugin.java.JavaPlugin;

public class Example extends JavaPlugin {

    @Override
    public void onEnable() {
        API api = DetivenChat.getInstace().getApi();

        if (api == null) {
            getLogger().severe("need to use softdepend: DetivenChat");
            return;
        }

        SwearObject object = api.isSwear("hello");
        
        if (object.isSwear()) {
            getLogger().info(
                "detected: " + object.getWord() + ", is badword."
            );
        } else {
            getLogger().info("not badword");
        }
    }
}
```
---
- if player send bad word in chat, call this method.
```java
import ir.detiven.detivenchat.api.event.PlayerSwearEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public class ExampleEvent implements Listener {

    @EventHandler
    public void onSwear(PlayerSwearEvent event) {
        Player player = event.getPlayer();
        
        // your code.
    }
}
```
