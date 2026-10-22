# Запуск: «Список покупок» (Room + DataStore)

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
