# Проект

## Как делаем
Создаете ветку со своей фамилией и клонируете ее (как делали на джаве в прошлом году), делаете свою часть в этой ветке. Потом нужно будет ваш бек разделенный на две части смержить и потестить работоспособность
В конце попытаемся смержить ветки или если не получится то просто весь код из папок в одну совместить)

## Примерная структура бека
Всего мне от вас нужен 1 эндпоинт: **/generate?name={name}&group={group}**

То есть вам нужно сделать эндпоит generate который принимает в себя аргументы name и/или group и возвращает **УЖЕ ГОТОВУЮ** таблицу

Заметьте, что либо name либо group **будут null** в зависимости от типа расписания

Сама таблица должна возвращаться в **бинарном** формате в поле spreadsheet_url

Структуру базы данных пока хз, но точно есть таблицы spreadsheets, groups, teachers со следующими полями:

### Spreadsheets
| Name       | Type                                     | Note                                     |
|------------|------------------------------------------|------------------------------------------|
| id         | int (PK)                                 | Unique identifier for the spreadsheet.   |
| spreadsheet_url  | BLOB                               | The spreadsheet     |
| created_at  | timestamp | Timestamp for when it was uploaded.      |

### Groups
| Name           | Type     | Note                               |
|----------------|----------|------------------------------------|
| id             | int (PK) | Unique identifier for the teacher. |
| group_name     | varchar  | Name of the student group.         |
| spreadsheet_id | int (FK) | Link to the spreadsheet's id.      |

### Teachers
| Name           | Type     | Note                               |
|----------------|----------|------------------------------------|
| id             | int (PK) | Unique identifier for the teacher. |
| name           | varchar  | Teacher's name.                    |
| spreadsheet_id | int (FK) | Link to the spreadsheet's id.      |

Все сгенерированные таблицы нужно сохранять, чтобы не создавать их заново. Вы просто будете проверять наличие ранее созданной таблицы в базе данных, и если таблица имеется, выводить ее без задержек генерации.

