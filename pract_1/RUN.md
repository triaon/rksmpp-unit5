# Запуск: «Дневник путешествий» (запись в файлы filesDir)

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
