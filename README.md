# App analyze log
Программа для анализа логов

# Использование
На вход программы принимается поток из файла логов со строками типа
```
192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1" 200 2 44.510983 "-" "@list-item-updater" prio:0
```
принимаются 2 параметра

```
-u (минимально допустимый уровень доступности в процентах Например, "99.9")
```
```
-t (приемлемое время ответа в миллисекундах. Например, "45")
```

создание сборка проекта

```
mvn clean package
```

Пример использования программы
```
$ cat access.log | java -jar app-analyze-log-1.0-SNAPSHOT.jar -u 99.9 -t 45
```
Пример вывода результата работы программы, 
если доля отказов системы превышала указанную границу и уровень доступности в этот интервал времени. 
```
13:32:26	13:33:15	94.5
15:23:02	15:23:08	99.8
```