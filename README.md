# 🎵 **CustomDiscPlugin**

A lightweight, configuration-driven plugin that adds a **custom music disc**, a **unique craftable sword**, and simple ability mechanics designed for SMPs, RPG-style servers, and texture-pack-themed worlds.

Everything is powered by **Custom Model Data** and clean config-based item definitions, making this plugin easy to integrate into any resource pack setup.

---

## 🎶 Custom Music Disc — **Reach**

A fully configurable custom disc defined in `config.yml`.

**Features:**

* Custom name, lore, and material
* Custom model data for texture-pack support
* ExactChoice crafting protection
* Interacts with jukeboxes for custom behavior

### 📥 How Players Obtain the Disc

Players can acquire the **Reach** disc naturally in gameplay:

> **When a Wither kills a Cat, it drops the Reach disc.**

This gives servers a fun, lore-friendly, progression-style method of obtaining the item without crafting or commands.

---

## ⚔️ Custom Sword — **SoulSave**

A powerful custom sword crafted from the Reach disc.

**Includes:**

* Custom name, lore, material, and model data
* Lore auto-inserts cooldown values from config
* Unbreakable with clean hidden attributes
* Simple ability system with configurable cooldown
* Perfect for unique gear progression or PvP modifiers

---

## 🛠 Crafting Recipe

```
 D 
NJN
FSF
```

**D** = Reach Disc (ExactChoice)
**F** = Nether Star
**J** = Jukebox
**N** = Netherite Ingot
**S** = Diamond Sword

Only the **exact custom Reach disc** works, preventing recipe bypassing and ensuring resource-pack accuracy.

---

## ⚙ Configuration

Everything is handled through `config.yml`, including:

* Item names
* Lore lines
* Materials
* Custom model data
* Cooldown duration
* Sword ability formatting

This makes the plugin extremely easy to theme and customize for any server.

---

## 📌 Commands

`/givedisc` – Gives the Reach disc (optional for admins/testing)
