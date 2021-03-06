﻿# ivor
Программа-виртуальный собеседник. Есть регистрация, залогинивание. Пользователи делятся на два типа admin/user. Это просто стобец в таблице User.
В зависимоти от типа пользователя меняется интерфейс. Все данные хранятся в удаленной БД (на моём компе). Поэтому присутствует собственный back на Node JS. Небольшая обертка над MySQL БД.
Видео: https://youtu.be/j42sIvyHQL8

## User
Есть возможность вести простой диалог с системой. Вводит сообщение, происходит обработка (см. далее), возвращается какой-то результат или его отсутствие.
Структура БД:
* **User**            - пользователи
* **Question**        - шаблоны вопросов
* **KeyWord**         - ключевые слова
* **Answer**          - конкретные ответы
* **Communication**   - связь Question и Answer
* **CommunicationKey** - связь KeyWord и Answer

## Структура БД. Тут более мене всё очевидно.
![фото стркутуры БД](https://github.com/lichtstrahl/ivor/blob/mvp/screenshots/%D0%A1%D1%82%D1%80%D1%83%D0%BA%D1%82%D1%83%D1%80%D0%B0%20%D0%91%D0%94.PNG)

Что не очевидно:
  1) Для каждой "связи" считается статистика: количество использований и корректность. Это нужно для дальнейшей селекции, т.е. выборки наиболее "подходящих" ответов.
  2) Последнее поле admin у всех User установлено в NULL, если оно не NULL то значит это админ и у него есть доступ к обучению системы.
  3) Есть селекция. Там хитрый SQL-запрос, удаляются самые популярные связи, имеющие самые отрицательные оценки.
Как работает я тебе покажу. Теперь самое интересное:

# API
Как это выглядит в моём клиенте на Android. Это так, для справки, ниже будет пародия на документацию.

    @GET("/api/answers")
    Observable<List<Answer>> loadAnswers();
    @GET("/api/answers/byID")
    Observable<Answer> loadAnswerByID(@Query("id") long id);
    @POST("/api/answers/update")
    Observable<Answer> replaceAnswer(@Body Answer answer);
    @POST("/api/answers/insert")
    Observable<Answer> insertAnswer(@Body PostContentDTO answer);
    @GET("/api/answers/answersForQuestion")
    Observable<List<Answer>> loadAnswersForQuestion(@Query("questionID") long id);
    @GET("/api/commands")
    Observable<List<Command>> loadCommands();


    @GET("/api/communications")
    Observable<List<Communication>> loadCommunications();
    @GET("/api/communications/communicationsForQuestion")
    Observable<List<Communication>> loadCommunicationsForQuestion(@Query("questionID") long id);
    @POST("/api/communications/update")
    Observable<Communication> replaceCommunication(@Body Communication communication);
    @POST("/api/communications/insert")
    Observable<Communication> insertCommunication(@Body PostComDTO com);
    @POST("/api/communications/delete")
    Observable<EmptyDTO> deleteCommunication(@Query("id") long id);


    @GET("/api/communicationkeys")
    Observable<List<CommunicationKey>> loadCommunicationKeys();
    @GET("/api/communicationkeys/communicationKeysForKeyWord")
    Observable<List<CommunicationKey>> loadCommunicationKeysForKeyWord(@Query("keyID") long id);
    @POST("/api/communicationkeys/update")
    Observable<CommunicationKey> replaceCommunicationKey(@Body CommunicationKey communicationKey);
    @POST("/api/communicationkeys/insert")
    Observable<CommunicationKey> insertCommunicationKey(@Body PostComKeyDTO comKey);
    @POST("/api/communicationkeys/delete")
    Observable<EmptyDTO> deleteCommunicationKey(@Query("id") long id);

    @GET("/api/keywords")
    Observable<List<KeyWord>> loadKeyWords();
    @GET("/api/keywords/byID")
    Observable<KeyWord> loadKeyWordByID(@Query("id") long id);
    @POST("/api/keywords/update")
    Observable<KeyWord> replaceKeyWord(@Body KeyWord word);
    @POST("/api/keywords/insert")
    Observable<KeyWord> insertKeyWord(@Body PostContentDTO word);
    @POST("/api/keywords/delete")
    Observable<EmptyDTO> deleteKeyWord(@Query("id") long id);

    @GET("/api/questions")
    Observable<List<Question>> loadQuestions();
    @GET("/api/questions/byID")
    Observable<Question> loadQuestionByID(@Query("id") long id);
    @POST("/api/questions/update")
    Observable<Question> replaceQuestion(@Body Question question);
    @POST("/api/questions/insert")
    Observable<Question> insertQuestion(@Body PostContentDTO question);
    @POST("/api/questions/delete")
    Observable<EmptyDTO> deleteQuestion(@Query("id") long id);

    @POST("/api/selection")
    Observable<EmptyDTO> selection();
    
    
  Пара скринов. Примерно такая же картинка должна быть и в Web.


  ## Регистрация
  ![Регистрация](https://github.com/lichtstrahl/ivor/blob/lite/screenshots/resitration.png)
  ![Проверка логина и пароля](https://github.com/lichtstrahl/ivor/blob/lite/screenshots/Проверка%20логина%20и%20пароля.png)
  
  ## Общение с системой
  ![Загрузка](https://github.com/lichtstrahl/ivor/blob/lite/screenshots/msg-1.png)
  ![Ответ](https://github.com/lichtstrahl/ivor/blob/lite/screenshots/msg-2.png)
  
  ## Режимы обучения
  Присутствует возможность обучения системы в клиентском приложении (только у admin-пользователей).
  Способы обучения:
  * Добавление новых ключевых слов
  * Добавление ответов на уже имеющиеся ключевые слова
  * Добавление новых шаблонов вопросов
  * Добавление ответов на уже имеющиеся вопросы
  ![Обучениe1](https://github.com/lichtstrahl/ivor/blob/lite/screenshots/work-mode.png)
  
