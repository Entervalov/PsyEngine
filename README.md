[README-updated.md](https://github.com/user-attachments/files/24460590/README-updated.md)
# ⚛️ PsyEngine

![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square)
![Spigot](https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-yellow?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
[![](https://jitpack.io/v/YourUsername/PsyEngine.svg)](https://jitpack.io/#YourUsername/PsyEngine)

**PsyEngine** — это высокопроизводительный физический движок для Minecraft-серверов. Превращает блоки в реальные физические объекты с гравитацией, инерцией, точными RayTrace-коллизиями и термодинамикой.

Используется как **обычный плагин** (админы ставят в `/plugins`) или как **API библиотека** (разработчики подключают через Maven/Gradle).

---

## 🧩 Архитектура

PsyEngine состоит из четырёх основных компонентов:

1. **PhysicsLibrary** — главный singleton, через который создаются физические объекты.
2. **PhysicsEntity** — обёртка над Bukkit-сущностью, которая имеет физику.
3. **PhysicsProperties** — свойства материала (масса, упругость, трение, поведение).
4. **PhysicsListener** — обработчик ивентов (столкновения, взаимодействия).

**Как обычный плагин:**
- Админ ставит `PsyEngine-1.0.0.jar` в `/plugins`.
- Команды `/physics <subcommand>` доступны игрокам.
- Конфиги в `plugins/PsyEngine/config.yml` и `materials.yml`.

**Как API библиотека:**
- Разработчик подключает зависимость через Maven/Gradle.
- Использует `PhysicsLibrary.getInstance()` в своём плагине.
- Вызывает методы API для создания физических объектов и обработки событий.

---

## 📦 Установка на сервер (для админов)

1. Скачайте `PsyEngine-1.0.0.jar` из раздела Releases на GitHub.
2. Поместите файл в папку `plugins/`.
3. Запустите или перезапустите сервер.
4. Отредактируйте `plugins/PsyEngine/config.yml` по необходимости.

### Требования

- **Java 17+**
- **Spigot / Paper 1.18+** (рекомендуется Paper 1.20+)

---

## ⚙️ Конфигурация `config.yml`

```yaml
# ==========================================
#            PSYENGINE CONFIG
# ==========================================

general:
  language: "ru"      # en, ru
  debug-mode: false

commands:
  enabled: true       # Включить встроенные команды

  available:
    tower: true
    cannon: true
    fireball: true
    glass: true
    raft: true
    landslide: true
    info: true
    push: true
    clear: true
    debug: true
    reload: true
    test: true
    stats: true

  require-op: false         # false = может использовать любой игрок
  use-permissions: false    # true = использовать систему пермишенов
  permission-prefix: "physicsengine"

physics:
  gravity: 0.08             # Сила гравитации (м/тик²)
  max-velocity: 50.0        # Макс. скорость объекта
  min-velocity: 0.01        # Мин. скорость перед остановкой

water:
  buoyancy-multiplier: 1.2  # Плавучесть в воде
  drag-in-water-multiplier: 2.0
  splash-velocity-threshold: 1.5
  splash-particles: true
  splash-sound: true

optimization:
  sleep-mode: true          # Усыплять ли неподвижные объекты
  sleep-threshold: 0.1      # Порог скорости для сна
  sleep-delay: 40           # Тики перед усыпанием
  solidify-on-sleep: true   # Преобразовать в обычный блок при сне

performance:
  max-active-entities: 500  # Макс. кол-во объектов одновременно
  update-interval: 1        # Обновление каждый N тик
  unload-distance: 500      # Расстояние выгрузки объектов
  warn-on-limit: true
  auto-cleanup: true

effects:
  particles:
    enabled: true
    collision-spark: true
    water-splash: true
    thermal-effects: true
    sleep-indicator: true

  sounds:
    enabled: true
    collision-sound: true
    water-splash-sound: true
    thermal-sound: true

logging:
  level: "INFO"             # DEBUG, INFO, WARNING, ERROR
  log-entity-spawns: false
  log-collisions: false
  log-performance: false
  log-file: "physicsengine.log"

advanced:
  collision-damage-multiplier: 1.5
  thermal-conductivity: 0.3
  thermal-enabled: true
  allow-block-breaking: true
  allow-block-placement: true
  allow-entity-damage: true
  check-other-plugins: true
  safe-mode: false
```

---

## 📊 Конфигурация материалов `materials.yml`

Каждый блок может иметь **уникальные физические свойства**:

```yaml
IRON_BLOCK:
  mass: 1.8                  # Масса (влияет на инерцию и урон)
  drag: 0.02                 # Сопротивление воздуха
  friction: 0.3              # Трение об землю
  bounciness: 0.2            # Упругость при отскоке (0-1)
  buoyancy: 0.0              # Плавучесть в воде (0 = тонет)
  thermal-conductivity: 0.9  # Проводимость тепла
  break-threshold: 50.0      # Сила удара для разрушения
  behavior: "METAL"          # Поведение (кастомный тип)

SLIME_BLOCK:
  mass: 0.8
  drag: 0.1
  friction: 0.6
  bounciness: 0.9            # Очень прыгучий
  buoyancy: 0.8
  behavior: "BOUNCY"

ICE:
  mass: 0.9
  drag: 0.02
  friction: 0.05             # Очень скользкий
  bounciness: 0.1
  buoyancy: 0.92
  melting-point: 0.0
  behavior: "MELT"
```

---

## 🔌 Подключение как библиотеки (Maven / Gradle)

Используй **JitPack** для подключения PsyEngine как зависимость.

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.YourUsername</groupId>
        <artifactId>PsyEngine</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle (Kotlin)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Entervalov:PsyEngine:1.x.x")
}
```

Также не забудь добавить PsyEngine как софт-зависимость в твой `plugin.yml`:

```yaml
name: MyPhysicsPlugin
version: 1.0
main: com.example.MyPlugin
softdepend: [PsyEngine]
```

---

## 🎮 API для разработчиков

### 1. Получение инстанса PhysicsLibrary

```java
// PsyEngine работает как Singleton
PhysicsLibrary physics = PhysicsLibrary.getInstance();

if (physics == null) {
    // PsyEngine не установлен на сервер
    getLogger().warning("PsyEngine not found!");
    return;
}
```

### 2. Создание физического объекта

```java
public void createPhysicsBlock(Player player) {
    Location spawnLoc = player.getLocation().add(0, 5, 0);
    
    // Спавним блок железа с физикой
    PhysicsEntity entity = physics.spawnPhysicsBlock(spawnLoc, Material.IRON_BLOCK);
    
    if (entity != null) {
        // Устанавливаем начальную скорость
        Vector velocity = player.getLocation().getDirection().multiply(2.0);
        entity.setVelocity(velocity);
        
        player.sendMessage("✓ Физический блок создан!");
    }
}
```

### 3. Работа с PhysicsProperties

```java
public void configureBlock(PhysicsEntity entity) {
    PhysicsProperties props = entity.getProperties();
    
    // Изменяем свойства на лету
    props.setMass(10.0f);           // Тяжелый объект
    props.setBounciness(0.5f);      // Хорошо отскакивает
    props.setBehaviorType("CUSTOM"); // Кастомный тип
    
    // Или используй готовые пресеты
    // PhysicsProperties.heavy();   // Масса 20, низкая упругость
    // PhysicsProperties.bouncy();  // Высокая упругость
    // PhysicsProperties.floaty();  // Высокая плавучесть
}
```

### 4. Добавление импульса (толчка)

```java
// Толкнуть объект в направлении взгляда игрока
public void pushEntity(Player player, PhysicsEntity entity) {
    Vector impulse = player.getLocation().getDirection().multiply(3.0);
    entity.addVelocity(impulse);
}
```

### 5. Удаление объекта

```java
entity.kill();
```

### 6. Проверка состояния

```java
// Проверить, "спит" ли объект (неподвижен)
if (entity.isSleeping()) {
    player.sendMessage("Объект неподвижен");
}

// Получить текущую скорость
double speed = entity.getVelocity().length();

// Получить материал
org.bukkit.entity.FallingBlock fb = (org.bukkit.entity.FallingBlock) entity.getEntity();
Material material = fb.getMaterial();
```

---

## 📡 События (Events)

PsyEngine публикует Bukkit-события, на которые можно подписаться.

### PhysicsCollideEvent

Вызывается, когда физический объект сталкивается с блоком или другой сущностью.

```java
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.physicsengine.events.PhysicsCollideEvent;

public class MyPhysicsListener implements Listener {

    @EventHandler
    public void onPhysicsCollide(PhysicsCollideEvent event) {
        PhysicsEntity entity = event.getPhysicsEntity();
        Block hitBlock = event.getHitBlock();
        double impactForce = event.getImpactForce();
        
        // Пример: сильный удар разрушает блок
        if (impactForce > 50.0 && hitBlock != null) {
            hitBlock.breakNaturally();
            hitBlock.getWorld().createExplosion(hitBlock.getLocation(), 1.0f, false, true);
        }
        
        // Пример: специальное поведение для TNT
        String behavior = entity.getProperties().getBehaviorType();
        if ("EXPLODE".equalsIgnoreCase(behavior) && impactForce > 20.0) {
            entity.kill();
            hitBlock.getWorld().createExplosion(hitBlock.getLocation(), 3.0f);
        }
    }
}
```

Регистрация в твоём плагине:

```java
@Override
public void onEnable() {
    getServer().getPluginManager().registerEvents(new MyPhysicsListener(), this);
}
```

---

## 💡 Примеры использования

### Пример 1: Падающая щебень с разрушением

```java
public void createFallingRocks(Player player, int count) {
    Location center = player.getLocation().add(10, 20, 0);
    
    for (int i = 0; i < count; i++) {
        double offsetX = Math.random() * 5 - 2.5;
        double offsetZ = Math.random() * 5 - 2.5;
        
        Location spawnLoc = center.clone().add(offsetX, i * 1.2, offsetZ);
        PhysicsEntity rock = physics.spawnPhysicsBlock(spawnLoc, Material.STONE);
        
        if (rock != null) {
            // Легко повреждается при ударе
            rock.getProperties().setBreakThreshold(15.0f);
            rock.getProperties().setBehaviorType("ROCK");
        }
    }
}
```

### Пример 2: Пушка (снаряд с взрывом)

```java
public void fireCannonball(Player player) {
    Location muzzle = player.getEyeLocation().add(
        player.getLocation().getDirection().multiply(2)
    );
    
    PhysicsEntity cannonball = physics.spawnPhysicsBlock(muzzle, Material.IRON_BLOCK);
    
    if (cannonball != null) {
        // Настройки снаряда
        cannonball.getProperties().setMass(5.0f);
        cannonball.getProperties().setBounciness(0.1f); // Мало отскакивает
        cannonball.getProperties().setBehaviorType("EXPLOSIVE");
        
        // Скорость выстрела
        Vector shootDir = player.getLocation().getDirection().multiply(3.0);
        cannonball.setVelocity(shootDir);
        
        // Урон при ударе
        cannonball.setDamageOnImpact(true);
        cannonball.setImpactDamageMultiplier(5.0);
        
        player.getWorld().playSound(muzzle, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 0.8f);
    }
}
```

### Пример 3: Плавучий объект в воде

```java
public void createFloatingItem(Player player) {
    Location loc = player.getLocation();
    
    PhysicsEntity floatingBlock = physics.spawnPhysicsBlock(loc, Material.SLIME_BLOCK);
    
    if (floatingBlock != null) {
        // Высокая плавучесть
        floatingBlock.getProperties().setBuoyancy(0.95f);
        
        // Мало трения (гладкий)
        floatingBlock.getProperties().setFriction(0.2f);
        
        // Хорошо отскакивает
        floatingBlock.getProperties().setBounciness(0.8f);
        
        floatingBlock.getProperties().setBehaviorType("FLOATY");
    }
}
```

### Пример 4: Реагирование на столкновение

```java
public class MyPhysicsListener implements Listener {

    @EventHandler
    public void onPhysicsCollide(PhysicsCollideEvent event) {
        PhysicsEntity entity = event.getPhysicsEntity();
        Block block = event.getHitBlock();
        double force = event.getImpactForce();
        
        String behavior = entity.getProperties().getBehaviorType();
        
        // Огненный объект поджигает блоки
        if ("FIRE".equalsIgnoreCase(behavior) && block != null) {
            if (block.getType() == Material.OAK_PLANKS) {
                block.setType(Material.FIRE);
            }
        }
        
        // Лёд тает при соприкосновении
        if ("MELT".equalsIgnoreCase(behavior) && block != null) {
            if (block.getType() == Material.ICE) {
                block.setType(Material.WATER);
            }
        }
        
        // Взрывчатка
        if ("EXPLOSIVE".equalsIgnoreCase(behavior) && force > 30.0) {
            entity.getEntity().getWorld().createExplosion(
                entity.getEntity().getLocation(),
                2.5f,
                true,
                true
            );
            entity.kill();
        }
    }
}
```

---

## 🧪 Команды для тестирования

Если плагин работает на сервере, можешь использовать встроенные команды для теста:

```
/physics tower       — Строит башню из физических блоков
/physics cannon      — Выстреливает ядром
/physics fireball    — Запускает огненный снаряд
/physics raft        — Создаёт плот на воде
/physics glass       — Спавнит стекло (проверка ломаемости)
/physics info        — Инфо о ближайшем объекте
/physics stats       — Статистика движка
/physics clear       — Удаляет все физ. объекты
/physics debug       — Визуализация векторов
/physics reload      — Перезагрузить конфиг
```

---

## 🏗 Сборка исходников

Если хочешь модифицировать PsyEngine:

```bash
# Клонируем репо
git clone https://github.com/YourUsername/PsyEngine.git
cd PsyEngine

# Собираем через Maven
mvn clean package

# Готовый JAR в target/
java -jar target/PsyEngine-1.0.0.jar
```

---

## 📜 Лицензия

Распространяется под лицензией **MIT**. Можешь использовать в личных и коммерческих проектах при сохранении указания авторства.

---
**Q: Как отключить звуки/частицы?**
A: Установи `effects.sounds.enabled: false` и `effects.particles.enabled: false` в `config.yml`.
