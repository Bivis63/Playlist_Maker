# Проект для составления плейлистов "Playlist Maker"

# Техническое задание

Проект представляет собой небольшое приложение для поиска треков,
использующее [API сервиса itunes]). Приложение предоставляет следующую функциональность:

- Поиск треков;
- Добавление и удаление треков в избранное и в плейлисты;
- Просмотр полной информации о треке;
- Прослушивание трека;
- Создание плейлистов;
- Отображение истории прослушеных треков.

## Общие требования

- Приложение должно поддерживать устройства, начиная с Android 6.0 (minSdkVersion = 23)

## Главный экран -- экран поиска треков
![Image alt](https://github.com/{username}/{repository}/raw/{branch}/{path}/image.png)

На этом экране пользователь может искать треки по любому непустому набору слов поискового запроса. Результаты поиска
представляют собой список, содержащий название трека, имя исполнителя, и продолжительность.

### Особенности экрана

Несколько особенностей экрана, которые нужно учитывать при реализации:

- При поисковом удачном поисковом запросе нам выдаёться список треков, если такого трека нету , то нам показывается
  плейсхолдер. Если в момент запроса отсутствует интернет или в ответе от сервера пришла ошибка, то также показывается
  плейсхолдер.
- Приложение хранит историю поиска последних 10 просмотренных треков, поэтому при пустой поисковой строки нам показывается
  история поиска и кнопка очистить историю.
- При вводе нового текста в поле ввода мы осуществляем новый поиск с debounce в 2000 миллисекунд.

## Экран плеера

На этом экране пользователь может прослушать трек, добить в избранное или в плейлист и вернуться назад к списку треков.

### Особенности экрана

Несколько особенностей экрана, которые нужно учитывать при реализации:

- При нажатии на любой трек мы переходим на этот экран.
- Мы видим полное описание трека (год, жанр, страну, альбом, время прослушивания, продолжительность и обложку).
- При нажатии на кнопку плей включается трек и запускаеться время прослушивания, если приложение сворачинается,
  то при востановление приложения время остаеться на том же месте где и было при сворачивание приложенеия.
- Если у трека нету обложки на её месте отображаеться плейсхолдер.
- Так же на экране отображаються 2 кнопки (добавить трек в плейлист и добавить трек в избранное).
- При нажатие на кнопку "добавить в плейлист" появляеться BottomSheet в который есть кнопка "новый плейлист",
  которая переводит нас на экран создания плейлиста, и список плейлистов. У плейлистов мы видим название,
  обложку, количество треков. При добавление трека в один из плейлистов , колличество треков увеличиваеться.
- Кнопка "добавить в избранное" выполнена ввиде сердечка, если трек добавлен в избранное то сердце должно быть
  красного цвета, если трека нету списке избранного, то он будет серого цвета. 

## Экран Медиатека

На этом экране можем перемещаться между фрагментами "Избранные треки" и "Плейлисты"

### Особенности экрана

- Экран "Медиатека" служит контейнером для фрагментов "Избранные треки" и "Плейлисты", и навигация между ними осуществляется с помощью TabLayoutMediator.
- На экране "Избранные треки" мы видим все треки, которые были добавлены туда. У каждой композиции отображается название, имя исполнителя, продолжительность
  и обложка.
- При нажатии на трек мы переходим на экран плеера.
- На экране "Плейлисты" отображается список всех плейлистов, используя здесь GridLayoutManager с двумя столбцами. Также у нас есть кнопка "Новый плейлист".
- При нажатии на кнопку "Новый плейлист" открывается экран для создания плейлиста. Нужно указать название, описание и обложку.
  На экране также есть кнопка "Создать". Она будет неактивной, пока мы не запишем название. Если мы вводим какие-то данные и хотим вернуться назад,
  то появляется диалоговое окно, которое предупреждает, что несохраненные данные будут утеряны, и предлагает отменить операцию или завершить её.
- При нажатии на плейлист мы переходим на экран "Плейлист". Здесь мы видим обложку, название, описание, количество треков, общую продолжительность всех треков,
  две кнопки (поделиться и настройки) и BottomSheet, в котором указан список всех треков в плейлисте.
- При нажатии на трек мы переходим на экран плеера. При длительном нажатии на трек появляется диалоговое окно, которое предлагает удалить трек из плейлиста.
- При нажатии на кнопку "Поделиться" появляется окно с выбором подходящего приложения, пользователю, с которым мы делимся плейлистом, приходит сообщение в
  формате: название плейлиста, описание, количество треков и пронумерованные треки.
- При нажатии на кнопку "Настройки" BottomSheet со списком треков скрывается, а появляется другое окно. В его заголовке отображается название плейлиста,
  обложка и количество треков. Мы можем удалить плейлист, отредактировать его или поделиться.
  

## Экран Настройки

На этом экране можно выбрать темную и светрую темы для приложения.

### Особенности экрана
- Для сохранения темной и светлой темы для приложения исплозуем SharedPreference 
