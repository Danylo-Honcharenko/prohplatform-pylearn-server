INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (1, 'Тема 1. Вступ до JavaScript', e'JavaScript — це мова програмування, яка спочатку була створена для додавання інтерактивності вебсторінкам. Сьогодні JavaScript використовується не тільки у браузерах, але й для серверної розробки, мобільних застосунків та інших програм.

Код JavaScript можна виконувати безпосередньо у браузері. Для виведення інформації під час розробки часто використовується консоль:

console.log("Hello, JavaScript!");

JavaScript є мовою з динамічною типізацією, тому тип змінної визначається автоматично залежно від значення.', 1, '2026-08-15 14:58:03.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (2, 'Тема 2. Змінні та константи', e'Змінні використовуються для збереження даних у програмі. У сучасному JavaScript для їх оголошення переважно використовуються ключові слова let та const.

let age = 20;
const name = "Alex";

let дозволяє змінювати значення змінної після її створення. const використовується тоді, коли значення не повинно бути перепризначене.

Назви змінних повинні бути зрозумілими та відображати призначення даних, які в них зберігаються.', 1, '2026-08-15 14:58:30.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (3, 'Тема 3. Типи даних', e'JavaScript підтримує декілька основних типів даних. Серед них: number, string, boolean, undefined, null, bigint та symbol. Окремо виділяється тип object.

Наприклад:

let age = 25;
let username = "John";
let isActive = true;
let value = null;

Тип значення можна перевірити за допомогою оператора typeof:

console.log(typeof age);

Розуміння типів даних важливе для правильного виконання операцій та обробки інформації.', 1, '2026-08-15 14:58:55.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (4, 'Тема 4. Оператори', e'Оператори дозволяють виконувати різні дії над значеннями. Арифметичні оператори використовуються для математичних обчислень: +, -, *, /, %.

let a = 10;
let b = 5;

console.log(a + b);
console.log(a * b);

Також JavaScript має оператори порівняння: >, <, >=, <=, ===, !==, а також логічні оператори &&, || та !.', 1, '2026-08-15 14:59:16.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (5, 'Тема 1. Умовна конструкція if', e'Конструкція if дозволяє виконувати код тільки тоді, коли задана умова є істинною.

let age = 20;

if (age >= 18) {
    console.log("Доступ дозволено");
}

Для альтернативної дії використовується else, а для перевірки додаткових умов — else if.', 2, '2026-08-15 15:00:05.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (6, 'Тема 2. Конструкція switch', e'Конструкція switch зручна у випадках, коли одне значення потрібно порівняти з декількома можливими варіантами.

let day = 2;

switch (day) {
    case 1:
        console.log("Понеділок");
        break;
    case 2:
        console.log("Вівторок");
        break;
    default:
        console.log("Невідомий день");
}

Ключове слово break завершує виконання поточного блоку case.', 2, '2026-08-15 15:00:31.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (7, 'Тема 3. Цикл for', e'Цикл for використовується для багаторазового виконання певного блоку коду. Він особливо зручний, коли кількість повторень відома заздалегідь.

for (let i = 0; i < 5; i++) {
    console.log(i);
}

Цикл складається з початкового значення, умови продовження роботи та операції, яка виконується після кожної ітерації.', 2, '2026-08-15 15:00:49.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (8, 'Тема 4. Цикли while та do...while', e'Цикл while виконує код доти, доки задана умова залишається істинною.

let i = 0;

while (i < 5) {
    console.log(i);
    i++;
}

Цикл do...while відрізняється тим, що його тіло виконується щонайменше один раз, оскільки перевірка умови відбувається після виконання коду.', 2, '2026-08-15 15:01:40.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (9, 'Тема 1. Функції', e'Функція — це окремий блок коду, який виконує певне завдання та може використовуватися багаторазово.

function sum(a, b) {
    return a + b;
}

console.log(sum(5, 3));

Функції можуть приймати параметри та повертати результат за допомогою ключового слова return.', 3, '2026-08-15 15:02:44.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (10, 'Тема 2. Стрілкові функції', e'Стрілкові функції — це коротший синтаксис створення функцій у JavaScript.

const sum = (a, b) => {
    return a + b;
};

Для простих функцій запис можна скоротити:

const sum = (a, b) => a + b;

Стрілкові функції часто використовуються під час роботи з масивами, асинхронними операціями та сучасними JavaScript-фреймворками.', 3, '2026-08-15 15:03:14.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (11, 'Тема 3. Масиви', e'Масив дозволяє зберігати декілька значень в одній змінній.

let languages = ["JavaScript", "Python", "Java"];

Доступ до елементів здійснюється за індексом, починаючи з нуля:

console.log(languages[0]);

Для роботи з масивами JavaScript надає багато методів, наприклад push(), pop(), map(), filter() та forEach().', 3, '2026-08-15 15:03:37.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (12, 'Тема 4. Об''єкти', e'Об\'єкти використовуються для збереження пов\'язаних даних у форматі властивостей та їх значень.

const user = {
    name: "Alex",
    age: 25,
    isActive: true
};

console.log(user.name);

Об\'єкти є одним із ключових механізмів JavaScript і активно використовуються для представлення складних структур даних.', 3, '2026-08-15 15:03:57.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (13, 'Тема 1. Вступ до Python', e'Python — це високорівнева мова програмування, головною особливістю якої є простий та зрозумілий синтаксис. Вона використовується у веброзробці, автоматизації, аналізі даних, машинному навчанні та багатьох інших сферах.

Проста програма на Python може складатися лише з одного рядка:

print("Hello, Python!")

На відміну від багатьох інших мов, Python активно використовує відступи для визначення блоків коду.', 4, '2026-08-15 15:08:56.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (14, 'Тема 2. Змінні', e'Для створення змінної у Python не потрібно вказувати її тип. Достатньо вказати назву та присвоїти значення.

name = "Alex"
age = 20

Python автоматично визначає тип даних залежно від присвоєного значення.

Значення змінної можна змінювати під час виконання програми:

age = 20
age = 21', 4, '2026-08-15 15:09:25.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (15, 'Тема 3. Типи даних', e'До основних типів даних Python належать int, float, str та bool.

age = 25
price = 19.99
name = "Alex"
is_active = True

Для визначення типу змінної використовується функція type():

print(type(age))

Python також має складніші структури даних: списки, кортежі, словники та множини.', 4, '2026-08-15 15:11:17.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (16, 'Тема 4. Введення та виведення даних', e'Функція print() використовується для виведення інформації, а input() — для отримання даних від користувача.

name = input("Введіть ваше ім\'я: ")
print("Привіт,", name)

Функція input() повертає рядок. Якщо потрібно отримати число, результат необхідно перетворити:

age = int(input("Введіть вік: "))', 4, '2026-08-15 15:11:19.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (17, 'Тема 1. Умовна конструкція if', e'Конструкція if дозволяє виконувати певний код залежно від заданої умови.

age = 20

if age >= 18:
    print("Доступ дозволено")

Python використовує відступи для визначення коду, який належить до умови.', 5, '2026-08-15 15:13:44.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (18, 'Тема 2. else та elif', e'else дозволяє виконати альтернативний блок коду, якщо умова if не була виконана.

if age >= 18:
    print("Повнолітній")
else:
    print("Неповнолітній")

Для перевірки декількох умов використовується elif.

if score >= 90:
    print("Відмінно")
elif score >= 60:
    print("Добре")
else:
    print("Потрібно покращити результат")', 5, '2026-08-15 15:14:07.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (19, 'Тема 3. Цикл for', e'Цикл for дозволяє перебирати елементи послідовності або виконувати код задану кількість разів.

for i in range(5):
    print(i)

Функція range() створює послідовність чисел. Вона часто використовується разом із циклом for.

Також цикл може перебирати елементи списку:

languages = ["Python", "Java", "JavaScript"]

for language in languages:
    print(language)', 5, '2026-08-15 15:14:29.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (20, 'Тема 4. Цикл while', e'Цикл while виконується доти, доки задана умова є істинною.

i = 0

while i < 5:
    print(i)
    i += 1

Важливо змінювати значення, яке використовується в умові, інакше можна випадково створити нескінченний цикл.', 5, '2026-08-15 15:14:51.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (21, 'Тема 1. Функції', e'Функції дозволяють розділити програму на невеликі блоки та повторно використовувати написаний код.

Функція створюється за допомогою ключового слова def:

def hello():
    print("Привіт!")

hello()

Функції також можуть приймати параметри та повертати значення:

def sum_numbers(a, b):
    return a + b', 6, '2026-08-15 15:15:55.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (22, 'Тема 2. Списки', e'Список (list) — це структура даних, яка дозволяє зберігати декілька значень.

languages = ["Python", "JavaScript", "Java"]

Елементи списку мають індекси:

print(languages[0])

До списку можна додавати та видаляти елементи за допомогою методів append(), insert(), remove() та pop().', 6, '2026-08-15 15:16:25.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (23, 'Тема 3. Словники', e'Словник (dict) зберігає інформацію у вигляді пар «ключ — значення».

user = {
    "name": "Alex",
    "age": 25
}

print(user["name"])

Словники зручні для представлення об\'єктів, які мають декілька характеристик. Значення можна отримувати, додавати, змінювати та видаляти за відповідними ключами.', 6, '2026-08-15 15:16:43.000000', null);
INSERT INTO public.topics (id, name, description, module_id, created, updated) VALUES (24, 'Тема 4. Кортежі та множини', e'Кортеж (tuple) схожий на список, але після створення його елементи не можна змінювати.

coordinates = (10, 20)

Множина (set) використовується для збереження унікальних елементів:

numbers = {1, 2, 3, 3}
print(numbers)

У результаті повторювані значення будуть видалені. Множини також підтримують операції об\'єднання, перетину та різниці.', 6, '2026-08-15 15:17:03.000000', null);
