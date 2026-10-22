# Запуск: «Мои растения» (камера, приватная папка, экспорт в MediaStore)

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
