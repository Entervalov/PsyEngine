[README-updated.md](https://github.com/user-attachments/files/24460590/README-updated.md)
# ⚛️ PsyEngine

![Java](https://img.shields.io/badge/Java-21+-orange?style=flat-square)
![Spigot](https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-yellow?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
[![](https://jitpack.io/v/YourUsername/PsyEngine.svg)](https://jitpack.io/#Entervalov/PsyEngine)

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

- **Java 21+**
- **Spigot / Paper 1.20+**

---

## ⚙️ Конфигурация `config.yml`

```yaml
# ==========================================
#            PSYENGINE CONFIG
# ==========================================

general:
  # Язык сообщений плагина.
  # Доступно: ru (Русский), en (Английский)
  language: "ru"
  
  # Режим отладки. Включает вывод подробной информации в консоль и визуализацию векторов (для разработчиков).
  # true = включить, false = выключить
  debug-mode: false

commands:
  # Глобальный переключатель встроенных команд плагина (/physics, /psy).
  # Если false, команды вообще не будут зарегистрированы.
  enabled: true

  # Включение/отключение отдельных подкоманд.
  # Полезно, если вы не хотите давать игрокам доступ к стресс-тестам или чистке мира.
  available:
    tower: true      # Строит башню из блоков (/physics tower)
    cannon: true     # Создает пушку и стреляет ядром (/physics cannon)
    fireball: true   # Запускает огненный шар (/physics fireball)
    glass: true      # Спавнит стекло для теста разрушаемости (/physics glass)
    raft: true       # Спавнит плот для теста воды (/physics raft)
    landslide: true  # Создает оползень (/physics landslide)
    info: true       # Показывает инфо о ближайшем физ. объекте (/physics info)
    push: true       # Толкает ближайший объект (/physics push)
    clear: true      # Удаляет ВСЕ физические объекты (/physics clear)
    debug: true      # Включает визуальный дебаг (/physics debug)
    reload: true     # Перезагружает конфиг (/physics reload)
    test: true       # Стресс-тест: спавнит много блоков (/physics test)
    stats: true      # Показывает статистику движка (/physics stats)

  # Требовать ли статус оператора (OP) для использования команд.
  # Если false, команды доступны всем игрокам (не рекомендуется для публичных серверов).
  require-op: false

  # Использовать ли систему прав (permissions) вместо OP.
  # Если true, игрокам нужны права вида physicsengine.tower, physicsengine.reload и т.д.
  use-permissions: false
  
  # Префикс для прав доступа (например, physicsengine.admin).
  permission-prefix: "physicsengine"

physics:
  # Сила гравитации, применяемая к объектам каждый тик.
  # 0.04 = лунная гравитация, 0.08 = нормальная (как в Minecraft), 0.2 = очень тяжелая.
  gravity: 0.08

  # Максимальная скорость падения или полета объекта (блоков/тик).
  # Ограничивает "разгон" объектов, чтобы они не пролетали сквозь мир.
  max-velocity: 50.0

  # Минимальная скорость, ниже которой объект считается остановившимся.
  # Если скорость меньше этого значения, она обнуляется (предотвращает бесконечное скольжение).
  min-velocity: 0.01

water:
  # Множитель выталкивающей силы воды (Архимедова сила).
  # >1.0 - объекты всплывают быстрее, <1.0 - медленнее.
  buoyancy-multiplier: 1.2

  # Сопротивление воды движению.
  # Чем выше значение, тем быстрее объекты тормозят в воде.
  drag-in-water-multiplier: 2.0

  # Минимальная скорость падения в воду, чтобы вызвать всплеск.
  splash-velocity-threshold: 1.5

  # Создавать ли частицы брызг при падении в воду.
  splash-particles: true

  # Проигрывать ли звук всплеска.
  splash-sound: true

optimization:
  # Включить "режим сна". Значительно экономит ресурсы CPU.
  # Если объект долго не двигается, он перестает обсчитываться физикой.
  sleep-mode: true

  # Скорость, ниже которой объект считается неподвижным (блоков/тик).
  sleep-threshold: 0.1

  # Сколько тиков (1/20 сек) объект должен быть неподвижен, чтобы "уснуть".
  # 40 тиков = 2 секунды покоя.
  sleep-delay: 40

  # Превращать ли "уснувший" физический объект обратно в твердый блок Minecraft.
  # Если true, объект станет обычным блоком в мире. Если false, останется сущностью.
  solidify-on-sleep: true

performance:
  # Жесткий лимит количества активных физических объектов на сервере.
  # Если лимит превышен, самые старые объекты будут удаляться.
  max-active-entities: 500

  # Интервал обновления физики (в тиках).
  # 1 = каждый тик (максимальная плавность), 2 = через тик (лучше производительность, но рывки).
  update-interval: 1

  # Расстояние от игрока, на котором физика объектов перестает работать и они выгружаются.
  unload-distance: 500

  # Предупреждать в консоли, если достигнут лимит объектов.
  warn-on-limit: true

  # Автоматически удалять объекты, которые упали в пустоту или улетели слишком далеко.
  auto-cleanup: true

effects:
  particles:
    enabled: true             # Глобальный переключатель частиц
    collision-spark: true     # Искры/пыль при ударе о землю
    water-splash: true        # Брызги воды
    thermal-effects: true     # Дым/пар от горячих или холодных объектов
    sleep-indicator: true     # Частицы "Zzz" над спящими объектами (для дебага)

  sounds:
    enabled: true             # Глобальный переключатель звуков
    collision-sound: true     # Звук удара о поверхность
    water-splash-sound: true  # Звук падения в воду
    thermal-sound: true       # Шипение при тушении/горении

logging:
  # Уровень логирования в консоль/файл.
  # Варианты: DEBUG (все детали), INFO (основное), WARNING (только проблемы), ERROR (ошибки).
  level: "INFO"

  log-entity-spawns: false    # Писать в лог каждый раз при спавне объекта
  log-collisions: false       # Писать в лог каждый удар (может создать ОГРОМНЫЙ лог!)
  log-performance: false      # Писать статистику нагрузки на CPU
  log-file: "physicsengine.log" # Имя файла логов в папке плагина

advanced:
  # Множитель урона сущностям при столкновении.
  # Урон = Масса * Скорость * Множитель.
  collision-damage-multiplier: 1.5

  # Базовая теплопроводность (как быстро объекты нагреваются/остывают).
  thermal-conductivity: 0.3

  # Включить систему термодинамики (температура, плавление, горение).
  thermal-enabled: true

  # Разрешить физическим объектам ломать блоки при падении (если сила удара велика).
  allow-block-breaking: true

  # Разрешить объектам становиться блоками (заменять блоки в мире) при остановке.
  allow-block-placement: true

  # Разрешить наносить урон мобам и игрокам.
  allow-entity-damage: true

  # Проверять регионы WorldGuard/других плагинов перед изменением блоков.
  check-other-plugins: true

  # Безопасный режим: если true, отключает все разрушительные действия (взрывы, ломку блоков, пожары).
  # Используйте, если боитесь гриферов или багов.
  safe-mode: false
```

---

## 📊 Конфигурация материалов `materials.yml`

Каждый блок может иметь **уникальные физические свойства**:

```yaml
IRON_BLOCK: # Железный блок
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

Используйте **JitPack** для подключения PsyEngine как зависимость.

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
        <groupId>com.github.Entervalov</groupId>
        <artifactId>PsyEngine</artifactId>
        <version>1.X.X</version> # В зависимости от нынешней версии плагина испльзуйте цифры (Пример: 1.2.0; 1.2.0.1 и т.д.)
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
    compileOnly("com.github.Entervalov:PsyEngine:1.X.X") # В зависимости от нынешней версии плагина испльзуйте цифры (Пример: 1.2.0; 1.2.0.1 и т.д.)
}
```

Также не забудьте добавить PsyEngine как софт-зависимость в твой `plugin.yml`:

```yaml
name: MyPhysicsPlugin
version: 1.0
main: com.example.MyPlugin
softdepend: [PsyEngine] # < -- Вот так
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
    
    // Или используйте готовые пресеты
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

Регистрация в вашем плагине:

```java
@Override
public void onEnable() {
    getServer().getPluginManager().registerEvents(new MyPhysicsListener(), this);
}
```

---

## 💡 Примеры использования

### Пример 1: Падающий "щебень" с разрушением

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

Если плагин работает на сервере, можете использовать встроенные команды для теста:

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

Если хочите модифицировать PsyEngine:

```bash
# Клонируете репо
git clone https://github.com/Entervalov/PsyEngine.git
cd PsyEngine

# Собираете через Maven
mvn clean package

# Готовый JAR в target/
java -jar target/PsyEngine-1.0.0.jar
```

---

## 📜 Лицензия

Распространяется под лицензией **MIT**. Вы можете использовать в личных и коммерческих проектах при сохранении указания авторства.

---
**Q: Как отключить звуки/частицы?**
A: Установи `effects.sounds.enabled: false` и `effects.particles.enabled: false` в `config.yml`.
