# Github Api App
Github Api App - it's simple Android application, to show latest Android tools. Authorisation is performed by Firebase. All requests is performed by GithubApi.

## What's under the hood

**Language:** Kotlin

**Architecture:** MVVM (Model View View-Model)

**Concurrency:** Coroutines, Flow

**Dependency Injection:** Hilt

**DAO:** Room

**Api:** GithubApi

## App Screenshots
**Authorisation:**

<img src="https://user-images.githubusercontent.com/41620144/179197516-a4171509-8561-4ad3-b1cf-91b7ea7c4a76.png" width="25%" height="50%"> <img src="https://user-images.githubusercontent.com/41620144/179197564-85b9b755-36d0-42b7-818f-f6722460a6d3.png" width="25%" height="50%">

**Search 30 repositories (15+15) with search key + paggination, and sorted by descending of stars:**

<img src="https://user-images.githubusercontent.com/41620144/179197700-719722ee-7477-4e2a-ace9-d759e1317a93.png" width="25%" height="50%">

**See latests 20 searched repositories(can be viewed without Internet connection):**

<img src="https://user-images.githubusercontent.com/41620144/179197748-c2ba591d-ffd0-4f4e-bea8-b4cf65e6cdd8.png" width="25%" height="50%">

**Open repository page when cliking on element in list:**

<img src="https://user-images.githubusercontent.com/41620144/179201345-e520a298-3433-4137-8f5b-acf8ada0cfcd.png" width="25%" height="50%"> <img src="https://user-images.githubusercontent.com/41620144/179201365-c38793b9-8d6a-4dd1-ace3-d86a628822cf.png" width="25%" height="50%"> 

## Project Structure

```
com/example/headwaytestapp # Root Package
    .
    ├── authorization_view # All files related to Authorization View(Fragment, ViewModel, StateManager)
    |
    ├── dao                # DAO related files
    | 
    ├── di                 # DI related files
    |
    ├── latest_search_view # All files related to Latest Search View(Fragment, ViewModel)
    |
    ├── network            # All files related to Network
    |
    ├── repository         # Main Repository file
    |
    └── show_repos_view    # All files related to Repositories List View(Fragment, ViewModel, StateManager)
```

**To Dos:** Tests



## What were the requirements

**Завдання:**

Написати додаток, що шукає репозиторії за назвою, використовуючи **[GitHub API](https://docs.github.com/en/free-pro-team@latest/rest)**.

**Вимоги:**

- Пошук результатів доступний тільки після авторизації. Авторизація через обліковий запис GitHub;
- Результат пошуку має містити 30 елементів (використовувати 2 паралельні потоки, перші 15 елементів результату з 1 потоку і наступні 15 елементів з 2 потоку);
- Зробити пагінацію для підвантаження наступних результатів (використовуючи той самий підхід);
- Під час пошуку екран не має блокуватися (можна проскролювати результати пошуку);
- Репозиторії мають бути відсортованими за кількістю зірок;
- Під час натискання на назву відкривається браузер з інформацією про репозиторій, а сам елемент позначається як переглянутий;
- Додати екран з історією переглядів репозиторіїв. Історія має містити останні 20 переглянутих елементів. Має працювати оффлайн.
