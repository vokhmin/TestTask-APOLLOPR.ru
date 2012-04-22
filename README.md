Задача:
Требуется создать прототип веб приложения, которое бы интегрировалось с facebook и twitter.

Требования:
1. Приложениие должно уметь авторизовывать пользователя в Facebook и Twitter, и получать информацию по нему через API этих сетей
2. Должна быть страница которая бы показывала все мои статусы с твиттера и фейсбука отсортированные по дате создания, независимого от того, из какой системы они были получены. Эта страница должна содержать самые последние обновления с этих сервисов.
3. Все страницы должны быть логичным образом связаны друг с другом - требуется нормальное меню и редиректы по выполнению некоторых действий (например, после авторизации редирект на страницу из п. 2)
4. Ошибки должны обрабатываться и показываться соответствующие сообщения пользователю, например, сообщение о невозможности подключится к сервисам twitter и facebook и т.д.
5. Все должно быть реализовано на java. не нужно использовать javascript api для этих сервисов.
6. Время выполнения - максимум 3 рабочих дня. Постарайтесь упростить все на сколько возможно, чтобы успеть в этот срок.

Дополнительные требования (если успеете):
1. Система должна сохранять все статусы полученные из twitter и facebook в базу данных, и давать возможность их просматривать, не имея подключения к сервисам, из которых статусы были получены.

На выходе требуется предостваить:
1. Исходные коды проекта со всеми требуемыми библиотеками.
2. Собранный war файл, который можно положить в папку webapps дистрибутива томката и который сразу бы запустился в этой конфигурации (то есть никаких дополнительных настроек проводится с томкатом не должно).
3. Ссылка (относительный путь), по которой можно смотреть приложение.
4. Плюсом будет наличие ant (или maven) скрипта для сборки проекта, но необязательно.

Примечания:
1. В качестве базы данных для дополнительных требований можно взять любую embedded - лишь бы все, что требовалось для ее работы находилось в war файле.
2. Проверять будем следующим образом: скачаем zip архив с томкатом нужной версии с сайта apache, разархивируем его, в папку webapps положим war файл, настроим JAVA_HOME, запустим томкат, откроем браузер - если не запустится, задание не принимается.
3. spring, guice, spring mvc, struts, jdbc, hibernate и т.д., и т.д. - все, что угодно, лишь бы работало.
4. Пример того, что требуется в полноценном варианте интеграции - netvibes.com там есть виджеты для facebook и twitter. В нашей задаче нужно только уметь получать статусы. Постить или получать что-либо кроме статусов не нужно.
5. используемая java - 1.6, томкат >= 5.5.*

Ссылки: 
http://developers.facebook.com/docs/
http://apiwiki.twitter.com/
