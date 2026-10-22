# РКСМП, Модуль 5 – Хранение данных

Барнатович И.В., ИКБО-65-23. Каждая папка `pract_*` — отдельный проект.

Перед запуском:

- Android-проекты открывать в Android Studio: **File → Open → папка практики** (например, `pract_3`),
  дождаться синхронизации Gradle и нажать **Run** на эмуляторе (Pixel, Android 14+).
- Из терминала: в папке практики `./gradlew assembleDebug`, затем `adb install -r app/build/outputs/apk/debug/app-debug.apk`.
- Нужен JDK 17+ (подходит встроенный в Android Studio):
  `export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"`.

## `pract_1` — «Дневник путешествий» (запись в файлы filesDir)

Код: https://github.com/triaon/rksmpp-unit5/tree/main/pract_1

Открыть папку `pract_1` в Android Studio и нажать Run, или из терминала:

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n ru.mirea.barnatovich.traveldiary/.MainActivity
```

Что показать:
1. Пустой экран: «У вас пока нет записей».
2. «+ Новая запись» → ввести заголовок и текст → «Сохранить». Запись появится вверху списка.
3. Долгое нажатие на запись → «Удалить». Нажатие на запись → экран редактирования.
4. Файлы записей лежат в приватной папке:

```bash
adb shell run-as ru.mirea.barnatovich.traveldiary ls files
```

## `pract_2_3` — «Мои растения» (камера, приватная папка, экспорт в MediaStore)

Код: https://github.com/triaon/rksmpp-unit5/tree/main/pract_2_3

Открыть папку `pract_2_3` в Android Studio и нажать Run, или из терминала:

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n ru.mirea.barnatovich.myplants/.MainActivity
```

Что показать:
1. Пустой экран → «Сделать первое фото» → разрешить камеру → снять фото в приложении «Камера» → галочка.
2. Фото появится в сетке (3 колонки). Файлы лежат в приватной папке:

```bash
adb shell ls /sdcard/Android/data/ru.mirea.barnatovich.myplants/files/Pictures
```

3. Нажать на фото → «Экспорт в галерею» → Snackbar «Фото добавлено в галерею».
   Копия видна в «Файлы» → Pictures → MyPlants.

На эмуляторе задняя камера — virtual scene. Если камера открылась фронтальная (мультяшная картинка),
переключить на заднюю: «⋯» → значок смены камеры.

## `pract_4` — «Список покупок» (Room + DataStore)

Код: https://github.com/triaon/rksmpp-unit5/tree/main/pract_4

Открыть папку `pract_4` в Android Studio и нажать Run, или из терминала:

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n ru.mirea.barnatovich.shoppinglist/.MainActivity
```

Что показать:
1. При первом запуске список заполняется из `app/src/main/assets/purchases.json` (импорт один раз, флаг в DataStore).
2. Переключатель «Цвет купленных» в TopAppBar — купленные позиции становятся зелёными.
   Закрыть и снова открыть приложение — переключатель сохранил значение.
3. «+» → диалог создания; нажатие на покупку → экран деталей → «Изменить» (диалог) / «Удалить».
4. Чекбокс слева отмечает покупку как купленную, корзина справа удаляет её.

Чтобы снова проверить импорт из JSON: `adb shell pm clear ru.mirea.barnatovich.shoppinglist`.
