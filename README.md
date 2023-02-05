# android-todo-list

Это тренировочный проект для хранения списка дел, отслеживания их управления и расставления в них приоритетов.

В приложении можно создавать, удалять и редактировать дела, устанавливать в них дедлайн и их важность.

Проект написан на Kotlin с использованием:
* RecyclerView, DiffUtils и ItemTouchHelper
* Room для хранения дел в базе данных
* Navigation component для навигации в приложении
* MVVM
* LiveData для отслеживания количества сделанных дел и хранения списка из базы данных
* Kotlin Coroutines

Главный экран

![List image](/img/list.jpg)

Экран создания дела

![Create task image](/img/create_task.jpg)

Экран редактирования дела

![Edit task image](/img/edit_task.jpg)

Удаление дела свайпом

![Swipe image](/img/swipe.jpg)
